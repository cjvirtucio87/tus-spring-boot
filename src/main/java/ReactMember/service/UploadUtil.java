package ReactMember.service;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

import static java.io.File.separator;

/**
 * Created by cvirtucio on 4/20/2017.
 */

public class UploadUtil {
    private static final Logger log = Logger.getLogger(UploadUtil.class);

    public static Long getFilePointer(PartInfo partInfo) {
        String filePath = createFilePath(partInfo);
        Long filePointer = 0L;
        RandomAccessFile raf = null;

        try {
            raf = new RandomAccessFile(filePath, "rw");
            filePointer = raf.getFilePointer();
        } catch (Exception e) {
            log.error("Error attempting to get offset for file, " + filePath);
        } finally {
            try {
                raf.close();
            } catch (Exception e) {
                log.error("Error closing RAF channel for file, " + filePath);
            }
        }

        return filePointer;
    }

    public static String createDirectory(String fileName) {
        Path path = Paths.get(
                System.getProperty("java.io.tmpdir"),
                fileName
        );
        try {
            Files.createDirectory(path);
            log.info("Created file directory, " + path.toString());
        } catch (Exception e) {
            log.error("Failed to create directory, " + path.toString());
        } finally {
            return path.toString();
        }
    }

    public static Boolean checkIfExists(String fileName) {
        Path path = Paths.get(System.getProperty("java.io.tmpdir") + separator + fileName);
        return Files.exists(path);
    }

    public static Long writeFilePart(PartInfo partInfo) {
        String filePath = createFilePath(partInfo);
        Long currentOffset = 0L;
        Long newOffset = 0L;
        Long bytesTransferred = 0L;
        RandomAccessFile raf = null;
        ReadableByteChannel is = null;
        FileChannel os;

        try {
            log.info("Opening channels for file part, " + filePath);
            raf = new RandomAccessFile(filePath, "rw");
            is = UploadUtil.getByteChannel(currentOffset, partInfo);
            os = raf.getChannel();
            currentOffset = raf.getFilePointer();

            log.info("Writing file part, " + filePath);
            Long bytesToTransfer = (partInfo.getUploadLength() - partInfo.getUploadOffset()) - currentOffset;
            if (bytesToTransfer > 0) {
                bytesTransferred = os.transferFrom(is, currentOffset, bytesToTransfer);
            }

            log.info("Done writing file part, " + filePath);
        } catch (IOException e) {
            log.error("Error writing file part, " + filePath);
            log.error("Error", e);
        } finally {
            log.info("Closing channels for file part, " + filePath);
            newOffset = currentOffset + bytesTransferred;

            try {
                is.close();
                raf.close();
            } catch (Exception e) {
                log.error("Error in attempt to close channels for file part, " + filePath);
            } finally {
                return newOffset;
            }
        }
    }

    public static Predicate<Long> checkIfComplete(PartInfo partInfo) {
        return filePointer -> filePointer == (partInfo.getUploadLength() - partInfo.getUploadOffset());
    }

    public static String concatenate(List<PartInfo> partInfoList) {
        final String fileName = partInfoList.get(0).getFileName();
        final Long fileSize = partInfoList.get(0).getFileSize();
        final String finalPath = Paths.get(
                System.getProperty("java.io.tmpdir"),
                fileName,
                fileName
        ).toString();
        RandomAccessFile raf = null;
        FileChannel os = null;
        Long currentOffset = 0L;

        try {
            log.info("Concatenating file parts for file, " + finalPath);
            raf = new RandomAccessFile(finalPath, "rw");
            os = raf.getChannel();

            for (PartInfo partInfo : partInfoList) {
                Path partPath = Paths.get(
                        System.getProperty("java.io.tmpdir"),
                        partInfo.getFileName(),
                        partInfo.getFileName() + "_" + partInfo.getPartNumber()
                );
                InputStream is = Files.newInputStream(partPath);
                try {
                    currentOffset += os.transferFrom(Channels.newChannel(is), currentOffset, fileSize);
                } catch (Exception e) {
                    log.error("Error attempting to concatenate file parts for file, " + finalPath);
                }
            }

            log.info("Done concatenating file parts for file, " + finalPath);
        } catch (Exception e) {
            log.error("Error attempting to get output stream for file, " + finalPath);
        } finally {
            log.info("Closing channels for file, " + finalPath);
            try {
                raf.close();
                os.close();
            } catch (Exception e) {
                log.error("Error closing channels for concatenation of file, " + finalPath);
            }
        }

        return fileName;
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
        String path = Paths.get(
                System.getProperty("java.io.tmpdir"),
                partInfo.getFileName(),
                partInfo.getFileName() + "_" + partInfo.getPartNumber()
        ).toString();
        log.info("Creating file part, " + path);
        return path;
    }

    private static ReadableByteChannel getByteChannel(Long filePointer, PartInfo partInfo) throws Exception {
        InputStream is = partInfo.getInputStream();
        is.skip(filePointer);
        return Channels.newChannel(is);
    }

    private UploadUtil() {
    }
}
