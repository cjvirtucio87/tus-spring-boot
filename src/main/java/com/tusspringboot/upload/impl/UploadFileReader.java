package com.tusspringboot.upload.impl;

import com.tusspringboot.upload.data.PartInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by cjvirtucio on 5/29/17.
 */
@Service
@Slf4j
public class UploadFileReader {

    public boolean fileExists(String fileName) {
        return UploadPathFactory.createDirectoryPath(fileName).toFile().exists();
    }

    public boolean isComplete(PartInfo partInfo) {
        return partInfo.getUploadOffset().equals(partInfo.getUploadLength());
    }

    public Long getCurrentOffset(PartInfo partInfo) {
        String filePath = UploadPathFactory.createPartPath(partInfo).toString();
        log.info("Retrieving pointer for file part, " + filePath);
        Long currentOffset = 0L;

        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw");) {
            currentOffset = raf.length();
        } catch (IOException e) {
            log.error("Error attempting to get offset for file, " + filePath);
            throw new RuntimeException(e);
        }

        return currentOffset;
    }
}
