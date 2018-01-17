package com.tusspringboot.service;

import static com.tusspringboot.util.Constants.TEST_FILENAME;
import static com.tusspringboot.util.Constants.TEST_UPLOADLENGTH;
import static com.tusspringboot.util.Constants.TEST_UPLOADOFFSET;
import static com.tusspringboot.util.Constants.TEST_UPLOADOFFSET_INC_COMPLETE;
import static com.tusspringboot.util.Constants.TEST_UPLOAD_PART_COUNT;
import static com.tusspringboot.util.Constants.TEST_UPLOAD_PART_FILESIZE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tusspringboot.upload.api.FileInfo;
import com.tusspringboot.upload.impl.PartInfo;
import com.tusspringboot.upload.impl.UploadFileWriter;
import com.tusspringboot.upload.impl.UploadPathFactory;

/**
 * Created by cjvirtucio on 5/30/17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UploadFileWriterTest {

    @Autowired
    private UploadFileWriter uploadFileWriter;

    private PartInfo testPartInfo;

    List<FileInfo> testPartInfoList;

    private Path testDirPath;

    private Path testPartPath;

    private Path testFinalPath;

    @Before
    public void setup() throws IOException {
        testPartInfo = PartInfo.builder().fileName(TEST_FILENAME).partNumber(0L).build();
        testPartInfoList = createPartInfoList(TEST_FILENAME, TEST_UPLOAD_PART_COUNT, TEST_UPLOAD_PART_FILESIZE);
        testDirPath = UploadPathFactory.createDirectoryPath(TEST_FILENAME);
        testPartPath = UploadPathFactory.createPartPath(testPartInfo);
        testFinalPath = UploadPathFactory.createFinalPath(TEST_FILENAME);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createDirectory_CreatesDirectory_OnNotNullFilename() throws IOException {
        uploadFileWriter.createDirectory(TEST_FILENAME);
        assertTrue(testDirPath.toFile().exists());
    }

    @Test(expected = IOException.class)
    public void createDirectory_DoesNotCreateDirectory_OnNullFilename() throws IOException {
        uploadFileWriter.createDirectory(null);
        assertFalse(testDirPath.toFile().exists());
    }

    @Test
    public void createDirectory_ReturnStringDir_OnNotNullFilename() throws IOException {
        assertEquals(testDirPath.toString(), uploadFileWriter.createDirectory(TEST_FILENAME));
    }

    @Test(expected = IOException.class)
    public void createDirectory_ThrowIOException_OnNullFilename() throws IOException {
        uploadFileWriter.createDirectory(null);
    }

    @Test
    public void writeFilePart_WritesFilePart_OnPartInfo() throws IOException {
        Files.createDirectory(testDirPath);

        InputStream is = createTestInputStream(createTestByteArray(TEST_UPLOADLENGTH));

        PartInfo partInfoInput = PartInfo.builder()
                .inputStream(is)
                .fileName(TEST_FILENAME)
                .partNumber(0L)
                .offset(TEST_UPLOADOFFSET)
                .length(TEST_UPLOADLENGTH)
                .build();

        uploadFileWriter.write(partInfoInput);

        assertTrue(testPartPath.toFile().exists());
    }

    @Test
    public void writeFilePart_WritesBytesUpToUploadLength_OnPartInfoWithOffsetLessThanLength() throws IOException {
        Files.createDirectory(testDirPath);

        InputStream is = createTestInputStream(createTestByteArray(TEST_UPLOADLENGTH));

        PartInfo partInfoInput = PartInfo.builder()
                .inputStream(is)
                .fileName(TEST_FILENAME)
                .partNumber(0L)
                .offset(TEST_UPLOADOFFSET)
                .length(TEST_UPLOADLENGTH)
                .build();

        uploadFileWriter.write(partInfoInput);

        assertEquals((long) TEST_UPLOADLENGTH, testPartPath.toFile().length());
    }

    @Test
    public void writeFilePart_WritesZeroBytes_OnPartInfoWithOffsetEqualLength() throws IOException {
        Files.createDirectory(testDirPath);

        InputStream is = createTestInputStream(createTestByteArray(TEST_UPLOADLENGTH));

        PartInfo partInfoInput = PartInfo.builder()
                .inputStream(is)
                .fileName(TEST_FILENAME)
                .partNumber(0L)
                .offset(TEST_UPLOADOFFSET + TEST_UPLOADOFFSET_INC_COMPLETE)
                .length(TEST_UPLOADLENGTH)
                .build();

        uploadFileWriter.write(partInfoInput);

        assertEquals(0L, testPartPath.toFile().length());
    }

    @Test
    public void writeFilePart_ReturnPartInfoWrittenBytes_OnPartInfoWithOffsetLessThanLength() throws IOException {
        Files.createDirectory(testDirPath);

        InputStream is = createTestInputStream(createTestByteArray(TEST_UPLOADLENGTH));

        PartInfo partInfoInput = PartInfo.builder()
                .inputStream(is)
                .fileName(TEST_FILENAME)
                .partNumber(0L)
                .offset(TEST_UPLOADOFFSET)
                .length(TEST_UPLOADLENGTH)
                .build();

        assertEquals(TEST_UPLOADLENGTH, uploadFileWriter.write(partInfoInput).getOffset());
    }

    @Test
    public void writeFilePart_ReturnPartInfoZeroWrittenBytes_OnPartInfoOffsetEqualsLength() throws IOException {
        Files.createDirectory(testDirPath);

        InputStream is = createTestInputStream(createTestByteArray(TEST_UPLOADLENGTH));

        PartInfo partInfoInput = PartInfo.builder()
                .inputStream(is)
                .fileName(TEST_FILENAME)
                .partNumber(0L)
                .offset(TEST_UPLOADOFFSET + TEST_UPLOADOFFSET_INC_COMPLETE)
                .length(TEST_UPLOADLENGTH)
                .build();

        assertEquals(partInfoInput.getOffset(), uploadFileWriter.write(partInfoInput).getOffset());
    }

    @Test
    public void concatenateFileParts_ReturnTotalBytesTransferred_OnPartInfoList() throws IOException {
        Files.createDirectory(testDirPath);

        testPartInfoList.forEach(uploadFileWriter::write);

        assertEquals(TEST_UPLOAD_PART_COUNT * TEST_UPLOAD_PART_FILESIZE, (long) uploadFileWriter.concat(testPartInfoList));
    }

    @After
    public void tearDown() {
        testPartPath.toFile().delete();
        testPartInfoList.stream()
                .map(UploadPathFactory::createPartPath)
                .map(Path::toFile)
                .forEach(File::delete);
        testFinalPath.toFile().delete();
        testDirPath.toFile().delete();
    }

    private InputStream createTestInputStream(byte[] data) {
        return new ByteArrayInputStream(data);
    }

    private byte[] createTestByteArray(long len) {
        byte[] output = new byte[(int) len];

        for (int i = 0; i < len; i++) {
            output[i] = 0;
        }

        return output;
    }

    private List<FileInfo> createPartInfoList(String uploadFilename, Long uploadPartCount, Long uploadPartFileSize) {
        List<FileInfo> output = new ArrayList<>();

        for (long i = 0; i < uploadPartCount; i++) {
            output.add(PartInfo.builder()
                    .fileName(uploadFilename)
                    .partNumber(i)
                    .offset(i > 0 ? ( ( uploadPartCount * uploadPartFileSize ) * ( i  /  uploadPartCount ) + 1   ) :  ( ( uploadPartCount * uploadPartFileSize ) * ( i  /  uploadPartCount ) ))
                    .length( ( uploadPartCount * uploadPartFileSize ) * ( i + 1 ) / uploadPartCount)
                    .fileSize(uploadPartFileSize)
                    .inputStream(createTestInputStream(createTestByteArray(uploadPartFileSize)))
                    .build()
            );
        }

        return output;
    }
}
