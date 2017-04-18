package funWithRx.service;

/**
 * Created by cvirtucio on 4/18/2017.
 */
public class FileInfo implements Info {
    private String id;
    private String fileName;
    private Long uploadLength;
    private Long fileOffset;

    @Override
    public void setFileID(String id) {
        this.id = id;
    }

    @Override
    public void setFilename(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void setUploadLength(Long uploadLength) {
        this.uploadLength = uploadLength;
    }

    @Override
    public
}
