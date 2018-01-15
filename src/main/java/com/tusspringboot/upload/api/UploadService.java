package com.tusspringboot.upload.api;

import com.tusspringboot.upload.data.PartInfo;

import java.io.IOException;
import java.util.List;

public interface UploadService {
  List<Long> getCurrentOffsets(String fileName, List<PartInfo> partInfoList) throws IOException;

  String getDirectoryPath(String fileName) throws IOException;

  PartInfo write(PartInfo partInfo) throws IOException;

  Long concat(List<PartInfo> partInfoList) throws IOException;
}
