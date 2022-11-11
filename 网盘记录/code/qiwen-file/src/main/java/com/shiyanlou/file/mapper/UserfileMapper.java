package com.shiyanlou.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiyanlou.file.model.UserFile;
import java.util.List;
import com.shiyanlou.file.vo.UserfileListVO;

import org.apache.ibatis.annotations.Param;

public interface UserfileMapper extends BaseMapper<UserFile> {

    List<UserfileListVO> userfileList(UserFile userfile, Long beginCount, Long pageCount);
    List<UserfileListVO> selectFileByExtendName(List<String> fileNameList, Long beginCount, Long pageCount, long userId);
    Long selectCountByExtendName(List<String> fileNameList, Long beginCount, Long pageCount, long userId);
    List<UserfileListVO> selectFileNotInExtendNames(List<String> fileNameList, Long beginCount, Long pageCount, long userId);
    Long selectCountNotInExtendNames(List<String> fileNameList, Long beginCount, Long pageCount, long userId);
    void updateFilepathByFilepath(String oldfilePath, String newfilePath, Long userId);
    void replaceFilePath(@Param("filePath") String filePath, @Param("oldFilePath") String oldFilePath, @Param("userId") Long userId);
    Long selectStorageSizeByUserId(@Param("userId") Long userId);
}