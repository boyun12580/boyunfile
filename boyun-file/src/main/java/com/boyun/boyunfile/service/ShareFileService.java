package com.boyun.boyunfile.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyun.boyunfile.domain.ShareFile;
import com.boyun.boyunfile.domain.UserFile;

import java.util.List;

public interface ShareFileService extends IService<ShareFile> {
    List<UserFile> selectShareFileTreeListLikeFilePath(String filePath, long userId);
    void shareFile(Long userFileId, Long userId, String shareBatchnum);
}
