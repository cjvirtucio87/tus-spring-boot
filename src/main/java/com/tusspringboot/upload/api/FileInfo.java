package com.tusspringboot.upload.api;

import java.io.InputStream;

public interface FileInfo {
    public String getFileName();
    
    public String getFileType();
    
    public String getFileExt();

    public Long getOffset();
    
    public Long getLength();

    public Long getFileSize();

    public String getUserName();

    public InputStream getInputStream();
}
