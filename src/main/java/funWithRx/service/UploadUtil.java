package funWithRx.service;

import funWithRx.UploadController;
import org.springframework.cglib.core.internal.Function;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Logger;
import static java.io.File.separator;

/**
 * Created by cvirtucio on 4/20/2017.
 */

public class UploadUtil {
    private static final Logger log = Logger.getLogger(UploadController.class.toString());

    public static Long writeFilePart(PartInfo partInfo) throws Exception {
        String filePath = createFilePath(partInfo);
        log.info("Opening channels for file, " + filePath);
        RandomAccessFile raf = new RandomAccessFile(filePath, "rw");
        ReadableByteChannel is = UploadUtil.getByteChannel(raf.getFilePointer(), partInfo);
        FileChannel os = raf.getChannel();
        Long newOffset;

        try {
            log.info("Writing file, " + filePath);
            Long bytesToTransfer = partInfo.getUploadLength() - raf.getFilePointer();
            if (bytesToTransfer > 0) {
                os.transferFrom(is, raf.getFilePointer(), bytesToTransfer);
            }
        } catch (IOException e) {
            log.warning("Error writing file, " + filePath);
            log.warning(e.getMessage());
        } finally {
            log.info("Closing channels for file, " + filePath);
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
        log.info("Creating file part, " + path);
        return path;
    }

    private static ReadableByteChannel getByteChannel(Long filePointer, PartInfo partInfo) throws Exception {
        InputStream is = partInfo.getInputStream();
        is.skip(filePointer);
        return Channels.newChannel(is);
    }
}
