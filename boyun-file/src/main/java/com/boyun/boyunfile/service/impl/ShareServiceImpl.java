package com.boyun.boyunfile.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyun.boyunfile.domain.Share;
import com.boyun.boyunfile.mapper.ShareMapper;
import com.boyun.boyunfile.service.ShareService;
import com.boyun.boyunfile.vo.ShareListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class ShareServiceImpl extends ServiceImpl<ShareMapper, Share> implements ShareService {

    @Resource
    private ShareMapper shareMapper;

    @Override
    public List<ShareListVO> getShareList(String filePath, Long userId, Long currentPage, Long pageCount) {

        return shareMapper.getShareList(filePath, userId, (currentPage - 1) * pageCount, pageCount);
    }
}
