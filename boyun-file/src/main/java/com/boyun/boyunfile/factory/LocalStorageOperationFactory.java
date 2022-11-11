package com.boyun.boyunfile.factory;

import javax.annotation.Resource;


import com.boyun.boyunfile.operation.delete.Deleter;
import com.boyun.boyunfile.operation.delete.product.LocalStorageDeleter;
import com.boyun.boyunfile.operation.download.Downloader;
import com.boyun.boyunfile.operation.download.product.LocalStorageDownloader;
import com.boyun.boyunfile.operation.upload.Uploader;
import com.boyun.boyunfile.operation.upload.product.LocalStorageUploader;
import org.springframework.stereotype.Component;

@Component
public class LocalStorageOperationFactory implements FileOperationFactory{

    @Resource
    LocalStorageUploader localStorageUploader;
    @Resource
    LocalStorageDownloader localStorageDownloader;
    @Resource
    LocalStorageDeleter localStorageDeleter;
    @Override
    public Uploader getUploader() {
        return localStorageUploader;
    }

    @Override
    public Downloader getDownloader() {
        return localStorageDownloader;
    }

    @Override
    public Deleter getDeleter() {
        return localStorageDeleter;
    }


}