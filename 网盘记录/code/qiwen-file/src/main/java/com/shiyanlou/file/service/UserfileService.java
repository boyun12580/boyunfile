package com.shiyanlou.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyanlou.file.model.UserFile;
import java.util.List;
import java.util.Map;

import com.shiyanlou.file.vo.UserfileListVO;

public interface UserfileService extends IService<UserFile> {
    List<UserfileListVO> getUserFileByFilePath(String filePath, Long userId, Long currentPage, Long pageCount);
    Map<String, Object> getUserFileByType(int fileType, Long currentPage, Long pageCount, Long userId);
    void deleteUserFile(Long userFileId, Long sessionUserId);
    List<UserFile> selectFileTreeListLikeFilePath(String filePath, long userId);
    List<UserFile> selectFilePathTreeByUserId(Long userId);
    void updateFilepathByFilepath(String oldfilePath, String newfilePath, String fileName, String extendName, Long userId);
    List<UserFile> selectUserFileByNameAndPath(String fileName, String filePath, Long userId);
void replaceUserFilePath(String filePath, String oldFilePath, Long userId);
}