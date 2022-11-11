package com.boyun.boyunfile.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.boyun.boyunfile.common.RestResult;
import com.boyun.boyunfile.domain.File;
import com.boyun.boyunfile.domain.Storage;
import com.boyun.boyunfile.domain.User;
import com.boyun.boyunfile.domain.UserFile;
import com.boyun.boyunfile.dto.DownloadFileDTO;
import com.boyun.boyunfile.dto.UploadFileDTO;
import com.boyun.boyunfile.service.FileService;
import com.boyun.boyunfile.service.FiletransferService;
import com.boyun.boyunfile.service.UserService;
import com.boyun.boyunfile.service.UserfileService;
import com.boyun.boyunfile.util.DateUtil;
import com.boyun.boyunfile.util.FileUtil;
import com.boyun.boyunfile.vo.UploadFileVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "filetransfer", description = "该接口为文件传输接口，主要用来做文件的上传和下载")
@RestController
@RequestMapping("/filetransfer")
public class FiletransferController {

    @Resource
    UserService userService;
    @Resource
    FileService fileService;
    @Resource
    UserfileService userfileService;
    @Resource
    FiletransferService filetransferService;

    @Operation(summary = "极速上传", description = "校验文件MD5判断文件是否存在，如果存在直接上传成功并返回skipUpload=true，如果不存在返回skipUpload=false需要再次调用该接口的POST方法", tags = {"filetransfer"})
    @GetMapping(value = "/uploadfile")
    @ResponseBody
    public RestResult<UploadFileVo> uploadFileSpeed(UploadFileDTO uploadFileDto, @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);

        UploadFileVo uploadFileVo = new UploadFileVo();
        Map<String, Object> param = new HashMap<>();
        param.put("identifier", uploadFileDto.getIdentifier());
        synchronized (FiletransferController.class) {
            List<File> list = fileService.listByMap(param);
            if (list != null && !list.isEmpty()) {
                File file = list.get(0);

                UserFile userfile = new UserFile();
                userfile.setFileId(file.getFileId());
                userfile.setUserId(sessionUser.getUserId());
                userfile.setFilePath(uploadFileDto.getFilePath());
                userfile.setExtendName(FileUtil.getFileExtendName(uploadFileDto.getFilename()));
                userfile.setIsDir(0);
                userfile.setUploadTime(DateUtil.getCurrentTime());
                userfile.setDeleteFlag(0);

                //对重复文件名进行增加后缀处理
                StringBuilder fileName = new StringBuilder(uploadFileDto.getFilename().substring(0, uploadFileDto.getFilename().lastIndexOf(".")));
                int temp = 0;
                StringBuilder originFileName = new StringBuilder(fileName.toString());
                LambdaQueryWrapper<UserFile> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(UserFile::getFileName, fileName.toString());
                wrapper.eq(UserFile::getFilePath, uploadFileDto.getFilePath());
                List<UserFile> files = userfileService.list(wrapper);
                while(files != null && files.size() > 0){
                    fileName = new StringBuilder(originFileName.toString());
                    fileName.append('(');
                    fileName.append(temp++);
                    fileName.append(')');
                    wrapper = new LambdaQueryWrapper<UserFile>()
                            .eq(UserFile::getFileName, fileName.toString())
                            .eq(UserFile::getFilePath, uploadFileDto.getFilePath());
                    files = userfileService.list(wrapper);
                }

                userfile.setFileName(fileName.toString());

                userfileService.save(userfile);
                // fileService.increaseFilePointCount(file.getFileId());
                uploadFileVo.setSkipUpload(true);

            } else {
                uploadFileVo.setSkipUpload(false);

            }
        }
        return RestResult.success().data(uploadFileVo);

    }

    @Operation(summary = "上传文件", description = "真正的上传文件接口", tags = {"filetransfer"})
    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult<UploadFileVo> uploadFile(HttpServletRequest request, UploadFileDTO uploadFileDto, @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);

        filetransferService.uploadFile(request, uploadFileDto, sessionUser.getUserId());
        UploadFileVo uploadFileVo = new UploadFileVo();
        return RestResult.success().data(uploadFileVo);

    }

    @Operation(summary = "下载文件", description = "下载文件接口", tags = {"filetransfer"})
    @RequestMapping(value = "/downloadfile", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response, DownloadFileDTO downloadFileDTO) {
        filetransferService.downloadFile(response, downloadFileDTO);
    }

    @Operation(summary = "获取存储信息", description = "获取存储信息", tags = {"filetransfer"})
    @RequestMapping(value = "/getstorage", method = RequestMethod.GET)
    @ResponseBody
    public RestResult<Long> getStorage(@RequestHeader("token") String token) {

        User sessionUserBean = userService.getUserByToken(token);
        Storage storageBean = new Storage();


        Long storageSize = filetransferService.selectStorageSizeByUserId(sessionUserBean.getUserId());
        return RestResult.success().data(storageSize);

    }
}