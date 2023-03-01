package com.boyun.boyunfile.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.boyun.boyunfile.common.RestResult;
import com.boyun.boyunfile.domain.RecoveryFile;
import com.boyun.boyunfile.domain.User;
import com.boyun.boyunfile.domain.UserFile;
import com.boyun.boyunfile.dto.*;
import com.boyun.boyunfile.service.RecoveryFileService;
import com.boyun.boyunfile.service.UserFileService;
import com.boyun.boyunfile.service.UserService;
import com.boyun.boyunfile.vo.RecoveryFileListVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "recoveryFile", description = "该接口为回收站文件接口，可以对已删除文件进行还原或永久删除。")
@RestController
@Slf4j
@RequestMapping("/recoveryfile")
public class RecoveryFileController {

    @Resource
    private UserService userService;

    @Resource
    private UserFileService userFileService;

    @Resource
    private RecoveryFileService recoveryFileService;

    @Operation(summary = "回收站文件列表", description = "获取回收站文件列表", tags = { "getrecoveryfilelist" })
    @GetMapping(value = "/getrecoveryfilelist")
    @ResponseBody
    public RestResult<RecoveryFileListVO> getRecoveryFileList(RecoveryFileListDTO recoveryFileListDTO){
        List<RecoveryFileListVO> recoveryFileList = recoveryFileService.getRecoveryFileList(recoveryFileListDTO.getUserId());

        Map<String, Object> map = new HashMap<>();

        int total = recoveryFileList.size();

        map.put("total", total);
        map.put("list", recoveryFileList);

        return RestResult.success().data(map);
    }

    @Operation(summary = "还原文件", description = "可以还原文件或者目录", tags = { "recoveryfile" })
    @RequestMapping(value = "/recoveryfile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult recoveryFile(@RequestBody RecoveryFileDTO recoveryFileDTO) {

        RecoveryFile recoveryFile = recoveryFileService.getById(recoveryFileDTO.getRecoveryFileId());

        UserFile userFile = userFileService.getIsDeleteUserFile(recoveryFile.getUserFileId());
        if(!isExistOfFather(userFile)){
            return RestResult.fail().message("原文件夹不存在或被删除");
        }

        recoveryFileService.recoveryFile(recoveryFileDTO.getRecoveryFileId());

        return RestResult.success();
    }


    @Operation(summary = "批量还原文件", description = "批量还原文件或者目录", tags = { "batchrecoveryfile" })
    @RequestMapping(value = "/batchrecoveryfile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult batcheleteFile(@RequestBody BatchRecoveryFileDTO batchRecoveryFileDTO) {

        List<RecoveryFile> files = JSON.parseArray(batchRecoveryFileDTO.getFiles(), RecoveryFile.class);
        for (RecoveryFile file : files) {
            recoveryFileService.recoveryFile(file.getRecoveryFileId());
        }

        return RestResult.success();

    }


    @Operation(summary = "删除回收站文件", description = "删除回收站文件", tags = { "deleterecoveryfile" })
    @RequestMapping(value = "/deleterecoveryfile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult deleteRecoveryFile(@RequestBody DeleteRecoveryFileDTO deleteRecoveryFileDTO){

        recoveryFileService.removeById(deleteRecoveryFileDTO.getRecoveryFileId());

        return RestResult.success().message("删除成功");

    }

    @Operation(summary = "批量删除回收站文件", description = "批量删除回收站文件", tags = { "batchdeleterecoveryfile" })
    @RequestMapping(value = "/batchdeleterecoveryfile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult<String> batchDeleteRecoveryFile(@RequestBody BatchDeleteRecoveryFileDTO batchDeleteRecoveryFileDTO) {

        List<RecoveryFile> files = JSON.parseArray(batchDeleteRecoveryFileDTO.getFiles(), RecoveryFile.class);
        for (RecoveryFile file : files) {
            recoveryFileService.removeById(file.getRecoveryFileId());
        }

        return RestResult.success().message("批量删除文件成功");
    }

    private boolean isExistOfFather(UserFile userFile) {

        String path = userFile.getFilePath().substring(0, userFile.getFilePath().lastIndexOf("/", userFile.getFilePath().length() - 2) + 1);
        if("/".equals(path)){
            return true;
        }
        String fatherName = userFile.getFilePath().substring(path.length(), userFile.getFilePath().length() - 1);

        LambdaQueryWrapper<UserFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFile::getFilePath, path)
                .eq(UserFile::getFileName, fatherName)
                .eq(UserFile::getIsDir, 1)
                .eq(UserFile::getDeleteFlag, 0)
                .eq(UserFile::getUserId, userFile.getUserId());
        List<UserFile> list = userFileService.list(queryWrapper);
        if(list.isEmpty()){
            return false;
        }
        return true;
    }

}
