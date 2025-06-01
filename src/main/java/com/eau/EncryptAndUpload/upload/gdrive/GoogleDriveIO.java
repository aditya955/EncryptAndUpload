package com.eau.EncryptAndUpload.upload.gdrive;


import java.util.List;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

/**
 * Handles all I/O operations for Google Drive, including file upload, download, and listing.
 * <p>
 * This class acts as a facade for Google Drive API operations, delegating upload and download
 * functionality to {@link GoogleDriveUpload} and {@link GoogleDriveDownload} helpers.
 * </p>
 */
public class GoogleDriveIO {
    private final Drive service;
    private final GoogleDriveUpload driveUploadObj;
    private final GoogleDriveDownload driveDownloadObj;

    /**
     * Constructs a new {@code GoogleDriveIO} with the specified Google Drive service instance.
     *
     * @param service the Google Drive API service
     */
    public GoogleDriveIO(Drive service) {
        this.service = service;
        driveUploadObj = new GoogleDriveUpload(this.service);
        driveDownloadObj = new GoogleDriveDownload(this.service);
    }

    /**
     * Uploads the local file at the specified path to Google Drive.
     *
     * @param absoluteFilePath the absolute path of the file to be uploaded
     * @return the ID of the uploaded file
     */
    protected String upload(String absoluteFilePath) {
        return driveUploadObj.uploadMultiPart(absoluteFilePath);
    }

    /**
     * Retrieves a list of all files from Google Drive.
     *
     * @return a list of files fetched from Google Drive
     */
    protected List<File> getAllFiles() {
        FileList result;
        List<File> files = null;
        try {
            result = this.service.files().list()
                    .setFields("nextPageToken, files(id, name)")
                    .execute();
            files = result.getFiles();
        } catch (IOException e) {
            System.out.println("[-] Unable to upload/read files " + e.getMessage());
        }

        return files;
    }

    /**
     * Downloads a file from Google Drive as a byte array stream.
     *
     * @param fileId the ID of the file to download
     * @return a {@link ByteArrayOutputStream} containing the file data, or {@code null} on failure
     */
    protected ByteArrayOutputStream download(String fileId) {
        return driveDownloadObj.downloadFile(fileId);
    }
}

