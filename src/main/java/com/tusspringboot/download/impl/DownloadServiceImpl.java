package com.tusspringboot.download.impl;

import java.io.IOException;
import java.nio.file.Files;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;


import com.tusspringboot.download.api.DownloadService;
import com.tusspringboot.util.PathFactory;

@Service
public class DownloadServiceImpl implements DownloadService {
    public ByteArrayResource stream( String fileName ) throws IOException {
        return new ByteArrayResource ( Files.readAllBytes( PathFactory.createFinalPath( fileName ) ) );
    }
}
