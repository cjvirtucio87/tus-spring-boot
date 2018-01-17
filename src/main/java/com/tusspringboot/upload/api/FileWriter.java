package com.tusspringboot.upload.api;

import java.io.IOException;
import java.util.List;

public interface FileWriter {
    public String createDirectory(String fileName) throws IOException;

    public FileInfo write(FileInfo partInfo);

    public Long concat(List<FileInfo> fileInfoList) throws IOException;
}
