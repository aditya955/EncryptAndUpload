package com.eau.EncryptAndUpload.upload.gdrive;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.eau.EncryptAndUpload.upload.CloudUploader;
import com.google.api.services.drive.model.File;

/**
 * Google Drive implementation of the {@link CloudUploader} interface.
 * <p>
 * Provides methods to upload, download, and list files on Google Drive using the Google Drive API.
 * This class delegates actual I/O operations to a {@link GoogleDriveIO} instance.
 * </p>
 */
public class GoogleDrive implements CloudUploader {
    private GoogleDriveIO gDriveIO;

    /**
     * Constructs a new {@code GoogleDrive} uploader with the specified I/O handler.
     *
     * @param gDriveIO the Google Drive I/O handler
     */
    public GoogleDrive(GoogleDriveIO gDriveIO) {
        this.gDriveIO = gDriveIO;
    }

    /**
     * Uploads a file to Google Drive.
     *
     * @param absoluteFilePath the absolute path of the file to upload
     * @return the ID of the uploaded file
     */
    @Override
    public String upload(String absoluteFilePath) {
        return gDriveIO.upload(absoluteFilePath);
    }

    /**
     * Downloads a file from Google Drive and saves it locally.
     *
     * @param fileId the ID of the file to download
     * @param fileName the local file name to save the downloaded content
     * @return {@code true} if the download was successful, {@code false} otherwise
     */
    @Override
    public boolean download(String fileId, String fileName) {
        ByteArrayOutputStream downloadedFile = gDriveIO.download(fileId);

        if(downloadedFile == null) {
            return false;
        }

        try(OutputStream outputStream = new FileOutputStream(fileName)) {
            downloadedFile.writeTo(outputStream);
            return true;
        } catch (IOException e) {
            System.out.println("[-] Unable to download/write to file '" + fileName + "': " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a list of all files available in Google Drive.
     *
     * @return a list of files from Google Drive
     */
    @Override
    public List<File> getAllFiles() {
        return gDriveIO.getAllFiles();
    }
}
