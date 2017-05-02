package ReactMember.service;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
                log.error("Error closing RandomAccessFile for file, " + filePath);
            }
        }

        return filePointer;
    }

    public static String createDirectory(String fileName) {
        Path path = Paths.get(System.getProperty("java.io.tmpdir") + separator + fileName);
        try {
            Files.createDirectory(path);
            log.info("Created file directory, " + path.toString());
        } catch (Exception e) {
            log.error("Failed to create directory, " + path.toString());
        } finally {
            return path.toString();
        }
    }

    public static Function<String, List<Long>> getFilePointerList(List<Long> partNumbers) {
        return fileName -> partNumbers.stream()
                    .map(num -> {
                        String filePath = System.getProperty("java.io.tmpdir") + fileName + separator + fileName + "_" + num.toString();
                        RandomAccessFile raf = null;
                        Long filePointer = 0L;

                        try {
                            raf = new RandomAccessFile(filePath, "r");
                            filePointer = raf.getFilePointer();
                        } catch (Exception e) {
                            log.error("Error attempting to read file, " + filePath);
                        } finally {
                            try {
                                raf.close();
                            } catch (Exception e) {
                                log.error("Error attempting to close RandomAccessFile for file, " + filePath);
                            }
                        }

                        return filePointer;
                    })
                    .collect(Collectors.toList());
    }

    public static Boolean checkIfExists(String fileName) {
        Path path = Paths.get(System.getProperty("java.io.tmpdir") + separator + fileName);
        return Files.exists(path);
    }

    // TODO: writeFilePart stops halfway through; fix it
    public static Long writeFilePart(PartInfo partInfo) {
        String filePath = createFilePath(partInfo);
        Long currentOffset = 0L;
        Long newOffset = 0L;
        Long bytesTransferred = 0L;
        RandomAccessFile raf = null;
        ReadableByteChannel is = null;
        FileChannel os;

        try {
            log.info("Opening channels for file, " + filePath);
            raf = new RandomAccessFile(filePath, "rw");
            is = UploadUtil.getByteChannel(currentOffset, partInfo);
            os = raf.getChannel();
            currentOffset = raf.getFilePointer();

            log.info("Writing file, " + filePath);
            Long bytesToTransfer = (partInfo.getUploadLength() - partInfo.getUploadOffset()) - currentOffset;
            if (bytesToTransfer > 0) {
                bytesTransferred = os.transferFrom(is, raf.getFilePointer(), bytesToTransfer);
            }

        } catch (IOException e) {
            log.error("Error writing file, " + filePath);
            log.error("Error", e);
        } finally {
            log.info("Closing channels for, " + filePath);
            newOffset = currentOffset + bytesTransferred;

            try {
                is.close();
                raf.close();
            } catch (Exception e) {
                log.error("Error in attempt to close channels for, " + filePath);
            } finally {
                return newOffset;
            }
        }
    }

    public static Predicate<Long> checkIfComplete(PartInfo partInfo) {
        // TODO: fix this filter
        return filePointer -> filePointer == (partInfo.getUploadLength() - partInfo.getUploadOffset());
    }

    /**
     * The path to the file part consists of the following components:
     * (1) the tmp directory
     * (2) the file name as a folder
     * (3) the file name on the file itself
     * (4) the part number
     * @param partInfo
     * @return the file path for the part to be written
     */
    private static String createFilePath(PartInfo partInfo) {
        String path = System.getProperty("java.io.tmpdir") + separator + partInfo.getFileName()  + separator + partInfo.getFileName() + "_" + partInfo.getPartNumber();
        String message = "Creating file part, " + path;
        log.info(message);
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
