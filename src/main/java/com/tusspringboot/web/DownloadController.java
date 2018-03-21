package com.tusspringboot.web;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
           return onExists( downloadService.stream( fileName ), fileName );
       } catch (IOException e) {
           return onNotExist( fileName );
       }
    }
    
    private ResponseEntity onExists( ByteArrayResource bar, String fileName ) throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
                .header( "Content-Disposition", "attachment; filename=" + fileName)
                .contentType( MediaType.APPLICATION_OCTET_STREAM )
                .contentLength( bar.contentLength() )
                .body( bar );
    }

    private ResponseEntity onNotExist(String fileName) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("fileName", fileName)
                .body("No upload has been created for file, " + fileName + ", yet.");
    }    
}