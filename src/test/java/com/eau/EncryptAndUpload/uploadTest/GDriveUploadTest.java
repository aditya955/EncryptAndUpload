package com.eau.EncryptAndUpload.uploadTest;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.eau.EncryptAndUpload.builder.uploader.UploaderServiceBuilder;
import com.eau.EncryptAndUpload.config.upload.UploaderConfig;
import com.eau.EncryptAndUpload.enums.CloudProvider;
import com.eau.EncryptAndUpload.upload.CloudUploader;
import com.eau.EncryptAndUpload.upload.gdrive.GoogleDriveKeys;
import com.eau.EncryptAndUpload.utils.config.AppConfig;
import com.eau.EncryptAndUpload.utils.config.ConfigLoader;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

public class GDriveUploadTest {
    // private final String uploadFilePath = "/tmp/fileToUpload.txt";

    private void testUpload(UploaderConfig config) {
        try {
            // --------------- Uploader service ---------------
            UploaderServiceBuilder uploaderBuilder = new UploaderServiceBuilder(config);

            CloudUploader uploader = uploaderBuilder.build();

            if(uploader == null) {
                fail("Unable to create CloudUploader");
                return;
            }

            // --------------- Upload file ---------------
            // uploader.upload(uploadFilePath);

            // --------------- List Files ---------------
            List<File> files = uploader.getAllFiles();

            System.out.println("Sr No.\t\tFile Name\t\t\tFile ID");
            for(int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                System.out.println((i + 1) + "\t\t" + file.getName() + "\t\t\t" + file.getId());
            }
        } catch (Exception e) {
            fail("Unable to list files: " + e);
        }
    }

    @Test
    public void uploadTest() {
        try {
            // --------------- Config ---------------
            UploaderConfig config = new UploaderConfig(CloudProvider.GOOGLE_DRIVE);

            config.set(GoogleDriveKeys.APPLICATION_NAME.getKey(), "EncryptAndUpload-Test");
            
            config.set(GoogleDriveKeys.AUTH_PORT.getKey(), 8888);

            config.set(GoogleDriveKeys.CREDENTIALS_FILE_PATH.getKey(), "local-config/credentials.json");

            NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            config.set(GoogleDriveKeys.HTTP_TRANSPORT.getKey(), HTTP_TRANSPORT);

            config.set(GoogleDriveKeys.JSON_FACTORY.getKey(), GsonFactory.getDefaultInstance());

            config.set(GoogleDriveKeys.SCOPES.getKey(), Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY, DriveScopes.DRIVE_FILE, DriveScopes.DRIVE));

            config.set(GoogleDriveKeys.TOKENS_DIRECTORY_PATH.getKey(), "local-config");

            this.testUpload(config);
        } catch (Exception e) {
            fail("Unable to upload to cloud: " + e);
        }
    }

    @Test
    public void testConfigFileForUpload() {
        try {
            AppConfig config = ConfigLoader.getPropertiesConfig("local-config/config.properties");
            UploaderConfig uploaderConfig = config.getUploaderConfig();

            NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            uploaderConfig.set(GoogleDriveKeys.HTTP_TRANSPORT.getKey(), HTTP_TRANSPORT);

            uploaderConfig.set(GoogleDriveKeys.JSON_FACTORY.getKey(), GsonFactory.getDefaultInstance());

            uploaderConfig.set(GoogleDriveKeys.SCOPES.getKey(), Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY, DriveScopes.DRIVE_FILE, DriveScopes.DRIVE));

            this.testUpload(uploaderConfig);
        } catch (Exception e) {
            fail("Unable to load from properties file: " + e);
        }
    }
}
