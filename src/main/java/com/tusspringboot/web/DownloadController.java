package com.tusspringboot.web;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;


import com.tusspringboot.download.api.DownloadService;

@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = { "fileName" })
@Controller
@RequestMapping("/download")
public class DownloadController {
    
    @Autowired
    private DownloadService downloadService;
        
    @GetMapping("/file")
    public ResponseEntity getDownload(  
            @RequestHeader(name="fileName") String fileName
    ) {
       try {
           return onExists( downloadService.stream( fileName ) );
       } catch (IOException e) {
           return onNotExist( fileName );
       }
    }
    
    private ResponseEntity onExists( InputStreamResource isr ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body( isr );
    }

    private ResponseEntity onNotExist(String fileName) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("fileName", fileName)
                .body("No upload has been created for file, " + fileName + ", yet.");
    }    
}