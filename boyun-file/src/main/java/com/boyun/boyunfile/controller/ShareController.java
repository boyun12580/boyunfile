package com.boyun.boyunfile.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boyun.boyunfile.common.RestResult;
import com.boyun.boyunfile.constant.ShareConstant;
import com.boyun.boyunfile.domain.Share;
import com.boyun.boyunfile.domain.ShareFile;
import com.boyun.boyunfile.domain.UserFile;
import com.boyun.boyunfile.dto.*;
import com.boyun.boyunfile.service.ShareFileService;
import com.boyun.boyunfile.service.ShareService;
import com.boyun.boyunfile.util.DateUtil;
import com.boyun.boyunfile.vo.ShareListVO;
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


    @Operation(summary = "获取我的分享文件列表", description = "用来做前台文件列表展示", tags = { "getsharelist" })
    @GetMapping(value = "/getsharelist")
    @ResponseBody
    public RestResult getShareList(ShareListDTO shareListDTO) {

//        LambdaQueryWrapper<Share> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Share::getUserId, shareListDTO.getUserId())
//                .and(shareLambdaQueryWrapper -> shareLambdaQueryWrapper
//                        .eq(Share::getShareStatus, Share.NOMALTIME)
//                        .or().eq(Share::getShareStatus, Share.EXPIREDTIME));
//        List<Share> list = shareService.list(queryWrapper);
//        List<ShareFile> shareList = new ArrayList<>();
//
//        LambdaQueryWrapper<ShareFile> fileQueryWrapper = new LambdaQueryWrapper<>();
//        for (Share share : list) {
//            fileQueryWrapper.clear();
//            fileQueryWrapper.eq(ShareFile::getSharebatchnum, share.getShareBatchnum())
//                    .eq(ShareFile::getSharefilepath, repalcePath(shareListDTO.getFilePath()));
//            List<ShareFile> list1 = shareFileService.list(fileQueryWrapper);
//            shareList.addAll(list1);
//        }
        List<ShareListVO> shareList = shareService.getShareList(shareListDTO.getFilePath(),
                shareListDTO.getUserId(),
                shareListDTO.getCurrentPage(),
                shareListDTO.getPageCount(),
                shareListDTO.getShareBatchnum());

        int total = shareFileService.countShareFile(shareListDTO.getUserId(), shareListDTO.getFilePath(), shareListDTO.getShareBatchnum());

        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", shareList);

        return RestResult.success().data(map);
    }

    @Operation(summary = "其他用户获取某个分享文件列表", description = "用来做前台文件列表展示", tags = { "getsharefilelist" })
    @GetMapping(value = "/getsharefilelist")
    @ResponseBody
    public RestResult getShareFileList(ShareFileListDTO shareFileListDTO) {
        List<ShareListVO> shareFileList = shareService.getShareFileList(shareFileListDTO.getFilePath(), shareFileListDTO.getShareBatchNum());
        Map<String, Object> map = new HashMap<>();
        map.put("total", shareFileList.size());
        map.put("list", shareFileList);
        return RestResult.success().data(map);
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
        share.setShareBatchNum(uuid);
        share.setShareTime(DateUtil.getCurrentTime());
        share.setShareStatus(ShareConstant.NOMALTIME);


        List<UserFile> files = JSONArray.parseArray(shareFileDTO.getFiles(), UserFile.class);

        for (UserFile file : files) {
            shareFileService.shareFile(file.getUserFileId(), shareFileDTO.getUserId(), uuid);
        }

        shareService.save(share);

        map.put("shareBatchNum", uuid);

        return RestResult.success().message("分享成功").data(map);
    }

    @Operation(summary = "校验分享链接过期时间", tags = { "checkendtime" })
    @GetMapping(value = "/checkendtime")
    @ResponseBody
    public RestResult checkEndTime(String shareBatchNum) {

        QueryWrapper<Share> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shareBatchNum", shareBatchNum);
        queryWrapper.select("shareStatus");
        Share share = shareService.getOne(queryWrapper);

        Map<String, Integer> map = new HashMap<>();
        map.put("shareStatus", share.getShareStatus());

        return RestResult.success().data(map);
    }

    @Operation(summary = "校验分享类型", tags = { "checksharetype" })
    @GetMapping(value = "/checksharetype")
    @ResponseBody
    public RestResult checkShareType(String shareBatchNum) {
        QueryWrapper<Share> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shareBatchNum", shareBatchNum);
        queryWrapper.select("shareType");
        Share share = shareService.getOne(queryWrapper);

        Map<String, Integer> map = new HashMap<>();
        map.put("shareType", share.getShareType());

        return RestResult.success().data(map);
    }

    @Operation(summary = "校验分享文件提取码", description = "校验分享文件提取码", tags = { "checksharecode" })
    @GetMapping(value = "/checksharecode")
    @ResponseBody
    public RestResult checkShareCode(CheckShareCodeDTO checkShareCodeDTO) {

        LambdaQueryWrapper<Share> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Share::getShareBatchNum, checkShareCodeDTO.getShareBatchNum())
                .eq(Share::getShareStatus, Share.NOMALTIME);
        Share share = shareService.getOne(queryWrapper);

        if(share.getExtractionCode().equals(checkShareCodeDTO.getExtractionCode())){
            return RestResult.success();
        }else{
            return RestResult.fail().message("提取码错误");
        }
    }

    @Operation(summary = "取消分享文件", description = "取消分享文件", tags = { "deleteshare" })
    @RequestMapping(value = "/deleteshare", method = RequestMethod.POST)
    @ResponseBody
    public RestResult deleteShare(@RequestBody DeleteShareDTO deleteShareDTO) {

        shareFileService.deleteShare(deleteShareDTO.getUserFileId(), deleteShareDTO.getShareFileId(), deleteShareDTO.getShareBatchNum());

        return RestResult.success().message("取消成功");
    }

    @Operation(summary = "保存分享文件到我的网盘", description = "保存分享文件到我的网盘", tags = { "savesharefile" })
    @RequestMapping(value = "/savesharefile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult saveShareFile() {
        //todo
        return null;
    }

    public String repalcePath(String path){

        String filePath = path;
        filePath = filePath.replace("\\", "\\\\\\\\");
        filePath = filePath.replace("'", "\\'");
        filePath = filePath.replace("%", "\\%");
        filePath = filePath.replace("_", "\\_");

        return filePath;

    }
}
