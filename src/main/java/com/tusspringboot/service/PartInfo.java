package com.tusspringboot.service;

import lombok.Builder;
import lombok.Getter;

import java.io.InputStream;

/**
 * Created by cvirtucio on 4/20/2017.
 */

@Builder
public class PartInfo {
    @Getter String fileName;
    @Getter Long partNumber;
    @Getter Long uploadOffset;
    @Getter Long uploadLength;
    @Getter Long fileSize;
    @Getter String userName;
    @Getter InputStream inputStream;
}
