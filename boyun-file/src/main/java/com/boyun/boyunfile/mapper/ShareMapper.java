package com.boyun.boyunfile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boyun.boyunfile.domain.Share;
import com.boyun.boyunfile.vo.ShareListVO;

import java.util.List;

public interface ShareMapper extends BaseMapper<Share> {

    List<ShareListVO> getShareList(String filePath, Long userId, Long beginCount, Long pageCount);

}
