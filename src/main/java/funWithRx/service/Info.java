package funWithRx.service;

/**
 * Created by cvirtucio on 4/18/2017.
 */
public interface Info {
    /**
     * A service will generate a base64 string as the ID representing the file. This will be the file name of the temp file being built.
     * @return a base64 string
     */
    public String getFileID();

    /**
     * The original filename will be used to create FileInfo objects. This filename, together with the file size and user name, will be the components that make up the key in the
     * service's Map store.
     * @return a String representing the original file name
     */
    public String getFilename();

    /**
     * The file will be uploaded concurrently in chunks. Upload length represents the upper bound for a specific chunk.
     * @return a Long representing the upper bound of the chunk to be uploaded
     */
    public Long getUploadLength();

    /**
     * The file will be uploaded concurrently in chunks. File offset represents the lower bound for a specific chunk.
     * @return a Long representing the lower bound of the chunk to be uploaded
     */
    public Long getFileOffset();

    /**
     * The client is expected to pass a string containing metadata about the file to be uploaded. This metadata will consist of comma separated key/value pairs, with the mapping represente by a space. This method returns a value based on the specified key.
     * @return a String representing the metadata passed by the client
     */
    public String getMetadatum(String key);
}