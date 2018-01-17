package com.tusspringboot.service;

import static com.tusspringboot.util.Constants.TEST_FILENAME;
import static com.tusspringboot.util.Constants.TEST_UPLOADLENGTH;
import static com.tusspringboot.util.Constants.TEST_UPLOADOFFSET;
import static com.tusspringboot.util.Constants.TEST_UPLOADOFFSET_INC_COMPLETE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tusspringboot.upload.impl.PartInfo;
import com.tusspringboot.upload.impl.UploadFileReader;
import com.tusspringboot.upload.impl.UploadPathFactory;

/**
 * Created by cjvirtucio on 5/29/17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UploadFileReaderTest {
    
	@Autowired
    private UploadFileReader uploadFileReader;

    private PartInfo testPartInfo;

    private Path testDirPath;

    private Path testPartPath;

    @Before
    public void setup() throws IOException {
        testPartInfo = PartInfo.builder().fileName(TEST_FILENAME).partNumber(0L).build();
        testDirPath = UploadPathFactory.createDirectoryPath(TEST_FILENAME);
        testPartPath = UploadPathFactory.createPartPath(testPartInfo);
        Files.createDirectory(testDirPath);
        Files.createFile(testPartPath);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void checkIfExists_ReturnTrue_OnDirectoryExists() {
        assertTrue(uploadFileReader.fileExists(TEST_FILENAME));
    }

    @Test
    public void checkIfExists_ReturnFalse_OnDirectoryNotExist() {
        assertFalse(uploadFileReader.fileExists(TEST_FILENAME + "HELLO"));
    }

    @Test
    public void checkIfComplete_ReturnTrue_OnOffsetMatchesLength() {
        PartInfo partInfo = PartInfo.builder()
                .offset(TEST_UPLOADOFFSET + TEST_UPLOADOFFSET_INC_COMPLETE)
                .length(TEST_UPLOADLENGTH)
                .build();

        assertTrue(uploadFileReader.isComplete(partInfo));
    }

    @Test
    public void checkIfComplete_ReturnFalse_OnOffsetLessThanLength() {
        PartInfo partInfo = PartInfo.builder()
                .offset(TEST_UPLOADOFFSET)
                .length(TEST_UPLOADLENGTH)
                .build();

        assertFalse(uploadFileReader.isComplete(partInfo));
    }

    @Test
    public void getCurrentOffset_ReturnZero_OnEmptyFile() throws IOException {
        PartInfo partInfo = PartInfo.builder()
                .fileName(TEST_FILENAME)
                .partNumber(0L)
                .build();

        assertEquals(0L, (long) uploadFileReader.getOffset(partInfo));
    }

    @Test
    public void getCurrentOffset_ReturnLong_OnWrittenFile() throws IOException {
        PartInfo partInfo = PartInfo.builder()
                .fileName(TEST_FILENAME)
                .partNumber(0L)
                .build();

        byte[] byteArray = new byte[]{ 0, 0, 0 };

        Files.write(testPartPath, byteArray);

        assertEquals(byteArray.length, (long) uploadFileReader.getOffset(partInfo));
    }

    @After
    public void tearDown() throws IOException {
        testPartPath.toFile().delete();
        testDirPath.toFile().delete();
    }
}
