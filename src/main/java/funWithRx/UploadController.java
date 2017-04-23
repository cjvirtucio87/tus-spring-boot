package funWithRx;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonFactory;
import funWithRx.service.PartInfo;
import funWithRx.service.UploadUtil;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rx.Observable;
import rx.observables.StringObservable;

import java.io.InputStream;
import java.util.Optional;

/**
 * Created by cvirtucio on 4/18/2017.
 */
@Controller
@EnableAutoConfiguration
public class UploadController {

    @RequestMapping(value="/upload", method=RequestMethod.HEAD)
    @ResponseBody
    String readUpload() {
        return "Checking if the upload exists..";
    }

    @PostMapping("/upload")
    @ResponseBody
    String createUpload() {
        return "Creating an upload.";
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PatchMapping("/upload/{id}")
    @ResponseBody
    String updateUpload(
            @RequestHeader(name="fileName") String fileName,
            @RequestHeader(name="partNumber") Long partNumber,
            @RequestHeader(name="uploadLength") Long uploadLength,
            @RequestHeader(name="userName") String userName,
            InputStream inputStream
    ) {
        PartInfo partInfo = new PartInfo(fileName, partNumber, uploadLength, userName, inputStream);

        Optional.of(partInfo)
                .map(UploadUtil::writeFilePart)
                .map(UploadUtil.checkIfComplete(partInfo))
                .get();

        return "Uploading...";
    }

    @RequestMapping(value="/destroy", method=RequestMethod.DELETE)
    @ResponseBody
    String destroyUpload() {
        return "Destroying upload.";
    }
}
