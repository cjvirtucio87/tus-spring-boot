package com.tusspringboot.upload.impl;

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
public class UploadService {
    @Autowired
    UploadFileReader uploadFileReader;

    @Autowired
    UploadFileWriter uploadFileWriter;

    public List<Long> mapToCurrentOffsetList(String fileName, List<PartInfo> partInfoList) throws IOException {
        if (!uploadFileReader.checkIfExists(fileName)) {
            throw new IOException("No directory for file, " + fileName);
        }

        return partInfoList.stream()
                .map(uploadFileReader::getCurrentOffset)
                .collect(Collectors.toList());
    }

    public String mapToDirectoryPath(String fileName) throws IOException {
        return uploadFileWriter.createDirectory(fileName);
    }

    public PartInfo mapToPartInfoWrittenBytes(PartInfo partInfo) throws IOException {
        return Optional.of(partInfo)
                .map(uploadFileWriter::writeFilePart)
                .filter(uploadFileReader::checkIfComplete)
                .orElseThrow(this::raiseIfIncomplete);
    }

    public Long reduceToTotalBytesTransferred(List<PartInfo> partInfoList) throws IOException {
        return uploadFileWriter.concatenateFileParts(partInfoList);
    }

    private IOException raiseIfIncomplete() {
        return new IOException("Upload incomplete.");
    }
}
