package com.boyun.boyunfile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyun.boyunfile.domain.RecoveryFile;
import com.boyun.boyunfile.domain.UserFile;
import com.boyun.boyunfile.mapper.RecoveryFileMapper;
import com.boyun.boyunfile.mapper.UserfileMapper;
import com.boyun.boyunfile.service.RecoveryFileService;
import com.boyun.boyunfile.vo.RecoveryFileListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class RecoveryFileServiceImpl extends ServiceImpl<RecoveryFileMapper, RecoveryFile> implements RecoveryFileService {

    @Resource
    private UserfileMapper userfileMapper;

    @Resource
    private RecoveryFileMapper recoveryFileMapper;

    @Override
    public void recoveryFile(Long recoveryFileId) {
        RecoveryFile recoveryFile = recoveryFileMapper.selectById(recoveryFileId);

        List<UserFile> userFileList = userfileMapper.selectRecoveryFile(recoveryFile.getDeleteBatchNum());

        for (UserFile userFiletemp : userFileList) {
            UserFile userFile = new UserFile();
            userFile.setExtendName(userFiletemp.getExtendName());
            userFile.setFileId(userFiletemp.getFileId());

            //对重复文件名进行增加后缀处理
            StringBuffer fileName = new StringBuffer(userFiletemp.getFileName().toString());
            int temp = 0;
            StringBuilder originFileName = new StringBuilder(fileName.toString());
            LambdaQueryWrapper<UserFile> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserFile::getFileName, fileName.toString())
                    .eq(UserFile::getFilePath, userFiletemp.getFilePath());
            List<UserFile> files = userfileMapper.selectList(wrapper);
            while(files != null && files.size() > 0){
                fileName = new StringBuffer(originFileName.toString());
                fileName.append('(');
                fileName.append(temp++);
                fileName.append(')');
                wrapper = new LambdaQueryWrapper<UserFile>()
                        .eq(UserFile::getFileName, fileName.toString())
                        .eq(UserFile::getFilePath, userFiletemp.getFilePath());
                files = userfileMapper.selectList(wrapper);
            }


            userFile.setFileName(fileName.toString());
            userFile.setFilePath(userFiletemp.getFilePath());
            userFile.setIsDir(userFiletemp.getIsDir());
            userFile.setUploadTime(userFiletemp.getUploadTime());
            userFile.setUserId(userFiletemp.getUserId());
            userfileMapper.insert(userFile);
        }


        recoveryFileMapper.deleteById(recoveryFileId);

    }

    @Override
    public List<RecoveryFileListVO> getRecoveryFileList(Long userId) {
        return recoveryFileMapper.getRecoveryFileList(userId);
    }
}