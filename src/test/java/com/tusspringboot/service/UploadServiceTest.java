package com.tusspringboot.service;

import static com.tusspringboot.util.Constants.TEST_FILEDIR;
import static com.tusspringboot.util.Constants.TEST_FILENAME;
import static com.tusspringboot.util.Constants.TEST_UPLOADLENGTH;
import static com.tusspringboot.util.Constants.TEST_UPLOADOFFSET;
import static com.tusspringboot.util.Constants.TEST_UPLOADOFFSET_INC;
import static com.tusspringboot.util.Constants.TEST_UPLOADOFFSET_INC_COMPLETE;
import static com.tusspringboot.util.Constants.TEST_UPLOAD_PART_FILESIZE;
import static com.tusspringboot.util.Constants.TEST_USERNAME;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tusspringboot.upload.api.FileInfo;
import com.tusspringboot.upload.api.UploadService;
import com.tusspringboot.upload.impl.PartInfo;
import com.tusspringboot.upload.impl.UploadFileReader;
import com.tusspringboot.upload.impl.UploadFileWriter;
import com.tusspringboot.upload.impl.UploadServiceImpl;

/**
 * Created by cjvirtucio on 5/27/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UploadServiceTest {

    @Mock
    private UploadFileReader uploadFileReader;

    @Mock
    private UploadFileWriter uploadFileWriter;
    
    @InjectMocks
    private UploadService uploadService = new UploadServiceImpl(uploadFileReader, uploadFileWriter);    
    
    @Before
    public void setup() {
    		MockitoAnnotations.initMocks(this);
    }

    @Test
    public void mapToCurrentOffsetList_ReturnListLong_OnFilenameExists() throws IOException {
        PartInfo partInfo = PartInfo.builder().build();

        List<FileInfo> partInfoList = Arrays.asList(partInfo);

        when(uploadFileReader.fileExists(anyString())).thenReturn(true);

        assertFalse(uploadService.getCurrentOffsets(TEST_FILENAME, partInfoList).isEmpty());
    }

    @Test(expected = IOException.class)
    public void mapToCurrentOffsetList_ThrowIOException_OnFilenameNotExist() throws IOException {
        PartInfo partInfo = PartInfo.builder().build();

        List<FileInfo> partInfoList = Arrays.asList(partInfo);

        when(uploadFileReader.fileExists(anyString())).thenReturn(false);

        uploadService.getCurrentOffsets(TEST_FILENAME, partInfoList);
    }

    @Test
    public void mapToDirectoryPath_ReturnDirectoryPathString_OnNotNullFilename() throws IOException {
        when(uploadFileWriter.createDirectory(anyString())).thenReturn(TEST_FILEDIR);

        Assert.assertEquals(TEST_FILEDIR, uploadService.getDirectoryPath(TEST_FILENAME));
    }

    @Test(expected = IOException.class)
    public void mapToDirectoryPath_ThrowIOException_OnNullFilename() throws IOException {
        when(uploadFileWriter.createDirectory(null)).thenThrow(new IOException());

        uploadService.getDirectoryPath(null);
    }

    @Test
    public void mapToPartInfoWrittenBytes_ReturnPartInfoWithWrittenBytes_OnCompleteUpload() throws IOException {
        PartInfo partInfoInput = PartInfo.builder()
                .fileSize(5L)
                .fileName(TEST_FILENAME)
                .partNumber(0L)
                .offset(TEST_UPLOADOFFSET)
                .length(TEST_UPLOADLENGTH)
                .userName(TEST_USERNAME)
                .build();

        PartInfo partInfoOutput = PartInfo.builder()
                .fileSize(5L)
                .fileName(TEST_FILENAME)
                .partNumber(0L)
                .offset(partInfoInput.getOffset() + TEST_UPLOADOFFSET_INC_COMPLETE)
                .length(TEST_UPLOADLENGTH)
                .userName(TEST_USERNAME)
                .build();

        when(uploadFileWriter.write(partInfoInput)).thenReturn(partInfoOutput);
        when(uploadFileReader.isComplete(partInfoOutput)).thenReturn(true);

        Assert.assertEquals(TEST_UPLOADLENGTH, uploadService.write(partInfoInput).getOffset());
    }

    @Test(expected = IOException.class)
    public void mapToPartInfoWrittenBytes_ThrowIOException_OnIncompleteUpload() throws IOException {
        PartInfo partInfoInput = PartInfo.builder()
                .fileSize(5L)
                .fileName(TEST_FILENAME)
                .partNumber(0L)
                .offset(TEST_UPLOADOFFSET)
                .length(TEST_UPLOADLENGTH)
                .userName(TEST_USERNAME)
                .build();

        PartInfo partInfoOutput = PartInfo.builder()
                .fileSize(5L)
                .fileName(TEST_FILENAME)
                .partNumber(0L)
                .offset(partInfoInput.getOffset() + TEST_UPLOADOFFSET_INC)
                .length(5L)
                .userName(TEST_USERNAME)
                .build();

        when(uploadFileWriter.write(partInfoInput)).thenReturn(partInfoOutput);
        when(uploadFileReader.isComplete(partInfoOutput)).thenReturn(false);

        Assert.assertEquals((Long) 2L, uploadService.write(partInfoInput).getOffset());
    }

    @Test
    public void reduceToTotalBytesTransferred_ReturnLong_OnPartInfoList() throws IOException {
        FileInfo partInfoA = PartInfo.builder().fileSize(TEST_UPLOAD_PART_FILESIZE).build();
        FileInfo partInfoB = PartInfo.builder().fileSize(TEST_UPLOAD_PART_FILESIZE).build();
        FileInfo partInfoC = PartInfo.builder().fileSize(TEST_UPLOAD_PART_FILESIZE).build();

        List<FileInfo> partInfoList = Arrays.asList(partInfoA, partInfoB, partInfoC);

        Long sum = partInfoA.getFileSize() + partInfoB.getFileSize() + partInfoC.getFileSize();

        when(uploadFileWriter.concat(partInfoList)).thenReturn(sum);

        Assert.assertEquals((Long) (TEST_UPLOAD_PART_FILESIZE * 3), uploadService.concat(partInfoList));
    }
}