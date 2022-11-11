package com.shiyanlou.file.exception;

public class NotSameFileExpection extends Exception {
    public NotSameFileExpection() {
        super("File MD5 Different");
    }
}