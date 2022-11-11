package com.boyun.boyunfile.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.boyun.boyunfile.common.RestResult;
import com.boyun.boyunfile.domain.User;
import com.boyun.boyunfile.domain.UserFile;
import com.boyun.boyunfile.dto.UserfileListDTO;
import com.boyun.boyunfile.vo.UserfileListVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "share", description = "该接口为分享文件接口，可以对文件进行分享等操作。")
@RestController
@Slf4j
@RequestMapping("/share")
public class ShareController {

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
    public RestResult shareFile() {
        //todo
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
