package com.eau.EncryptAndUpload.upload.gdrive;


import java.util.List;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

/**
 * Handle all IO operations for Google drive including file upload, download, listing, etc
 */
public class GoogleDriveIO {
    private final Drive service;
    private final GoogleDriveUpload driveUploadObj;
    private final GoogleDriveDownload driveDownloadObj;

    /**
     * 
     * @param service
     */
    public GoogleDriveIO(Drive service) {
        this.service = service;
        driveUploadObj = new GoogleDriveUpload(this.service);
        driveDownloadObj = new GoogleDriveDownload(this.service);
    }

    /**
     * Upload the file at specified path to google drive
     * 
     * @param absoluteFilePath Absolute path of file to be uploaded
     * @return ID of the uploaded file
     */
    protected String upload(String absoluteFilePath) {
        return driveUploadObj.uploadMultiPart(absoluteFilePath);
    }


    /**
     * 
     * @return List of File fetched from drive
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
     * 
     * @param fileId ID of file to download
     * @return
     */
    protected ByteArrayOutputStream download(String fileId) {
        return driveDownloadObj.downloadFile(fileId);
    }
}

