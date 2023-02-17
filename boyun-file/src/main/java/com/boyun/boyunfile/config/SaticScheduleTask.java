package com.boyun.boyunfile.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boyun.boyunfile.constant.ShareConstant;
import com.boyun.boyunfile.domain.Share;
import com.boyun.boyunfile.service.ShareService;
import com.boyun.boyunfile.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
public class SaticScheduleTask {

    @Autowired
    private ShareService shareService;

//    每天0点1分1秒触发任务
    @Scheduled(cron = "1 1 0 * * ?")
    private void shareTimeTasks() throws ParseException {
        QueryWrapper<Share> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("endTime", "shareId","shareStatus");
        queryWrapper.eq("shareStatus", ShareConstant.NOMALTIME);
        List<Share> list = shareService.list(queryWrapper);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        long dateCompare = new Date().getTime();
        log.info("执行定时任务->当前时为：" + new Date());
        for (Share share : list) {
            String endTime = share.getEndTime();
            Date date = df.parse(endTime);
            if(date.getTime() < dateCompare){
                share.setShareStatus(ShareConstant.EXPIREDTIME);
                shareService.updateById(share);
            }
        }
    }

}
