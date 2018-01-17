package com.tusspringboot.upload.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tusspringboot.upload.api.FileInfo;
import com.tusspringboot.upload.api.FileReader;
import com.tusspringboot.upload.api.UploadService;

/**
 * Created by cjvirtucio on 5/27/17.
 */
@Service
public class UploadServiceImpl implements UploadService {

    private FileReader uploadFileReader;

    private UploadFileWriter uploadFileWriter;
    
    @Autowired
    public UploadServiceImpl(
    		UploadFileReader uploadFileReader,
    		UploadFileWriter uploadFileWriter
    		) {
    		this.uploadFileReader = uploadFileReader;
    		this.uploadFileWriter = uploadFileWriter;
    }

    public List<Long> getCurrentOffsets(String fileName, List<FileInfo> partInfoList) throws IOException {
        if (!uploadFileReader.fileExists(fileName)) {
            throw new IOException("No directory for file, " + fileName);
        }

        return partInfoList.stream()
                .map(uploadFileReader::getOffset)
                .collect(Collectors.toList());
    }

    public String getDirectoryPath(String fileName) throws IOException {
        return uploadFileWriter.createDirectory(fileName);
    }

    public PartInfo write(FileInfo partInfo) throws IOException {
        return Optional.of(partInfo)
                .map(uploadFileWriter::write)
                .filter(uploadFileReader::isComplete)
                .orElseThrow(this::onIncomplete);
    }

    public Long concat(List<FileInfo> partInfoList) throws IOException {
        return uploadFileWriter.concat(partInfoList);
    }

    private IOException onIncomplete() {
        return new IOException("Upload incomplete.");
    }
}
