package com.eau.EncryptAndUpload.upload.gdrive;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

import com.google.api.services.drive.Drive;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

/**
 * Handles file download operations from Google Drive.
 * <p>
 * This class provides methods to download files from Google Drive using the Drive API.
 * </p>
 */
public class GoogleDriveDownload {
    private final Drive service;

    /**
     * Constructs a new {@code GoogleDriveDownload} with the specified Drive service.
     *
     * @param service the Google Drive API service
     */
    public GoogleDriveDownload(Drive service) {
        this.service = service;
    }

    /**
     * Downloads a file from Google Drive as a byte array stream.
     *
     * @param fileId the ID of the file to download
     * @return a {@link ByteArrayOutputStream} containing the file data, or {@code null} on failure
     */
    public ByteArrayOutputStream downloadFile(String fileId) {
        try {
            OutputStream outputStream = new ByteArrayOutputStream();

            this.service.files().get(fileId)
                .executeMediaAndDownloadTo(outputStream);

            return (ByteArrayOutputStream) outputStream;
        } catch (GoogleJsonResponseException e) {
            System.err.println("[-] Unable to move file: " + e.getDetails());
        } 
        catch (IOException e) {
            System.err.println("[-] Unable to download/write to file: " + e.getMessage());
        }
        return null;
    }
}
