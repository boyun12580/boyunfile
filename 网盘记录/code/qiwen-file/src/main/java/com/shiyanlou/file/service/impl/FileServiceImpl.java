package com.shiyanlou.file.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyanlou.file.mapper.FileMapper;
import com.shiyanlou.file.model.File;
import com.shiyanlou.file.service.FileService;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

}