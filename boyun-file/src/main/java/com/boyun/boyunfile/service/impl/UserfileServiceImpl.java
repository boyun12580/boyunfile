package com.boyun.boyunfile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.boyun.boyunfile.constant.FileConstant;
import com.boyun.boyunfile.domain.File;
import com.boyun.boyunfile.domain.RecoveryFile;
import com.boyun.boyunfile.domain.UserFile;
import com.boyun.boyunfile.mapper.FileMapper;
import com.boyun.boyunfile.mapper.RecoveryFileMapper;
import com.boyun.boyunfile.mapper.UserfileMapper;
import com.boyun.boyunfile.service.UserfileService;
import com.boyun.boyunfile.util.DateUtil;
import com.boyun.boyunfile.vo.UserfileListVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class UserfileServiceImpl extends ServiceImpl<UserfileMapper, UserFile> implements UserfileService {

    @Resource
    UserfileMapper userfileMapper;

    @Resource
    FileMapper fileMapper;

    @Resource
    RecoveryFileMapper recoveryFileMapper;

    public static Executor executor = Executors.newFixedThreadPool(20);

    @Override
    public List<UserfileListVO> getUserFileByFilePath(String filePath, Long userId, Long currentPage, Long pageCount) {
        Long beginCount = (currentPage - 1) * pageCount;
        UserFile userfile = new UserFile();
        userfile.setUserId(userId);
        userfile.setFilePath(filePath);
        List<UserfileListVO> fileList = userfileMapper.userfileList(userfile, beginCount, pageCount);
        return fileList;
    }

    @Override
    public Map<String, Object> getUserFileByType(int fileType, Long currentPage, Long pageCount, Long userId) {
        Long beginCount = (currentPage - 1) * pageCount;
        List<UserfileListVO> fileList;
        Long total;
        if (fileType == FileConstant.OTHER_TYPE) {

            List<String> arrList = new ArrayList<>();
            arrList.addAll(Arrays.asList(FileConstant.DOC_FILE));
            arrList.addAll(Arrays.asList(FileConstant.IMG_FILE));
            arrList.addAll(Arrays.asList(FileConstant.VIDEO_FILE));
            arrList.addAll(Arrays.asList(FileConstant.MUSIC_FILE));

            fileList = userfileMapper.selectFileNotInExtendNames(arrList,beginCount, pageCount, userId);
            total = userfileMapper.selectCountNotInExtendNames(arrList,beginCount, pageCount, userId);
        } else {
            List<String> fileExtends = null;
            if (fileType == FileConstant.IMAGE_TYPE) {
                fileExtends = Arrays.asList(FileConstant.IMG_FILE);
            } else if (fileType == FileConstant.DOC_TYPE) {
                fileExtends = Arrays.asList(FileConstant.DOC_FILE);
            } else if (fileType == FileConstant.VIDEO_TYPE) {
                fileExtends = Arrays.asList(FileConstant.VIDEO_FILE);
            } else if (fileType == FileConstant.MUSIC_TYPE) {
                fileExtends = Arrays.asList(FileConstant.MUSIC_FILE);
            }
            fileList = userfileMapper.selectFileByExtendName(fileExtends, beginCount, pageCount,userId);
            total = userfileMapper.selectCountByExtendName(fileExtends, beginCount, pageCount,userId);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list",fileList);
        map.put("total", total);
        return map;
    }

    @Override
    public void deleteUserFile(Long userFileId, Long sessionUserId) {
        UserFile userFile = userfileMapper.selectById(userFileId);
//        System.out.println(userFile);
        String uuid = UUID.randomUUID().toString();
        if (userFile.getIsDir() == 1) {
            LambdaUpdateWrapper<UserFile> userFileLambdaUpdateWrapper = new LambdaUpdateWrapper<UserFile>();
            userFileLambdaUpdateWrapper.set(UserFile::getDeleteFlag, 1)
                    .set(UserFile::getDeleteBatchNum, uuid)
                    .set(UserFile::getDeleteTime, DateUtil.getCurrentTime())
                    .eq(UserFile::getUserFileId, userFileId);
            userfileMapper.update(null, userFileLambdaUpdateWrapper);

            String filePath = userFile.getFilePath() + userFile.getFileName() + "/";
            updateFileDeleteStateByFilePath(filePath, uuid, sessionUserId);

        }else{

            UserFile userFileTemp = userfileMapper.selectById(userFileId);
            File file = fileMapper.selectById(userFileTemp.getFileId());

            LambdaUpdateWrapper<UserFile> userFileLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            userFileLambdaUpdateWrapper.set(UserFile::getDeleteFlag, 1)
                    .set(UserFile::getDeleteTime, DateUtil.getCurrentTime())
                    .set(UserFile::getDeleteBatchNum, uuid)
                    .eq(UserFile::getUserFileId, userFileTemp.getUserFileId());
            userfileMapper.update(null, userFileLambdaUpdateWrapper);

        }

        RecoveryFile recoveryFile = new RecoveryFile();
        recoveryFile.setUserFileId(userFileId);
        recoveryFile.setDeleteTime(DateUtil.getCurrentTime());
        recoveryFile.setDeleteBatchNum(uuid);
        recoveryFile.setUserId(userFile.getUserId());
        recoveryFileMapper.insert(recoveryFile);
    }

    @Override
    public List<UserFile> selectFileTreeListLikeFilePath(String filePath, long userId) {
        //UserFile userFile = new UserFile();
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

    @Override
    public List<UserFile> selectFilePathTreeByUserId(Long userId) {
        LambdaQueryWrapper<UserFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserFile::getUserId, userId)
                .eq(UserFile::getIsDir, 1)
                .eq(UserFile::getDeleteFlag, 0);
        return userfileMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public void updateFilepathByFilepath(String oldfilePath, String newfilePath, String fileName, String extendName, Long userId) {
        if ("null".equals(extendName)){
            extendName = null;
        }

        LambdaUpdateWrapper<UserFile> lambdaUpdateWrapper = new LambdaUpdateWrapper<UserFile>();
        lambdaUpdateWrapper.set(UserFile::getFilePath, newfilePath)
                .eq(UserFile::getFilePath, oldfilePath)
                .eq(UserFile::getFileName, fileName)
                .eq(UserFile::getUserId, userId);
        if (StringUtils.isNotEmpty(extendName)) {
            lambdaUpdateWrapper.eq(UserFile::getExtendName, extendName);
        } else {
            lambdaUpdateWrapper.isNull(UserFile::getExtendName);
        }
        userfileMapper.update(null, lambdaUpdateWrapper);
        //移动子目录
        oldfilePath = oldfilePath + fileName + "/";
        newfilePath = newfilePath + fileName + "/";

        oldfilePath = oldfilePath.replace("\\", "\\\\\\\\");
        oldfilePath = oldfilePath.replace("'", "\\'");
        oldfilePath = oldfilePath.replace("%", "\\%");
        oldfilePath = oldfilePath.replace("_", "\\_");

        if (extendName == null) { //为null说明是目录，则需要移动子目录
            userfileMapper.updateFilepathByFilepath(oldfilePath, newfilePath, userId);
        }
    }

    @Override
    public List<UserFile> selectUserFileByNameAndPath(String fileName, String filePath, Long userId) {
        LambdaQueryWrapper<UserFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserFile::getFileName, fileName)
                .eq(UserFile::getFilePath, filePath)
                .eq(UserFile::getUserId, userId)
                .eq(UserFile::getDeleteFlag, "0");
        return userfileMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public void replaceUserFilePath(String filePath, String oldFilePath, Long userId) {
        userfileMapper.replaceFilePath(filePath, oldFilePath, userId);
    }

    //    删除目录时需要将该文件目录下的所有文件都放入回收站，而代码实现则是通过一个删除标志来实现，
//    为了防止文件目录下文件特别多，因此这里需要创建一个新的线程去执行，防止出现阻塞
    private void updateFileDeleteStateByFilePath(String filePath, String deleteBatchNum, Long userId) {
        new Thread(()->{
            List<UserFile> fileList = selectFileTreeListLikeFilePath(filePath, userId);
            for (int i = 0; i < fileList.size(); i++){
                UserFile userFileTemp = fileList.get(i);
                executor.execute(() -> {
                    //标记删除标志
                    LambdaUpdateWrapper<UserFile> userFileLambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
                    userFileLambdaUpdateWrapper1.set(UserFile::getDeleteFlag, 1)
                            .set(UserFile::getDeleteTime, DateUtil.getCurrentTime())
                            .set(UserFile::getDeleteBatchNum, deleteBatchNum)
                            .eq(UserFile::getUserFileId, userFileTemp.getUserFileId());
                    userfileMapper.update(null, userFileLambdaUpdateWrapper1);
                });

            }
        }).start();
    }
}