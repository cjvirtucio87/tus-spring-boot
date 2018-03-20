package com.tusspringboot.download.impl;

import java.io.FileInputStream;
import java.io.IOException;


import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;


import com.tusspringboot.download.api.DownloadService;
import com.tusspringboot.util.PathFactory;

@Service
public class DownloadServiceImpl implements DownloadService {
    public InputStreamResource stream( String fileName ) throws IOException {
        return new InputStreamResource ( new FileInputStream ( PathFactory.createFinalPath( fileName ).toString() ) );
    }
}
