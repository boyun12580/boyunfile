package com.boyun.boyunfile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyun.boyunfile.domain.Share;
import com.boyun.boyunfile.domain.ShareFile;
import com.boyun.boyunfile.domain.UserFile;
import com.boyun.boyunfile.mapper.ShareFileMapper;
import com.boyun.boyunfile.mapper.ShareMapper;
import com.boyun.boyunfile.mapper.UserfileMapper;
import com.boyun.boyunfile.service.ShareFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@Service
public class ShareFileServiceImpl extends ServiceImpl<ShareFileMapper, ShareFile> implements ShareFileService {

    @Resource
    private ShareFileMapper shareFileMapper;

    @Resource
    private ShareMapper shareMapper;

    @Resource
    private UserfileMapper userfileMapper;

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 40, 10, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(4));

    @Override
    public void shareFile(Long userFileId, Long userId, String shareBatchNum) {

        UserFile userFile = userfileMapper.selectById(userFileId);

        if (userFile.getIsDir() == 1) {
            String filePath = userFile.getFilePath() + userFile.getFileName() + "/";
            shareFileByFilePath(filePath, shareBatchNum, userId, userFile.getFilePath());
        }

        ShareFile shareFile = new ShareFile();
        shareFile.setShareBatchNum(shareBatchNum);
        shareFile.setShareFilePath("/");
        shareFile.setUserFileId(userFile.getUserFileId());
        shareFileMapper.insert(shareFile);


    }

    @Override
    public Integer countShareFile(Long userId, String filePath, String shareBatchnum) {
        return shareMapper.countShareFile(userId, filePath, shareBatchnum);
    }

    @Override
    public void deleteShare(Long userFileId, Long shareFileId, String shareBatchNum) {
        UserFile userFile = userfileMapper.selectById(userFileId);
        ShareFile shareFile = shareFileMapper.selectById(shareFileId);

        if (userFile.getIsDir() == 1) {
            String filePath = shareFile.getShareFilePath() + userFile.getFileName() + "/";
            deleteShareByFilePath(filePath, shareBatchNum);
        }

        shareFileMapper.deleteById(shareFileId);

    }

    @Override
    public List<ShareFile> selectShareFileTreeListLikeFilePath(String filePath, String shareBatchNum) {
        filePath = filePath.replace("\\", "\\\\\\\\");
        filePath = filePath.replace("'", "\\'");
        filePath = filePath.replace("%", "\\%");
        filePath = filePath.replace("_", "\\_");

        LambdaQueryWrapper<ShareFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShareFile::getShareBatchNum, shareBatchNum).likeRight(ShareFile::getShareFilePath, filePath);
        return shareFileMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public List<UserFile> selectUserFileTreeListLikeFilePath(String filePath, long userId) {
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

//    开多线程来分享目录以下的子目录或文件
    private void shareFileByFilePath(String shareFilePath, String shareBatchnum, Long userId, String originFilePath) {
        executor.execute(()->{
            List<UserFile> fileList = selectUserFileTreeListLikeFilePath(shareFilePath, userId);
            for (int i = 0; i < fileList.size(); i++){
                UserFile userFileTemp = fileList.get(i);
                executor.execute(() -> {
                    //标记分享
                    ShareFile shareFile = new ShareFile();
                    shareFile.setShareBatchNum(shareBatchnum);
                    shareFile.setShareFilePath(userFileTemp.getFilePath().replaceFirst(originFilePath,"/"));
                    shareFile.setUserFileId(userFileTemp.getUserFileId());
                    shareFileMapper.insert(shareFile);

                });

            }
        });
    }

    private void deleteShareByFilePath(String shareFilePath, String shareBatchnum) {
        executor.execute(()->{
            List<ShareFile> fileList = selectShareFileTreeListLikeFilePath(shareFilePath, shareBatchnum);
            for (int i = 0; i < fileList.size(); i++){
                ShareFile shareFileTemp = fileList.get(i);
                executor.execute(() -> {
                    //标记分享
                    shareFileMapper.deleteById(shareFileTemp.getShareFileId());
                });

            }
        });
    }
}
