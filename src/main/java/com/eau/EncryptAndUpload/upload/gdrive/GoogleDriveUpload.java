package com.eau.EncryptAndUpload.upload.gdrive;

import java.io.IOException;

import com.google.api.services.drive.Drive;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.model.File;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

/**
* Core logic for uploading file (multipart, resumable upload, ...)
*/
public class GoogleDriveUpload {
   private final Drive service;

   /**
    * 
    * @param service
    */
   public GoogleDriveUpload(Drive service) {
       this.service = service;
   }

   /**
    * Perform multipart data (upload metadata and data in the same request)
    * @param absoluteFilePath Absolute path of file to be uploaded
    * @return ID of uploaded file
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