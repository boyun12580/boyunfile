package com.boyun.boyunfile.factory;


import com.boyun.boyunfile.operation.delete.Deleter;
import com.boyun.boyunfile.operation.download.Downloader;
import com.boyun.boyunfile.operation.upload.Uploader;

public interface FileOperationFactory {
    Uploader getUploader();
    Downloader getDownloader();
    Deleter getDeleter();
}