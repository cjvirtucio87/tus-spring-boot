package com.tusspringboot.upload.api;

import java.io.IOException;
import java.util.List;

import com.tusspringboot.upload.impl.PartInfo;

public interface UploadService {
  List<Long> getCurrentOffsets(String fileName, List<FileInfo> partInfoList) throws IOException;

  String getDirectoryPath(String fileName) throws IOException;

  PartInfo write(FileInfo partInfo) throws IOException;

  Long concat(List<FileInfo> partInfoList) throws IOException;
}
