package com.boyun.boyunfile.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyun.boyunfile.domain.ShareFile;
import com.boyun.boyunfile.mapper.ShareFileMapper;
import com.boyun.boyunfile.service.ShareFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ShareFileServiceImpl extends ServiceImpl<ShareFileMapper, ShareFile> implements ShareFileService {
}
