package tusspringboot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.RandomAccessFile;
import java.nio.file.Paths;

import static tusspringboot.Constants.TMP_DIR;

/**
 * Created by cjvirtucio on 5/29/17.
 */
@Service
@Slf4j
public class UploadFileReader {

    public boolean checkIfExists(String fileName) {
        return Paths.get(TMP_DIR, fileName).toFile().exists();
    }

    public boolean checkIfComplete(PartInfo partInfo) {
        return partInfo.uploadOffset.equals(partInfo.uploadLength);
    }

    public Long getCurrentOffset(PartInfo partInfo) {
        String filePath = PathFactory.createPartPath(partInfo).toString();
        log.info("Retrieving pointer for file part, " + filePath);
        Long currentOffset = 0L;

        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw");) {
            currentOffset = raf.length();
        } catch (Exception e) {
            log.error("Error attempting to get offset for file, " + filePath);
            throw new RuntimeException(e);
        }

        return currentOffset;
    }
}
