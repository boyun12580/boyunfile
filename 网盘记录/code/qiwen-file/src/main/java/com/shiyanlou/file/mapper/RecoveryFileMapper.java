package com.shiyanlou.file.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiyanlou.file.model.RecoveryFile;
import com.shiyanlou.file.vo.RecoveryFileListVO;

public interface RecoveryFileMapper extends BaseMapper<RecoveryFile> {
    List<RecoveryFileListVO> selectRecoveryFileList();
}