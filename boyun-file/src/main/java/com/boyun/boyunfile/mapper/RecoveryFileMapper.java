package com.boyun.boyunfile.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boyun.boyunfile.domain.RecoveryFile;
import com.boyun.boyunfile.domain.UserFile;
import com.boyun.boyunfile.vo.RecoveryFileListVO;

public interface RecoveryFileMapper extends BaseMapper<RecoveryFile> {
//    List<RecoveryFileListVO> selectRecoveryFileList();

    List<RecoveryFileListVO> getRecoveryFileList(Long userId);
}