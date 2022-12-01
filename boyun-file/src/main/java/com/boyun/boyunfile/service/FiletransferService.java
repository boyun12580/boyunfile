package com.boyun.boyunfile.service;

import com.boyun.boyunfile.dto.DownloadFileDTO;
import com.boyun.boyunfile.dto.PreviewDTO;
import com.boyun.boyunfile.dto.UploadFileDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public interface FiletransferService {
    void uploadFile(HttpServletRequest request, UploadFileDTO uploadFileDto, Long userId);
    void downloadFile(HttpServletResponse httpServletResponse, DownloadFileDTO downloadFileDTO);
    Long selectStorageSizeByUserId(Long userId);
    void previewPictureFile(HttpServletResponse httpServletResponse, PreviewDTO previewDTO);
}