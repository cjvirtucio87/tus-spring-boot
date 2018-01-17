package com.tusspringboot.upload.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.ToLongFunction;

import org.springframework.stereotype.Service;

import com.tusspringboot.upload.api.FileInfo;
import com.tusspringboot.upload.api.FileWriter;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by cjvirtucio on 5/29/17.
 */

@Service
@Slf4j
public class UploadFileWriter implements FileWriter {

    public String createDirectory(String fileName) throws IOException {
        if (fileName == null) {
            throw new IOException("Cannot create directory for a null filename!");
        }

        Path path = UploadPathFactory.createDirectoryPath(fileName);
        Files.createDirectory(path);
        log.info("Created file directory, " + path.toString());
        return path.toString();
    }

    public PartInfo write(FileInfo partInfo) {
        String filePath = UploadPathFactory.createPartPath((PartInfo) partInfo).toString();
        Long bytesTransferred = 0L;

        try (
                RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
                ReadableByteChannel is = Channels.newChannel(partInfo.getInputStream());
                FileChannel os = raf.getChannel();
        ) {
            log.info("Opening channels for file part, " + filePath);
            log.info("Writing file part, " + filePath);
            Long bytesToTransfer = (partInfo.getLength() - partInfo.getOffset());

            if (bytesToTransfer > 0) {
                bytesTransferred = os.transferFrom(is, os.size(), bytesToTransfer);
            }

            if (bytesToTransfer.equals(bytesTransferred)) {
                log.info("Done writing file part, " + filePath);
            }
        } catch (IOException e) {
            log.error("Error writing file part, " + filePath);
            throw new RuntimeException(e);
        }

        return PartInfo.builder()
                .offset(partInfo.getOffset() + bytesTransferred)
                .length(partInfo.getLength())
                .fileName(partInfo.getFileName())
                .partNumber(((PartInfo) partInfo).getPartNumber())
                .build();
    }

    public Long concat(List<FileInfo> partInfoList) throws IOException {
        String fileName = partInfoList.get(0).getFileName();
        String finalPath = UploadPathFactory.createFinalPath(fileName).toString();
        Long totalBytesTransferred = 0L;

        try (
                RandomAccessFile raf = new RandomAccessFile(finalPath, "rw");
                FileChannel outputStream = raf.getChannel();
        ) {
            log.info("Concatenating file, " + fileName);
            totalBytesTransferred = partInfoList.stream()
                    .mapToLong(toBytesTransferred(outputStream))
                    .sum();
        } catch (IOException e) {
            log.error("Error attempting to concatenate parts for file, " + fileName);
            throw new IOException(e);
        }

        return totalBytesTransferred;
    }

    private ToLongFunction<FileInfo> toBytesTransferred(FileChannel outputStream) {
        return partInfo -> {
            Long bytesTransferred = 0L;
            try {
                InputStream is = Files.newInputStream(UploadPathFactory.createPartPath((PartInfo) partInfo));

                bytesTransferred = outputStream.transferFrom(
                        Channels.newChannel(is),
                        outputStream.size(),
                        partInfo.getFileSize()

                );
            } catch (IOException e) {
                log.error("Error during file concatenation for file, " + partInfo.getFileName());
                throw new RuntimeException(e);
            }
            return bytesTransferred;
        };
    }
}
