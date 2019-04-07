package com.tusspringboot.upload.api;

/**
 * A temporary staging area for all files.
 */
public interface StagingArea {

    /**
     * Retrieve the absolute path to the file or directory in the staging area, including the
     * staging area itself.
     * 
     * @param target the relative path to the file or directory.
     * @return the absolute path to the file or directory within the staging area.
     */
    public Path path(Path target);

    /**
     * Write a stream into the specified target within the staging area. 
     * 
     * @param stream the input stream that will be written to the specified target in the staging area.
     */
    public void write(Stream stream, String target);

    public void 
}