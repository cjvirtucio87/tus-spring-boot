package funWithRx.service;

/**
 * Created by cvirtucio on 4/18/2017.
 */
public interface Info {
    // getters
    public String getFileID();
    public String getFilename();
    public Long getUploadLength();
    public Long getFileOffset();

    // setters
    public void setFileID(String id);
    public void setFilename(String fileName);
    public void setUploadLength(Long uploadLength);
    public void setFileOffset(Long fileOffset);
}