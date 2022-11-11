package com.boyun.boyunfile.operation.download;

import com.boyun.boyunfile.operation.download.domain.DownloadFile;

import javax.servlet.http.HttpServletResponse;


public abstract class Downloader {
    public abstract void download(HttpServletResponse httpServletResponse, DownloadFile uploadFile);
}