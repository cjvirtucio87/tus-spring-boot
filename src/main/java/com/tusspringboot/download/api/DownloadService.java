package com.tusspringboot.download.api;

import java.io.IOException;


import org.springframework.core.io.InputStreamResource;

public interface DownloadService {
    public InputStreamResource stream( String fileName ) throws IOException;
}
