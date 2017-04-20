package funWithRx;

import funWithRx.service.PartInfo;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rx.Observable;
import rx.observables.StringObservable;

import java.io.InputStream;

/**
 * Created by cvirtucio on 4/18/2017.
 */
@Controller
@EnableAutoConfiguration
public class UploadController {
    @GetMapping("/")
    @ResponseBody
    String home() {
        return "Hello, world!";
    }

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

    @PatchMapping("/upload/{id}")
    @ResponseBody
    String updateUpload(
            String fileName,
            Long partNumber,
            Long uploadLength,
            String userName,
            InputStream inputStream
    ) {
        PartInfo partInfo = new PartInfo(fileName, partNumber, uploadLength, userName);
        StringObservable.from(inputStream)
            .flatMap;
        return "Uploading...";
    }

    @RequestMapping(value="/destroy", method=RequestMethod.DELETE)
    @ResponseBody
    String destroyUpload() {
        return "Destroying upload.";
    }
}
