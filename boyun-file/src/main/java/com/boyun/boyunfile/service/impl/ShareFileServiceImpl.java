package com.boyun.boyunfile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyun.boyunfile.domain.File;
import com.boyun.boyunfile.domain.RecoveryFile;
import com.boyun.boyunfile.domain.ShareFile;
import com.boyun.boyunfile.domain.UserFile;
import com.boyun.boyunfile.mapper.ShareFileMapper;
import com.boyun.boyunfile.mapper.ShareMapper;
import com.boyun.boyunfile.mapper.UserfileMapper;
import com.boyun.boyunfile.service.ShareFileService;
import com.boyun.boyunfile.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class ShareFileServiceImpl extends ServiceImpl<ShareFileMapper, ShareFile> implements ShareFileService {

    @Resource
    private ShareFileMapper shareFileMapper;

    @Resource
    private ShareMapper shareMapper;

    @Resource
    private UserfileMapper userfileMapper;

    public static Executor executor = Executors.newFixedThreadPool(20);

    @Override
    public void shareFile(Long userFileId, Long userId, String shareBatchnum) {

        UserFile userFile = userfileMapper.selectById(userFileId);

        if (userFile.getIsDir() == 1) {
            String filePath = userFile.getFilePath() + userFile.getFileName() + "/";
            shareFileByFilePath(filePath, shareBatchnum, userId, userFile.getFilePath());
        }

        ShareFile shareFile = new ShareFile();
        shareFile.setSharebatchnum(shareBatchnum);
        shareFile.setSharefilepath("/");
        shareFile.setUserfileid(userFile.getUserFileId());
        shareFileMapper.insert(shareFile);


    }

    @Override
    public Integer countShareFile(Long userId, String filePath) {
        return shareMapper.countShareFile(userId, filePath);
    }

    @Override
    public List<UserFile> selectShareFileTreeListLikeFilePath(String filePath, long userId) {
        filePath = filePath.replace("\\", "\\\\\\\\");
        filePath = filePath.replace("'", "\\'");
        filePath = filePath.replace("%", "\\%");
        filePath = filePath.replace("_", "\\_");

        //userFile.setFilePath(filePath);

        LambdaQueryWrapper<UserFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        log.info("查询文件路径：" + filePath);

        lambdaQueryWrapper.eq(UserFile::getUserId, userId).likeRight(UserFile::getFilePath, filePath);
        return userfileMapper.selectList(lambdaQueryWrapper);
    }

    private void shareFileByFilePath(String shareFilePath, String shareBatchnum, Long userId, String originFilePath) {
        new Thread(()->{
            List<UserFile> fileList = selectShareFileTreeListLikeFilePath(shareFilePath, userId);
            for (int i = 0; i < fileList.size(); i++){
                UserFile userFileTemp = fileList.get(i);
                executor.execute(() -> {
                    //标记分享
                    ShareFile shareFile = new ShareFile();
                    shareFile.setSharebatchnum(shareBatchnum);
                    shareFile.setSharefilepath(userFileTemp.getFilePath().replaceFirst(originFilePath,"/"));
                    shareFile.setUserfileid(userFileTemp.getUserFileId());
                    shareFileMapper.insert(shareFile);

                });

            }
        }).start();
    }
}
