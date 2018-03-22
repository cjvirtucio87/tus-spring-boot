package com.tusspringboot.download.api;

import java.io.IOException;


import org.springframework.core.io.ByteArrayResource;

public interface DownloadService {
    public ByteArrayResource stream( String fileName, String fileExt ) throws IOException;
}
