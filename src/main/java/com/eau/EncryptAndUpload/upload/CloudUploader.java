package com.eau.EncryptAndUpload.upload;

import java.util.List;

import com.google.api.services.drive.model.File;

public interface CloudUploader {
    public List<File> getAllFiles();
    public String upload(String absoluteFilePath);
    public boolean download(String fileId, String fileName);
}
