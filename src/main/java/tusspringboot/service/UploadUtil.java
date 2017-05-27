package tusspringboot.service;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.ToLongFunction;

import static tusspringboot.Constants.TMP_DIR;

/**
 * Created by cvirtucio on 4/20/2017.
 */

@Slf4j
public class UploadUtil {

    public static Long getCurrentOffset(PartInfo partInfo) {
        String filePath = createFilePath(partInfo);
        log.info("Retrieving pointer for file part, " + filePath);
        Long currentOffset = 0L;

        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw");) {
            currentOffset = raf.length();
        } catch (Exception e) {
            log.error("Error attempting to get offset for file, " + filePath);
        }

        return currentOffset;
    }

    public static String createDirectory(String fileName) {
        Path path = Paths.get(TMP_DIR, fileName);
        try {
            Files.createDirectory(path);
            log.info("Created file directory, " + path.toString());
        } catch (Exception e) {
            log.error("Failed to create directory, " + path.toString());
        }

        return path.toString();
    }

    public static Boolean checkIfExists(String fileName) {
        return Paths.get(TMP_DIR, fileName).toFile().exists();
    }

    public static PartInfo writeFilePart(PartInfo partInfo) {
        String filePath = createFilePath(partInfo);
        Long bytesTransferred = 0L;

        try (
                RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
                ReadableByteChannel is = Channels.newChannel(partInfo.getInputStream());
                FileChannel os = raf.getChannel();
                ) {
            log.info("Opening channels for file part, " + filePath);
            log.info("Writing file part, " + filePath);
            Long bytesToTransfer = (partInfo.getUploadLength() - partInfo.getUploadOffset());

            if (bytesToTransfer > 0) {
                bytesTransferred = os.transferFrom(is, os.size(), bytesToTransfer);
            }

            log.info("Done writing file part, " + filePath);
        } catch (IOException e) {
            log.error("Error writing file part, " + filePath);
        }

        return PartInfo.builder()
                .uploadOffset(bytesTransferred + partInfo.getUploadOffset())
                .uploadLength(partInfo.uploadLength)
                .fileName(partInfo.fileName)
                .partNumber(partInfo.partNumber)
                .build();
    }

    public static boolean checkIfComplete(PartInfo partInfo) {
        return partInfo.uploadOffset.equals(partInfo.uploadLength);
    }

    public static Long concatenate(List<PartInfo> partInfoList) {
        String fileName = partInfoList.get(0).getFileName();
        String finalPath = Paths.get(TMP_DIR, fileName, fileName).toString();
        Long totalBytesTransferred = 0L;

        try (
                RandomAccessFile raf = new RandomAccessFile(finalPath, "rw");
                FileChannel outputStream = raf.getChannel();
                ) {
            log.info("Concatenating file, " + fileName);
            totalBytesTransferred = partInfoList.stream()
                    .mapToLong(UploadUtil.toBytesTransferred(outputStream))
                    .sum();
        } catch (IOException e) {
            log.error("Error attempting to concatenate parts for file, " + fileName);
        }

        return totalBytesTransferred;
    }

    private static ToLongFunction<PartInfo> toBytesTransferred(FileChannel outputStream) {
        return partInfo -> {
            Long bytesTransferred = 0L;
            try {
                InputStream is = Files.newInputStream(
                        Paths.get(
                                TMP_DIR,
                                partInfo.getFileName(),
                                partInfo.getFileName() + "_" + partInfo.getPartNumber()
                        )
                );

                bytesTransferred = outputStream.transferFrom(
                        Channels.newChannel(is),
                        outputStream.size(),
                        partInfo.getFileSize()

                );
            } catch (IOException e) {
                log.error("Error during file concatenation for file, " + partInfo.getFileName());
            }
            return bytesTransferred;
        };
    }

    /**
     * The path to the file part consists of the following components:
     * (1) the tmp directory
     * (2) the file name as a folder
     * (3) the file name on the file itself
     * (4) the part number
     *
     * e.g. For n file parts, "/c/tmp/myFile/myFile_0", "/c/tmp/myFile/myFile_1", ..., "/c/tmp/myFile_myFile_n".
     *
     * @param partInfo
     * @return the file path for the part to be written
     */
    private static String createFilePath(PartInfo partInfo) {
        return Paths.get(
                System.getProperty("java.io.tmpdir"),
                partInfo.getFileName(),
                partInfo.getFileName() + "_" + partInfo.getPartNumber()
        ).toString();
    }

    private UploadUtil() {
    }
}
