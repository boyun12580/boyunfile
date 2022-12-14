package com.boyun.boyunfile.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyun.boyunfile.domain.RecoveryFile;
import com.boyun.boyunfile.domain.UserFile;
import com.boyun.boyunfile.vo.RecoveryFileListVO;

import java.util.List;


public interface RecoveryFileService extends IService<RecoveryFile> {

    void recoveryFile(Long recoveryFileId);
    List<RecoveryFileListVO> getRecoveryFileList(Long userId);
}