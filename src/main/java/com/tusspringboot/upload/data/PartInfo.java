package com.tusspringboot.upload.data;

import java.io.InputStream;

import lombok.Builder;
import lombok.Value;

/**
 * Created by cvirtucio on 4/20/2017.
 */

@Builder
@Value
public class PartInfo {
    private String fileName;
    private Long partNumber;
    private Long uploadOffset;
    private Long uploadLength;
    private Long fileSize;
    private String userName;
    private InputStream inputStream;
}
