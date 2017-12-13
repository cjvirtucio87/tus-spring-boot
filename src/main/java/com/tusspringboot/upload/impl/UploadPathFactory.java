package com.tusspringboot.upload.impl;

import com.tusspringboot.upload.data.PartInfo;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.tusspringboot.util.Constants.TMP_DIR;

/**
 * Created by cjvirtucio on 5/29/17.
 */
public class UploadPathFactory {

    public static Path createDirectoryPath(String fileName) {
        return Paths.get(TMP_DIR, fileName);
    }

    public static Path createPartPath(PartInfo partInfo) {
        return Paths.get(
                System.getProperty("java.io.tmpdir"),
                partInfo.getFileName(),
                partInfo.getFileName() + "_" + partInfo.getPartNumber()
        );
    }

    public static Path createFinalPath(String fileName) {
        return Paths.get(TMP_DIR, fileName, fileName);
    }

    protected UploadPathFactory() {
    }
}
