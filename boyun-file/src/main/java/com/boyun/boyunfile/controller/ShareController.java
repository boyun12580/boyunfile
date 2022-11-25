package com.boyun.boyunfile.controller;

import com.alibaba.fastjson.JSONArray;
import com.boyun.boyunfile.common.RestResult;
import com.boyun.boyunfile.constant.ShareConstant;
import com.boyun.boyunfile.domain.Share;
import com.boyun.boyunfile.domain.ShareFile;
import com.boyun.boyunfile.domain.UserFile;
import com.boyun.boyunfile.dto.ShareFileDTO;
import com.boyun.boyunfile.dto.ShareListDTO;
import com.boyun.boyunfile.service.ShareFileService;
import com.boyun.boyunfile.service.ShareService;
import com.boyun.boyunfile.service.UserFileService;
import com.boyun.boyunfile.util.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Tag(name = "share", description = "该接口为分享文件接口，可以对文件进行分享等操作。")
@RestController
@Slf4j
@RequestMapping("/share")
public class ShareController {

    @Resource
    private ShareService shareService;

    @Resource
    private ShareFileService shareFileService;

    @Resource
    private UserFileService userfileService;


    @Operation(summary = "获取我的分享文件列表", description = "用来做前台文件列表展示", tags = { "getsharelist" })
    @GetMapping(value = "/getsharelist")
    @ResponseBody
    public RestResult getShareList(@RequestBody ShareListDTO shareListDTO) {



        return null;
    }

    @Operation(summary = "其他用户获取某个分享文件列表", description = "用来做前台文件列表展示", tags = { "getsharefilelist" })
    @GetMapping(value = "/getsharefilelist")
    @ResponseBody
    public RestResult getShareFileList() {
        //todo
        return null;
    }

    @Operation(summary = "分享文件", description = "分享文件", tags = { "sharefile" })
    @RequestMapping(value = "/sharefile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult shareFile(@RequestBody ShareFileDTO shareFileDTO) throws ParseException {

        if (Objects.isNull(shareFileDTO.getUserId()) || Objects.isNull(shareFileDTO.getFiles())){
            return RestResult.fail().message("参数错误");
        }

        String endTime = shareFileDTO.getEndTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = df.parse(endTime);
        if(date.getTime() < new Date().getTime()){
            return RestResult.fail().message("所选日期需大于当前日期");
        }


        Map<String, Object> map = new HashMap<>();
        map.put("extractionCode", "");
        Share share = new Share();
        share.setUserId(shareFileDTO.getUserId());
        share.setEndTime(shareFileDTO.getEndTime());
        share.setShareType(ShareConstant.SHAREALL);
        if(shareFileDTO.getShareType().equals(ShareConstant.SHAREPRIVATE)){

            SplittableRandom splittableRandom = new SplittableRandom();
            String extractionCode = String.valueOf(splittableRandom.nextInt(10000, 99999));
//
            share.setExtractionCode(extractionCode);
            share.setShareType(ShareConstant.SHAREPRIVATE);
            map.replace("extractionCode", "", extractionCode);
        }
        String uuid = UUID.randomUUID().toString().replace("-", "");
        share.setShareBatchnum(uuid);
        share.setShareTime(DateUtil.getCurrentTime());
        share.setShareStatus(ShareConstant.NOMALTIME);


        List<UserFile> files = JSONArray.parseArray(shareFileDTO.getFiles(), UserFile.class);

        for (UserFile file : files) {
            UserFile userFile = userfileService.getById(file.getUserFileId());
            ShareFile shareFile = new ShareFile();
            shareFile.setSharebatchnum(uuid);
            shareFile.setSharefilepath(userFile.getFilePath());
            shareFile.setUserfileid(userFile.getUserFileId());
            shareFileService.save(shareFile);
        }

        shareService.save(share);

        map.put("shareBatchNum", uuid);

        return RestResult.success().message("分享成功").data(map);
    }

    @Operation(summary = "取消分享文件", description = "取消分享文件", tags = { "deleteshare" })
    @RequestMapping(value = "/deleteshare", method = RequestMethod.POST)
    @ResponseBody
    public RestResult deleteShare() {
        //todo
        return null;
    }

    @Operation(summary = "保存分享文件到我的网盘", description = "保存分享文件到我的网盘", tags = { "savesharefile" })
    @RequestMapping(value = "/savesharefile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult saveShareFile() {
        //todo
        return null;
    }

}
