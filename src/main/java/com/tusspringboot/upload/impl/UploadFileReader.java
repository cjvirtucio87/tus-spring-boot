package com.tusspringboot.upload.impl;

import java.io.IOException;
import java.io.RandomAccessFile;

import org.springframework.stereotype.Service;

import com.tusspringboot.upload.api.FileInfo;
import com.tusspringboot.upload.api.FileReader;
import com.tusspringboot.util.PathFactory;


import lombok.extern.slf4j.Slf4j;

/**
 * Created by cjvirtucio on 5/29/17.
 */
@Service
@Slf4j
public class UploadFileReader implements FileReader {

    public boolean fileExists(String fileName) {
        return PathFactory.createDirectoryPath(fileName).toFile().exists();
    }

    public boolean isComplete(FileInfo partInfo) {
        return partInfo.getOffset().equals(partInfo.getLength());
    }

    public Long getOffset(FileInfo partInfo) {
        String filePath = PathFactory.createPartPath((PartInfo) partInfo).toString();
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
