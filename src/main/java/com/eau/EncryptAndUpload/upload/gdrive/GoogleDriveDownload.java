package com.eau.EncryptAndUpload.upload.gdrive;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

import com.google.api.services.drive.Drive;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

public class GoogleDriveDownload {
    private final Drive service;

    /**
     * 
     * @param service
     */
    public GoogleDriveDownload(Drive service) {
        this.service = service;
    }

    /**
     * Download a Document file in PDF format.
     *
     * @param realFileId file ID of any workspace document format file.
     * @return byte array stream if successful, {@code null} otherwise.
     * @throws IOException if service account credentials file not found.
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
