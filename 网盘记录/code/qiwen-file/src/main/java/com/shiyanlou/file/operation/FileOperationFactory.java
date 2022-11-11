package com.shiyanlou.file.operation;

import com.shiyanlou.file.operation.delete.Deleter;
import com.shiyanlou.file.operation.download.Downloader;
import com.shiyanlou.file.operation.upload.Uploader;

public interface FileOperationFactory {
    Uploader getUploader();
    Downloader getDownloader();
    Deleter getDeleter();
}