package funWithRx;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    @PatchMapping("/upload")
    @ResponseBody
    String updateUpload() {
        return "Uploading...";
    }

    @RequestMapping(value="/destroy", method=RequestMethod.DELETE)
    @ResponseBody
    String destroyUpload() {
        return "Destroying upload.";
    }
}
