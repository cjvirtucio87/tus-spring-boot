package com.tusspringboot.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static com.tusspringboot.Constants.*;

/**
 * Created by cjvirtucio on 5/29/17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UploadFileReaderTest {

    @InjectMocks
    UploadFileReader uploadFileReader;

    private PartInfo testPartInfo;

    private Path testDirPath;

    private Path testPartPath;

    @Before
    public void setup() throws IOException {
        testPartInfo = PartInfo.builder().fileName(TEST_FILENAME).partNumber(0L).build();
        testDirPath = PathFactory.createDirectoryPath(TEST_FILENAME);
        testPartPath = PathFactory.createPartPath(testPartInfo);
        Files.createDirectory(testDirPath);
        Files.createFile(testPartPath);
    }

    @Test
    public void checkIfExists_ReturnTrue_OnDirectoryExists() {
        assertTrue(uploadFileReader.checkIfExists(TEST_FILENAME));
    }

    @Test
    public void checkIfExists_ReturnFalse_OnDirectoryNotExist() {
        assertFalse(uploadFileReader.checkIfExists(TEST_FILENAME + "HELLO"));
    }

    @Test
    public void checkIfComplete_ReturnTrue_OnOffsetMatchesLength() {
        PartInfo partInfo = PartInfo.builder()
                .uploadOffset(TEST_UPLOADOFFSET + TEST_UPLOADOFFSET_INC_COMPLETE)
                .uploadLength(TEST_UPLOADLENGTH)
                .build();

        assertTrue(uploadFileReader.checkIfComplete(partInfo));
    }

    @Test
    public void checkIfComplete_ReturnFalse_OnOffsetLessThanLength() {
        PartInfo partInfo = PartInfo.builder()
                .uploadOffset(TEST_UPLOADOFFSET)
                .uploadLength(TEST_UPLOADLENGTH)
                .build();

        assertFalse(uploadFileReader.checkIfComplete(partInfo));
    }

    @Test
    public void getCurrentOffset_ReturnZero_OnEmptyFile() throws IOException {
        PartInfo partInfo = PartInfo.builder()
                .fileName(TEST_FILENAME)
                .partNumber(0L)
                .build();

        assertEquals(0L, (long) uploadFileReader.getCurrentOffset(partInfo));
    }

    @Test
    public void getCurrentOffset_ReturnLong_OnWrittenFile() throws IOException {
        PartInfo partInfo = PartInfo.builder()
                .fileName(TEST_FILENAME)
                .partNumber(0L)
                .build();

        byte[] byteArray = new byte[]{ 0, 0, 0 };

        Files.write(testPartPath, byteArray);

        assertEquals(byteArray.length, (long) uploadFileReader.getCurrentOffset(partInfo));
    }

    @After
    public void tearDown() throws IOException {
        testPartPath.toFile().delete();
        testDirPath.toFile().delete();
    }
}
