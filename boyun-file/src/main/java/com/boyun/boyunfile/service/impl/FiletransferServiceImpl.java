package com.boyun.boyunfile.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boyun.boyunfile.domain.File;
import com.boyun.boyunfile.domain.UserFile;
import com.boyun.boyunfile.dto.DownloadFileDTO;
import com.boyun.boyunfile.dto.UploadFileDTO;
import com.boyun.boyunfile.factory.FileOperationFactory;
import com.boyun.boyunfile.mapper.FileMapper;
import com.boyun.boyunfile.mapper.UserfileMapper;
import com.boyun.boyunfile.operation.download.Downloader;
import com.boyun.boyunfile.operation.download.domain.DownloadFile;
import com.boyun.boyunfile.operation.upload.Uploader;
import com.boyun.boyunfile.operation.upload.domain.UploadFile;
import com.boyun.boyunfile.service.FiletransferService;
import com.boyun.boyunfile.util.DateUtil;
import com.boyun.boyunfile.util.PropertiesUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FiletransferServiceImpl implements FiletransferService {

    @Resource
    private FileMapper fileMapper;
    @Resource
    private UserfileMapper userfileMapper;

    @Resource
    private FileOperationFactory localStorageOperationFactory;

    @Override
    @Transactional
    public void uploadFile(HttpServletRequest request, UploadFileDTO uploadFileDto, Long userId) {

        Uploader uploader = null;
        UploadFile uploadFile = new UploadFile();
        uploadFile.setChunkNumber(uploadFileDto.getChunkNumber());
        uploadFile.setChunkSize(uploadFileDto.getChunkSize());
        uploadFile.setTotalChunks(uploadFileDto.getTotalChunks());
        uploadFile.setIdentifier(uploadFileDto.getIdentifier());
        uploadFile.setTotalSize(uploadFileDto.getTotalSize());
        uploadFile.setCurrentChunkSize(uploadFileDto.getCurrentChunkSize());
        String storageType = PropertiesUtil.getProperty("file.storage-type");
        synchronized (FiletransferService.class) {
            if ("0".equals(storageType)) {
                uploader = localStorageOperationFactory.getUploader();
            }
        }

        List<UploadFile> uploadFileList = uploader.upload(request, uploadFile);
        for (int i = 0; i < uploadFileList.size(); i++){
            uploadFile = uploadFileList.get(i);
            File file = new File();

            file.setIdentifier(uploadFileDto.getIdentifier());
            file.setStorageType(Integer.parseInt(storageType));
            file.setTimeStampName(uploadFile.getTimeStampName());
            if (uploadFile.getSuccess() == 1){
                file.setFileUrl(uploadFile.getUrl());
                file.setFileSize(uploadFile.getFileSize());
                file.setPointCount(1);
                fileMapper.insert(file);
                UserFile userFile = new UserFile();
                userFile.setFileId(file.getFileId());
                userFile.setExtendName(uploadFile.getFileType());
                userFile.setFileName(uploadFile.getFileName());
                userFile.setFilePath(uploadFileDto.getFilePath());
                userFile.setDeleteFlag(0);
                userFile.setUserId(userId);
                userFile.setIsDir(0);
                userFile.setUploadTime(DateUtil.getCurrentTime());
                userfileMapper.insert(userFile);
            }

        }
    }

    @Override
    public void downloadFile(HttpServletResponse httpServletResponse, DownloadFileDTO downloadFileDTO) {
        UserFile userFile = userfileMapper.selectById(downloadFileDTO.getUserFileId());

        String fileName = userFile.getFileName() + "." + userFile.getExtendName();
        try {
            fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpServletResponse.setContentType("application/force-download");// 设置强制下载不打开
        httpServletResponse.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名


        File file = fileMapper.selectById(userFile.getFileId());
        Downloader downloader = null;
        if (file.getStorageType() == 0) {
            downloader = localStorageOperationFactory.getDownloader();
        }
        DownloadFile uploadFile = new DownloadFile();
        uploadFile.setFileUrl(file.getFileUrl());
        uploadFile.setTimeStampName(file.getTimeStampName());
        downloader.download(httpServletResponse, uploadFile);
    }

    @Override
    public Long selectStorageSizeByUserId(Long userId) {
        return userfileMapper.selectStorageSizeByUserId(userId);
    }
}