package com.project.secureFileUpload.upload;

import java.util.List;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.io.OutputStream;
import java.util.Collections;
import java.lang.reflect.Field;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.ByteArrayOutputStream;

import com.google.api.services.drive.Drive;
import java.security.GeneralSecurityException;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.DriveScopes;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.model.FileList;
import com.project.secureFileUpload.Config;
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
public class GoogleDrive implements IUpload {
    private static final String APPLICATION_NAME = Config.getStr("applicationName");

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final String TOKENS_DIRECTORY_PATH = Config.getStr("tokensDirectoryPath");

    private static final List<String> SCOPES = getDriveScopes();

    private static List<String> getDriveScopes() {
        // Retrieve list of scope strings from configuration
        Object scopes = Config.getList("driveScopes");
        
        if (scopes instanceof List) {
            List<String> list = new ArrayList<>();
            
            for (Object element : (List<?>) scopes) {
                if (element instanceof String) {
                    String scope = (String) element;
                    
                    try {
                        Field field = DriveScopes.class.getField(scope);
                        String validScope = (String) field.get(null);

                        list.add(validScope);
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        // TODO: Add check for valid URLs
                        list.add(scope);
                    }
                }
            }
            
            return list;
        }
        
        return Collections.emptyList();
    }    

    private static final String CREDENTIALS_FILE_PATH = Config.getStr("credentialsFilePath");

    private final GoogleDriveIO driveIO;

    public GoogleDrive(GoogleDriveIO driveIO) {
        this.driveIO = driveIO;
    }

    public static GoogleDrive createWithDefaultTransport() {
        try {
            NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Drive service = GoogleDrive.initializeService(HTTP_TRANSPORT);
            return new GoogleDrive(new GoogleDriveIO(service));
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalStateException("[-] Failed to initialize Google Drive", e);
        }
    }

    @Override
    /**
     * Get list of all the files available on gdrive along with their name and id
     *
     * @return List of File fetched from drive
   */
    public List<File> getAllFiles() {
        return driveIO.getAllFiles();
    }

    @Override
    /**
     * @param absoluteFilePath 
     * @return ID of uploaded file if successful otherwise null
     */
    public String upload(String absoluteFilePath) {
        return driveIO.upload(absoluteFilePath);
    }

    @Override
    /**
     * @param fileId ID of file to be downloaded
     * @param fileName File name to save file as on disk
     * @return Success status of download and writing it to disk
     */
    public boolean download(String fileId, String fileName) {
        ByteArrayOutputStream downloadedFile = driveIO.download(fileId);

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
     * 
     * @param HTTP_TRANSPORT The network HTTP Transport
     * @return Drive object
     */
    private static Drive initializeService(NetHttpTransport HTTP_TRANSPORT) throws IOException {
        try {
            return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, GoogleDrive.getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (IOException e) {
            System.out.println("[-] Unable to initialize service: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 
     * @return Client secrets
     * @throws IOException
     */
    private static GoogleClientSecrets loadClientSecrets() throws IOException {
        try(InputStream in = GoogleDrive.class.getClassLoader().getResourceAsStream(CREDENTIALS_FILE_PATH)) {
            if (in == null) {
                throw new FileNotFoundException("[-] Resource not found: " + CREDENTIALS_FILE_PATH);
            }
            return GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        }
    }

    /**
     * Authorize user
     * 
     * @param HTTP_TRANSPORT The network HTTP Transport
     * @return Authorized Credential object
     * @throws IOException If credential.json file can't be found
     */
    public static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.

        GoogleClientSecrets clientSecrets;

        try {
            clientSecrets = GoogleDrive.loadClientSecrets();
        } catch (IOException e) {
            throw new IOException(String.format("[-] Failed to load client secrets from %s: %s", e.getMessage(), CREDENTIALS_FILE_PATH));
        }

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(Config.getInt("googleDriveAuthenticationPort")).build();

        try {
            Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

            // returns an authorized Credential object.
            return credential;
        } catch (IOException e) {
            throw new IOException("[-] Failed to authorize user: " + e.getMessage());
        }
    }
}

/**
 * Handle all IO operations for Google drive including file upload, download, listing, etc
 */
class GoogleDriveIO {
    private final Drive service;
    private final GoogleDriveUpload driveUploadObj;
    private final GoogleDriveDownload driveDownloadObj;

    /**
     * 
     * @param service
     */
    GoogleDriveIO(Drive service) {
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
            System.out.println("[-] Unable to upload/read file: " + e.getMessage());
        }

        return null;
    }
}

class GoogleDriveDownload {
    private final Drive service;

    /**
     * 
     * @param service
     */
    GoogleDriveDownload(Drive service) {
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