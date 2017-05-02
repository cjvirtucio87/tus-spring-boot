package ReactMember.service;

import lombok.Getter;

import java.io.InputStream;

/**
 * Created by cvirtucio on 4/20/2017.
 */
public class PartInfo {
    @Getter String fileName;
    @Getter Long partNumber;
    @Getter Long uploadOffset;
    @Getter Long uploadLength;
    @Getter String userName;
    @Getter InputStream inputStream;

    public PartInfo(
            String fileName,
            Long partNumber,
            Long uploadOffset,
            Long uploadLength,
            String userName,
            InputStream inputStream
    ) {
        this.fileName = fileName;
        this.partNumber = partNumber;
        this.uploadOffset = uploadOffset;
        this.uploadLength = uploadLength;
        this.userName = userName;
        this.inputStream = inputStream;
    }
}
