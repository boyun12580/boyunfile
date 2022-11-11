package com.boyun.boyunfile.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyun.boyunfile.domain.File;
import com.boyun.boyunfile.mapper.FileMapper;
import com.boyun.boyunfile.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {
}
