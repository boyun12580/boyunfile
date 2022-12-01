package com.boyun.boyunfile.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyun.boyunfile.domain.Share;
import com.boyun.boyunfile.vo.ShareListVO;

import java.util.List;

public interface ShareService extends IService<Share> {
    List<ShareListVO> getShareList(String filePath, Long userId, Long currentPage, Long pageCount, String shareBatchnum);
    List<ShareListVO> getShareFileList(String filePath, String shareBatchnum);
}
