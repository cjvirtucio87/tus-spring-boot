package funWithRx;

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
    @GetMapping("/")
    @ResponseBody
    String home() {
        return "index";
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
