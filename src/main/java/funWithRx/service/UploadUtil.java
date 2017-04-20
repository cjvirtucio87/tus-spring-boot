package funWithRx.service;

import org.apache.log4j.Logger;
import org.springframework.cglib.core.internal.Function;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import static java.io.File.separator;

/**
 * Created by cvirtucio on 4/20/2017.
 */

public class UploadUtil {
    private static final Logger log = Logger.getLogger(UploadUtil.class);

    public static Long writeFilePart(PartInfo partInfo) throws Exception {
        String filePath = createFilePath(partInfo);
        log.info("Opening channels for file, " + filePath);
        RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
        ReadableByteChannel is = UploadUtil.getByteChannel(raf.getFilePointer(), partInfo);
        FileChannel os = raf.getChannel();

        try {
            log.info("Writing file, " + filePath);
            Long bytesToTransfer = partInfo.getUploadLength() - raf.getFilePointer();
            if (bytesToTransfer > 0) {
                os.transferFrom(is, raf.getFilePointer(), bytesToTransfer);
            }
        } catch (IOException e) {
            log.error("Error writing file, " + filePath);
            log.error("Error", e);
        } finally {
            log.info("Error writing file, " + filePath);
            is.close();
            raf.close();
        }

        return raf.getFilePointer();
    }

    public static Function checkIfComplete(PartInfo partInfo) {
        return filePointer -> filePointer == partInfo.getUploadLength();
    }

    /**
     * The path to the file part consists of the following components:
     * (1) the tmp directory
     * (2) the file name
     * (3) the part number
     * @param partInfo
     * @return the file path for the part to be written
     */
    private static String createFilePath(PartInfo partInfo) {
        String path = System.getProperty("java.io.tmpdir") + separator + partInfo.getFileName() + "_" + partInfo.getPartNumber();
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
