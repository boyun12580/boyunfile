package com.boyun.boyunfile.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyun.boyunfile.domain.ShareFile;
import com.boyun.boyunfile.domain.UserFile;
import org.springframework.data.relational.core.sql.In;

import java.util.List;

public interface ShareFileService extends IService<ShareFile> {
    List<UserFile> selectUserFileTreeListLikeFilePath(String filePath, long userId);
    void shareFile(Long userFileId, Long userId, String shareBatchnum);
    Integer countShareFile(Long userId, String filePath, String shareBatchnum);
    void deleteShare(Long userFileId, Long shareFileId, String shareBatchNum);
    List<ShareFile> selectShareFileTreeListLikeFilePath(String filePath, String shareBatchNum);
}
