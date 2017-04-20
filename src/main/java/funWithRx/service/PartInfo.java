package funWithRx.service;

import lombok.Getter;

import java.io.InputStream;

/**
 * Created by cvirtucio on 4/20/2017.
 */
public class PartInfo {
    @Getter String fileName;
    @Getter Long partNumber;
    @Getter Long uploadLength;
    @Getter String userName;
    @Getter InputStream inputStream;

    public PartInfo(
            String fileName,
            Long partNumber,
            Long uploadLength,
            String userName,
            InputStream inputStream
    ) {
        this.fileName = fileName;
        this.partNumber = partNumber;
        this.uploadLength = uploadLength;
        this.userName = userName;
        this.inputStream = inputStream;
    }
}
