package com.shiyanlou.file.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shiyanlou.file.dto.DownloadFileDTO;
import com.shiyanlou.file.dto.UploadFileDTO;

public interface FiletransferService {
    void uploadFile(HttpServletRequest request, UploadFileDTO uploadFileDto, Long userId);
    void downloadFile(HttpServletResponse httpServletResponse, DownloadFileDTO downloadFileDTO);
    Long selectStorageSizeByUserId(Long userId);
}