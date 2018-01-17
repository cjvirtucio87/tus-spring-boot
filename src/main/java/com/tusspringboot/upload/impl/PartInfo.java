package com.tusspringboot.upload.impl;

import java.io.InputStream;

import com.tusspringboot.upload.api.FileInfo;

import lombok.Builder;
import lombok.Value;

/**
 * Created by cvirtucio on 4/20/2017.
 */

@Builder
@Value
public class PartInfo implements FileInfo {
    private String fileName;
    private Long partNumber;
    private Long offset;
    private Long length;
    private Long fileSize;
    private String userName;
    private InputStream inputStream;
}
