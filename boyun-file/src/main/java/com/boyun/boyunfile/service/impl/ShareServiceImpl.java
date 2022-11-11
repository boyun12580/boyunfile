package com.boyun.boyunfile.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyun.boyunfile.domain.Share;
import com.boyun.boyunfile.mapper.ShareMapper;
import com.boyun.boyunfile.service.ShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ShareServiceImpl extends ServiceImpl<ShareMapper, Share> implements ShareService {
}
