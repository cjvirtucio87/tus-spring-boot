package com.tusspringboot.service;

import com.tusspringboot.upload.data.PartInfo;
import com.tusspringboot.upload.impl.UploadFileReader;
import com.tusspringboot.upload.impl.UploadFileWriter;
import com.tusspringboot.upload.impl.UploadService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static com.tusspringboot.util.Constants.*;

/**
 * Created by cjvirtucio on 5/27/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UploadServiceTest {
    @InjectMocks
    UploadService uploadService;

    @Mock
    UploadFileReader uploadFileReader;

    @Mock
    UploadFileWriter uploadFileWriter;

    @Test
    public void mapToCurrentOffsetList_ReturnListLong_OnFilenameExists() throws IOException {
        PartInfo partInfo = PartInfo.builder().build();

        List<PartInfo> partInfoList = Arrays.asList(partInfo);

        when(uploadFileReader.checkIfExists(anyString())).thenReturn(true);

        assertFalse(uploadService.mapToCurrentOffsetList(TEST_FILENAME, partInfoList).isEmpty());
    }

    @Test(expected = IOException.class)
    public void mapToCurrentOffsetList_ThrowIOException_OnFilenameNotExist() throws IOException {
        PartInfo partInfo = PartInfo.builder().build();

        List<PartInfo> partInfoList = Arrays.asList(partInfo);

        when(uploadFileReader.checkIfExists(anyString())).thenReturn(false);

        uploadService.mapToCurrentOffsetList(TEST_FILENAME, partInfoList);
    }

    @Test
    public void mapToDirectoryPath_ReturnDirectoryPathString_OnNotNullFilename() throws IOException {
        when(uploadFileWriter.createDirectory(anyString())).thenReturn(TEST_FILEDIR);

        Assert.assertEquals(TEST_FILEDIR, uploadService.mapToDirectoryPath(TEST_FILENAME));
    }

    @Test(expected = IOException.class)
    public void mapToDirectoryPath_ThrowIOException_OnNullFilename() throws IOException {
        when(uploadFileWriter.createDirectory(null)).thenThrow(new IOException());

        uploadService.mapToDirectoryPath(null);
    }

    @Test
    public void mapToPartInfoWrittenBytes_ReturnPartInfoWithWrittenBytes_OnCompleteUpload() throws IOException {
        PartInfo partInfoInput = PartInfo.builder()
                .fileSize(5L)
                .fileName(TEST_FILENAME)
                .partNumber(0L)
                .uploadOffset(TEST_UPLOADOFFSET)
                .uploadLength(TEST_UPLOADLENGTH)
                .userName(TEST_USERNAME)
                .build();

        PartInfo partInfoOutput = PartInfo.builder()
                .fileSize(5L)
                .fileName(TEST_FILENAME)
                .partNumber(0L)
                .uploadOffset(partInfoInput.getUploadOffset() + TEST_UPLOADOFFSET_INC_COMPLETE)
                .uploadLength(TEST_UPLOADLENGTH)
                .userName(TEST_USERNAME)
                .build();

        when(uploadFileWriter.writeFilePart(partInfoInput)).thenReturn(partInfoOutput);
        when(uploadFileReader.checkIfComplete(partInfoOutput)).thenReturn(true);

        Assert.assertEquals(TEST_UPLOADLENGTH, uploadService.mapToPartInfoWrittenBytes(partInfoInput).getUploadOffset());
    }

    @Test(expected = IOException.class)
    public void mapToPartInfoWrittenBytes_ThrowIOException_OnIncompleteUpload() throws IOException {
        PartInfo partInfoInput = PartInfo.builder()
                .fileSize(5L)
                .fileName(TEST_FILENAME)
                .partNumber(0L)
                .uploadOffset(TEST_UPLOADOFFSET)
                .uploadLength(TEST_UPLOADLENGTH)
                .userName(TEST_USERNAME)
                .build();

        PartInfo partInfoOutput = PartInfo.builder()
                .fileSize(5L)
                .fileName(TEST_FILENAME)
                .partNumber(0L)
                .uploadOffset(partInfoInput.getUploadOffset() + TEST_UPLOADOFFSET_INC)
                .uploadLength(5L)
                .userName(TEST_USERNAME)
                .build();

        when(uploadFileWriter.writeFilePart(partInfoInput)).thenReturn(partInfoOutput);
        when(uploadFileReader.checkIfComplete(partInfoOutput)).thenReturn(false);

        Assert.assertEquals((Long) 2L, uploadService.mapToPartInfoWrittenBytes(partInfoInput).getUploadOffset());
    }

    @Test
    public void reduceToTotalBytesTransferred_ReturnLong_OnPartInfoList() throws IOException {
        PartInfo partInfoA = PartInfo.builder().fileSize(TEST_UPLOAD_PART_FILESIZE).build();
        PartInfo partInfoB = PartInfo.builder().fileSize(TEST_UPLOAD_PART_FILESIZE).build();
        PartInfo partInfoC = PartInfo.builder().fileSize(TEST_UPLOAD_PART_FILESIZE).build();

        List<PartInfo> partInfoList = Arrays.asList(partInfoA, partInfoB, partInfoC);

        Long sum = partInfoA.getFileSize() + partInfoB.getFileSize() + partInfoC.getFileSize();

        when(uploadFileWriter.concatenateFileParts(partInfoList)).thenReturn(sum);

        Assert.assertEquals((Long) (TEST_UPLOAD_PART_FILESIZE * 3), uploadService.reduceToTotalBytesTransferred(partInfoList));
    }
}