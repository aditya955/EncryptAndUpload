package com.eau.EncryptAndUpload.upload.gdrive;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.eau.EncryptAndUpload.upload.CloudUploader;
import com.google.api.services.drive.model.File;

public class GoogleDrive implements CloudUploader {
    private GoogleDriveIO gDriveIO;
    public GoogleDrive(GoogleDriveIO gDriveIO) {
        this.gDriveIO = gDriveIO;
    }

    @Override
    public String upload(String absoluteFilePath) {
        return gDriveIO.upload(absoluteFilePath);
    }

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

    @Override
    public List<File> getAllFiles() {
        return gDriveIO.getAllFiles();
    }
}
