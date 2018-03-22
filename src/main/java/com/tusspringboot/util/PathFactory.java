package com.tusspringboot.util;

import com.tusspringboot.upload.api.FileInfo;
import com.tusspringboot.upload.impl.PartInfo;


import java.nio.file.Path;
import java.nio.file.Paths;

import static com.tusspringboot.util.Constants.TMP_DIR;

/**
 * Created by cjvirtucio on 5/29/17.
 */
public class PathFactory {

    public static Path createDirectoryPath(String fileName) {
        return Paths.get(TMP_DIR, fileName);
    }

    public static Path createPartPath(FileInfo partInfo) {
        return Paths.get(
                System.getProperty("java.io.tmpdir"),
                partInfo.getFileName(),
                partInfo.getFileName() + "_" + ((PartInfo) partInfo).getPartNumber()
        );
    }

    public static Path createFinalPath(String fileName, String fileExt) {
        return Paths.get(TMP_DIR, fileName, fileName + fileExt);
    }

    protected PathFactory() {
    }
}
