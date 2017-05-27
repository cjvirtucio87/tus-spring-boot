package TusSpringBoot;

import TusSpringBoot.service.PartInfo;
import TusSpringBoot.service.UploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by cvirtucio on 4/18/2017.
 */
@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = { "fileDir", "fileName", "uploadOffset", "partNumber", "reason"})
@Controller
@RequestMapping("/upload")
@Slf4j
public class UploadController {

    @GetMapping("/file/{id}")
    public ResponseEntity readUpload(
            @RequestHeader(name="fileName") String fileName,
            @RequestHeader(name="partNumbers") List<Long> partNumbers
    ) {
        List<PartInfo> partInfoList = partNumbers.stream()
                .map(num -> PartInfo.builder().fileName(fileName).partNumber(num).build())
                .collect(Collectors.toList());

        return Optional.of(fileName)
                .filter(UploadUtil::checkIfExists)
                .map(name -> partInfoList.stream().map(UploadUtil::getCurrentOffset).collect(Collectors.toList()))
                .map(this::onExists)
                .orElseGet(onNotExist(fileName));
    }

    @PostMapping("/files")
    public ResponseEntity createUpload(
            @RequestHeader(name="fileName") String fileName
    ) {
        return Optional.of(fileName)
                .map(UploadUtil::createDirectory)
                .map(this::onCreateDir)
                .get();
    }

    @PatchMapping("/file/{id}")
    public ResponseEntity updateUpload(
            @RequestHeader(name="fileName") String fileName,
            @RequestHeader(name="partNumber") Long partNumber,
            @RequestHeader(name="uploadOffset") Long uploadOffset,
            @RequestHeader(name="uploadLength") Long uploadLength,
            @RequestHeader(name="fileSize") Long fileSize,
            @RequestHeader(name="userName") String userName,
            InputStream inputStream
    ) {
        PartInfo partInfo = PartInfo
                .builder()
                .fileSize(fileSize)
                .fileName(fileName)
                .partNumber(partNumber)
                .uploadOffset(uploadOffset)
                .uploadLength(uploadLength)
                .userName(userName)
                .inputStream(inputStream)
                .build();

        return Optional.of(partInfo)
                .map(UploadUtil::writeFilePart)
                .filter(UploadUtil::checkIfComplete)
                .map(this::onComplete)
                .orElseGet(onInterrupt(partInfo));
    }

    @PostMapping("/files/complete")
    public ResponseEntity completeUpload(
            @RequestHeader(name="fileName") String fileName,
            @RequestHeader(name="partNumbers") List<Long> partNumbers,
            @RequestHeader(name="fileSize") Long fileSize
    ) {
        List<PartInfo> partInfoList = partNumbers.stream()
                .map(partNumber -> PartInfo.builder().fileName(fileName).partNumber(partNumber).fileSize(fileSize).build())
                .collect(Collectors.toList());

        return Optional.of(partInfoList)
                .map(UploadUtil::concatenate)
                .map(this::onConcatenate)
                .orElseGet(this::onFailedConcatenate);
    }

    @RequestMapping(value="/destroy", method=RequestMethod.DELETE)
    public String destroyUpload() {
        return "Destroying upload.";
    }

    private ResponseEntity onConcatenate(String fileName) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("fileName", fileName)
                .build();
    }

    private ResponseEntity onFailedConcatenate() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("reason", "Failed to concatenate file.")
                .build();
    }

    private ResponseEntity onCreateDir(String fileDir) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("fileDir", fileDir)
                .build();
    }

    private ResponseEntity onExists(List<Long> currentOffsetList) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(currentOffsetList);
    }

    private Supplier<ResponseEntity> onNotExist(String fileName) {
        return () -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("fileName", fileName)
                .body("No upload has been created for file, " + fileName + ", yet.");
    }

    private ResponseEntity onComplete(PartInfo partInfo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("newOffset", partInfo.getUploadOffset().toString())
                .build();
    }

    private Supplier<ResponseEntity> onInterrupt(PartInfo partInfo) {
        return () -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("partNumber", partInfo.getPartNumber().toString())
                .build();
    }
}
