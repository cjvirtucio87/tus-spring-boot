package com.tusspringboot.upload.impl;

import com.tusspringboot.upload.api.UploadService;
import com.tusspringboot.upload.data.PartInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by cjvirtucio on 5/27/17.
 */
@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    private UploadFileReader uploadFileReader;

    private UploadFileWriter uploadFileWriter;
    
    @Autowired
    public UploadServiceImpl(
    		UploadFileReader uploadFileReader,
    		UploadFileWriter uploadFileWriter
    		) {
    		this.uploadFileReader = uploadFileReader;
    		this.uploadFileWriter = uploadFileWriter;
    }

    public List<Long> getCurrentOffsets(String fileName, List<PartInfo> partInfoList) throws IOException {
        if (!uploadFileReader.fileExists(fileName)) {
            throw new IOException("No directory for file, " + fileName);
        }

        return partInfoList.stream()
                .map(uploadFileReader::getCurrentOffset)
                .collect(Collectors.toList());
    }

    public String getDirectoryPath(String fileName) throws IOException {
        return uploadFileWriter.createDirectory(fileName);
    }

    public PartInfo write(PartInfo partInfo) throws IOException {
        return Optional.of(partInfo)
                .map(uploadFileWriter::writeFilePart)
                .filter(uploadFileReader::isComplete)
                .orElseThrow(this::onIncomplete);
    }

    public Long concat(List<PartInfo> partInfoList) throws IOException {
        return uploadFileWriter.concat(partInfoList);
    }

    private IOException onIncomplete() {
        return new IOException("Upload incomplete.");
    }
}
