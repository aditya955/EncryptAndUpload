package com.eau.EncryptAndUpload.upload;

import java.util.List;

import com.google.api.services.drive.model.File;

/**
 * Interface representing a cloud uploader capable of uploading, downloading, and listing files.
 * <p>
 * Implementations of this interface should provide concrete logic for interacting with
 * specific cloud storage providers (e.g., Google Drive, Dropbox, etc.).
 * </p>
 */
public interface CloudUploader {
    /**
     * Retrieves a list of all files available in the cloud storage.
     *
     * @return a list of files from the cloud provider
     */
    public List<File> getAllFiles();

    /**
     * Uploads a file to the cloud storage.
     *
     * @param absoluteFilePath the absolute path of the file to upload
     * @return the ID or reference of the uploaded file
     */
    public String upload(String absoluteFilePath);

    /**
     * Downloads a file from the cloud storage.
     *
     * @param fileId   the ID of the file to download
     * @param fileName the local file name to save the downloaded content
     * @return {@code true} if the download was successful, {@code false} otherwise
     */
    public boolean download(String fileId, String fileName);
}
