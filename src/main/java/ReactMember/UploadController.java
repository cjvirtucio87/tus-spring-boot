package ReactMember;

import ReactMember.service.PartInfo;
import ReactMember.service.UploadUtil;
import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by cvirtucio on 4/18/2017.
 */
@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = { "fileDir", "fileName", "uploadOffset", "partNumber", "filePointer"})
@Controller
@EnableAutoConfiguration
public class UploadController {
    private static final Logger log = Logger.getLogger(UploadController.class);

    @RequestMapping(value="/upload", method=RequestMethod.HEAD)
    @ResponseBody
    ResponseEntity checkUpload(
            @RequestHeader(name="fileName") String fileName,
            @RequestHeader(name="partNumbers") List<Long> partNumbers
    ) {
        List<PartInfo> partInfoList = partNumbers.stream()
                .map(num -> PartInfo.builder().fileName(fileName).partNumber(num).build())
                .collect(Collectors.toList());

        return Optional.of(fileName)
                .filter(UploadUtil::checkIfExists)
                .map(name -> partInfoList.stream().map(UploadUtil::getFilePointer).collect(Collectors.toList()))
                .map(this::onExists)
                .orElseGet(onNotExist(fileName));
    }

    @PostMapping("/upload")
    @ResponseBody
    ResponseEntity createUpload(
            @RequestHeader(name="fileName") String fileName
    ) {
        return Optional.of(fileName)
                .map(UploadUtil::createDirectory)
                .map(this::onCreateDir)
                .get();
    }

    @GetMapping("/upload/{id}")
    @ResponseBody
    ResponseEntity readUpload(
            @RequestHeader(name="fileName") String fileName,
            @RequestHeader(name="partNumber") Long partNumber,
            @RequestHeader(name="userName") String userName
    ) {
        PartInfo partInfo = PartInfo.builder()
                .fileName(fileName)
                .partNumber(partNumber)
                .userName(userName)
                .build();

        return Optional.of(partInfo)
                .map(UploadUtil::getFilePointer)
                .map(this::onRead)
                .get();
    }


    @PatchMapping("/upload/{id}")
    @ResponseBody
    ResponseEntity updateUpload(
            @RequestHeader(name="fileName") String fileName,
            @RequestHeader(name="partNumber") Long partNumber,
            @RequestHeader(name="uploadOffset") Long uploadOffset,
            @RequestHeader(name="uploadLength") Long uploadLength,
            @RequestHeader(name="userName") String userName,
            InputStream inputStream
    ) {
        PartInfo partInfo = PartInfo
                .builder()
                .fileName(fileName)
                .partNumber(partNumber)
                .uploadOffset(uploadOffset)
                .uploadLength(uploadLength)
                .userName(userName)
                .inputStream(inputStream)
                .build();

        return Optional.of(partInfo)
                .map(UploadUtil::writeFilePart)
                .filter(UploadUtil.checkIfComplete(partInfo))
                .map(onComplete(partInfo))
                .orElseGet(onInterrupt(partInfo));
    }

    @PostMapping("/upload/complete")
    @ResponseBody
    ResponseEntity completeUpload(
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
    @ResponseBody
    String destroyUpload() {
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

    private ResponseEntity onRead(Long filePointer) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("filePointer", filePointer.toString());
        return new ResponseEntity<>(
                "",
                responseHeaders,
                HttpStatus.OK
        );
    }

    private ResponseEntity onCreateDir(String fileDir) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("fileDir", fileDir);
        return new ResponseEntity<>(
                "",
                responseHeaders,
                HttpStatus.CREATED
        );
    }

    private ResponseEntity onExists(List<Long> filePointerList) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<>(
                filePointerList,
                responseHeaders,
                HttpStatus.OK
        );
    }

    private Supplier<ResponseEntity> onNotExist(String fileName) {
        return () -> {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("fileName", fileName);
            return new ResponseEntity<>(
                    "No upload has been created for file, " + fileName + ", yet.",
                    responseHeaders,
                    HttpStatus.BAD_REQUEST
            );
        };
    }

    private Function<Long, ResponseEntity> onComplete(PartInfo partInfo) {
        return newOffset -> {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("partNumber", partInfo.getPartNumber().toString());
            return new ResponseEntity<>(
                    "",
                    responseHeaders,
                    HttpStatus.CREATED
            );
        };
    }

    private Supplier<ResponseEntity> onInterrupt(PartInfo partInfo) {
        return () -> {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("partNumber", partInfo.getPartNumber().toString());
            return new ResponseEntity<>(
                    "",
                    responseHeaders,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        };
    }
}
