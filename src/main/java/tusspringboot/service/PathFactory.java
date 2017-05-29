package tusspringboot.service;

import java.nio.file.Path;
import java.nio.file.Paths;

import static tusspringboot.Constants.TMP_DIR;

/**
 * Created by cjvirtucio on 5/29/17.
 */
public class PathFactory {

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
}
