package com.project.SecureFileUpload.Upload;

import java.util.List;
import java.io.InputStream;
import java.io.IOException;
import java.util.Collections;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import com.google.api.services.drive.Drive;
import java.security.GeneralSecurityException;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.DriveScopes;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.model.FileList;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;


/**
 * Wrapper class for performing GDrive related tasks
 */
public class GoogleDrive {
    private static final String APPLICATION_NAME = "Upload to cloud";

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final String TOKENS_DIRECTORY_PATH = "/tmp/tokens";

    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

    private static final String CREDENTIALS_FILE_PATH = "credentials.json";

    private final GoogleDriveIO driveIO;

    public GoogleDrive() {
        NetHttpTransport HTTP_TRANSPORT = null;
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            System.out.println("[-] Error: " + e);
        } catch (IOException e) {
            System.out.println("[-] Error: " + e);
        }

        Drive service = initializeService(HTTP_TRANSPORT);
        driveIO = new GoogleDriveIO(service);
    }

    /**
     * Get list of all the files available on gdrive along with their name and id
     *
     * @return List of File fetched from drive
   */
    public List<File> getAllFiles() {
        return driveIO.getAllFiles();
    }

    /**
     * 
     * @param HTTP_TRANSPORT The network HTTP Transport
     * @return Drive object
     */
    private Drive initializeService(NetHttpTransport HTTP_TRANSPORT) {
        try {
            return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (IOException e) {
            System.out.println("[-] Unable to initialize service " + e);
        }
        return null;
    }

    /**
     * Authorized user
     * 
     * @param HTTP_TRANSPORT The network HTTP Transport
     * @return Authorized Credential object
     * @throws IOException If credential.json file can't be found
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = GoogleDrive.class.getClassLoader().getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        // returns an authorized Credential object.
        return credential;
    }
}

/**
 * Handle all IO operations for Google drive including file upload, download, listing, etc
 */
class GoogleDriveIO {
    private final Drive service;
    private final GoogleDriveUpload driveUploadObj;

    /**
     * 
     * @param service
     */
    GoogleDriveIO(Drive service) {
        this.service = service;
        driveUploadObj = new GoogleDriveUpload(this.service);
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


    protected List<File> getAllFiles() {
        FileList result;
        List<File> files = null;
        try {
            result = this.service.files().list()
                    .setFields("nextPageToken, files(id, name)")
                    .execute();
        } catch (IOException e) {
            System.out.println("[-] Unable to read files " + e);
            return files;
        }

        files = result.getFiles();
        return files;
    }
}

/**
 * Core logic for uploading file (multipart, resumable upload, ...)
 */
class GoogleDriveUpload {
    private final Drive service;

    /**
     * 
     * @param service
     */
    GoogleDriveUpload(Drive service) {
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
        
        FileContent mediaContent = new FileContent("application/octet-stream", filePath);
        try {
            File file = service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            return file.getId();
        } catch (GoogleJsonResponseException e) {
            System.err.println("[-] Unable to upload file: " + e.getDetails());
        } catch (IOException e) {
            System.out.println("[-] Unable to read file: " + e);
        }

        return null;
    }
}