package com.tusspringboot.upload.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.io.InputStream;

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
