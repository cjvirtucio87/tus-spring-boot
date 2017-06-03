package com.tusspringboot.service;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static com.tusspringboot.Constants.*;

/**
 * Created by cjvirtucio on 5/30/17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UploadFileWriterTest {

    @InjectMocks
    UploadFileWriter uploadFileWriter;

    private PartInfo testPartInfo;

    List<PartInfo> testPartInfoList;

    private Path testDirPath;

    private Path testPartPath;

    private Path testFinalPath;

    @Before
    public void setup() throws IOException {
        testPartInfo = PartInfo.builder().fileName(TEST_FILENAME).partNumber(0L).build();
        testPartInfoList = createPartInfoList(TEST_FILENAME, TEST_UPLOAD_PART_COUNT, TEST_UPLOAD_PART_FILESIZE);
        testDirPath = PathFactory.createDirectoryPath(TEST_FILENAME);
        testPartPath = PathFactory.createPartPath(testPartInfo);
        testFinalPath = PathFactory.createFinalPath(TEST_FILENAME);
    }

    @Test
    public void createDirectory_ReturnStringDir_OnNotNullFilename() throws IOException {
        Assert.assertEquals(testDirPath.toString(), uploadFileWriter.createDirectory(TEST_FILENAME));
    }

    @Test(expected = IOException.class)
    public void createDirectory_ThrowIOException_OnNullFilename() throws IOException {
        uploadFileWriter.createDirectory(null);
    }

    @Test
    public void writeFilePart_ReturnPartInfoWrittenBytes_OnPartInfoWithOffsetLessThanLength() throws IOException {
        Files.createDirectory(testDirPath);

        InputStream is = createTestInputStream(createTestByteArray(TEST_UPLOADLENGTH));

        PartInfo partInfoInput = PartInfo.builder()
                .inputStream(is)
                .fileName(TEST_FILENAME)
                .partNumber(0L)
                .uploadOffset(TEST_UPLOADOFFSET)
                .uploadLength(TEST_UPLOADLENGTH)
                .build();

        Assert.assertEquals(TEST_UPLOADLENGTH, uploadFileWriter.writeFilePart(partInfoInput).getUploadOffset());
    }

    @Test
    public void writeFilePart_ReturnPartInfoZeroWrittenBytes_OnPartInfoOffsetEqualsLength() throws IOException {
        Files.createDirectory(testDirPath);

        InputStream is = createTestInputStream(createTestByteArray(TEST_UPLOADLENGTH));

        PartInfo partInfoInput = PartInfo.builder()
                .inputStream(is)
                .fileName(TEST_FILENAME)
                .partNumber(0L)
                .uploadOffset(TEST_UPLOADOFFSET + TEST_UPLOADOFFSET_INC_COMPLETE)
                .uploadLength(TEST_UPLOADLENGTH)
                .build();

        Assert.assertEquals(partInfoInput.getUploadOffset(), uploadFileWriter.writeFilePart(partInfoInput).getUploadOffset());
    }

    @Test
    public void concatenateFileParts_ReturnTotalBytesTransferred_OnPartInfoList() throws IOException {
        Files.createDirectory(testDirPath);

        testPartInfoList.forEach(uploadFileWriter::writeFilePart);

        assertEquals(TEST_UPLOAD_PART_COUNT * TEST_UPLOAD_PART_FILESIZE, (long) uploadFileWriter.concatenateFileParts(testPartInfoList));
    }

    @After
    public void tearDown() {
        testPartPath.toFile().delete();
        testPartInfoList.stream()
                .map(PathFactory::createPartPath)
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

    private List<PartInfo> createPartInfoList(String uploadFilename, Long uploadPartCount, Long uploadPartFileSize) {
        List<PartInfo> output = new ArrayList<>();

        for (long i = 0; i < uploadPartCount; i++) {
            output.add(PartInfo.builder()
                    .fileName(uploadFilename)
                    .partNumber(i)
                    .uploadOffset(i > 0 ? ( ( uploadPartCount * uploadPartFileSize ) * ( i  /  uploadPartCount ) + 1   ) :  ( ( uploadPartCount * uploadPartFileSize ) * ( i  /  uploadPartCount ) ))
                    .uploadLength( ( uploadPartCount * uploadPartFileSize ) * ( i + 1 ) / uploadPartCount)
                    .fileSize(uploadPartFileSize)
                    .inputStream(createTestInputStream(createTestByteArray(uploadPartFileSize)))
                    .build()
            );
        }

        return output;
    }
}
