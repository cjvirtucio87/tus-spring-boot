package com.tusspringboot.util;

/**
 * Created by cvirtucio on 4/19/2017.
 */
public class Constants {
    public static final String TMP_DIR = System.getProperty("java.io.tmpdir");

    public static final long PART_SIZE = 1054 * 1054 * 5;

    public static final String TEST_FILENAME = "testFilename";

    public static final String TEST_FILEDIR = "testFileDirectory";

    public static final String TEST_USERNAME = "testuser";

    public static final Long TEST_UPLOADOFFSET = 0L;

    public static final Long TEST_UPLOADOFFSET_INC = 2L;

    public static final Long TEST_UPLOADOFFSET_INC_COMPLETE = 5L;

    public static final Long TEST_UPLOADLENGTH = 5L;

    public static final Long TEST_UPLOAD_PART_FILESIZE = 5L;

    public static final Long TEST_UPLOAD_PART_COUNT = 5L;
}
