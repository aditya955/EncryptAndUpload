package com.eau.EncryptAndUpload.upload.gdrive;

import java.io.IOException;

import com.google.api.services.drive.Drive;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.File;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

/**
 * Core logic for uploading files to Google Drive (multipart, resumable upload, etc.).
 * <p>
 * This class provides methods to upload files to Google Drive using the Drive API.
 * </p>
 */
public class GoogleDriveUpload {
   private final Drive service;

   /**
    * Constructs a new {@code GoogleDriveUpload} with the specified Drive service.
    *
    * @param service the Google Drive API service
    */
   public GoogleDriveUpload(Drive service) {
       this.service = service;
   }

   /**
    * Performs a multipart upload (metadata and data in the same request) to Google Drive.
    *
    * @param absoluteFilePath the absolute path of the file to be uploaded
    * @return the ID of the uploaded file, or {@code null} if the upload fails
    */
   protected String uploadMultiPart(String absoluteFilePath) {
       File fileMetadata = new File();
       
       java.io.File filePath = new java.io.File(absoluteFilePath);
       fileMetadata.setName(filePath.getName());
       
    //    String mimeType = Files.probeContentType(Paths.get(absoluteFilePath));
       FileContent mediaContent = new FileContent("application/octet-stream", filePath);
       try {
           File file = service.files().create(fileMetadata, mediaContent)
                   .setFields("id")
                   .execute();
           return file.getId();
       } catch (GoogleJsonResponseException e) {
           System.err.println("[-] Unable to upload file: " + e.getDetails());
       } catch (IOException e) {
           System.out.println("[-] Unable to upload/read file: " + e.getMessage());
       }

       return null;
   }
}