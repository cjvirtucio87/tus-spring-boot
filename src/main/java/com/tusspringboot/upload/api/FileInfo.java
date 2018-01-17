package com.tusspringboot.upload.api;

import java.io.InputStream;

public interface FileInfo {
    public String getFileName();

    public Long getOffset();
    
    public Long getLength();

    public Long getFileSize();

    public String getUserName();

    public InputStream getInputStream();
}
