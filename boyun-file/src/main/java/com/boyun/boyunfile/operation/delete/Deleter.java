package com.boyun.boyunfile.operation.delete;

import com.boyun.boyunfile.operation.delete.domain.DeleteFile;

public abstract class Deleter {
    public abstract void delete(DeleteFile deleteFile);
}