package com.boyun.boyunfile.controller;

import com.boyun.boyunfile.common.RestResult;
import com.boyun.boyunfile.domain.Share;
import com.boyun.boyunfile.dto.ShareFileDTO;
import com.boyun.boyunfile.service.ShareFileService;
import com.boyun.boyunfile.service.ShareService;
import com.boyun.boyunfile.service.UserFileService;
import com.boyun.boyunfile.util.DateUtil;
import io.jsonwebtoken.lang.Strings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    public RestResult getShareList() {
        //todo
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
    @RequestMapping(value = "/share", method = RequestMethod.POST)
    @ResponseBody
    public RestResult shareFile(@RequestBody ShareFileDTO shareFileDTO) {

        Share share = new Share();
        share.setUserId(shareFileDTO.getUserId());
        share.setEndTime(shareFileDTO.getEndtime());
        share.setShareType(Share.SHAREALL);
        if(Strings.hasLength(shareFileDTO.getExtractionCode())){
            share.setExtractionCode(shareFileDTO.getExtractionCode());
            share.setShareType(Share.SHAREPRIVATE);
        }
        String uuid = UUID.randomUUID().toString().replace("-", "");
        share.setShareBatchnum(uuid);
        share.setShareTime(DateUtil.getCurrentTime());
        share.setShareStatus(Share.NOMALTIME);

        if (Objects.isNull(share.getUserId())){
            return RestResult.fail().message("参数错误");
        }



        return null;
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
