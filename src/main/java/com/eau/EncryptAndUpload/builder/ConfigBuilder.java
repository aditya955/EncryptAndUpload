package com.eau.EncryptAndUpload.builder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import com.eau.EncryptAndUpload.config.upload.UploaderConfig;
import com.eau.EncryptAndUpload.upload.gdrive.GoogleDriveKeys;
import com.eau.EncryptAndUpload.upload.gdrive.GoogleDriveOptions;
import com.eau.EncryptAndUpload.util.config.ConfigLoader;
import com.eau.EncryptAndUpload.util.config.JsonConfig;
import com.eau.EncryptAndUpload.util.config.ExternalUploadConfig;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

public class ConfigBuilder {
    private String configFilePath;

    public ConfigBuilder(String configFilePath) {
        this.configFilePath = configFilePath;
    }

    public UploaderConfig build() throws StreamReadException, DatabindException, IOException, GeneralSecurityException {
        String[] splittedFilePath = configFilePath.split("\\.");
        String configType = splittedFilePath[splittedFilePath.length - 1];
        switch (configType) {
            case "json":
                return loadJsonConfig();
        
            default:
                return null;
        }
    }

    private UploaderConfig loadJsonConfig() throws StreamReadException, DatabindException, IOException, GeneralSecurityException {
        JsonConfig jsonConfig = ConfigLoader.getJsonConfig(configFilePath);
        
        List<ExternalUploadConfig> uploadConfig = jsonConfig.getUploadConfig();

        List<UploaderConfig> uploaderConfigs = new ArrayList<>();

        for(ExternalUploadConfig currConfig: uploadConfig) {
            GoogleDriveOptions driveOptions = (GoogleDriveOptions) currConfig.getOptions();
            UploaderConfig config = new UploaderConfig(currConfig.getProvider());

            config.set(GoogleDriveKeys.APPLICATION_NAME.getKey(), driveOptions.getApplicationName());
            config.set(GoogleDriveKeys.AUTH_PORT.getKey(), driveOptions.getAuthPort());
            config.set(GoogleDriveKeys.CREDENTIALS_FILE_PATH.getKey(), driveOptions.getCredsFilePath());
            config.set(GoogleDriveKeys.TOKENS_DIRECTORY_PATH.getKey(), driveOptions.getTokensDirPath());
            config.set(GoogleDriveKeys.SCOPES.getKey(), driveOptions.getScopes());

            NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            config.set(GoogleDriveKeys.HTTP_TRANSPORT.getKey(), HTTP_TRANSPORT);

            config.set(GoogleDriveKeys.JSON_FACTORY.getKey(), GsonFactory.getDefaultInstance());

            uploaderConfigs.add(config);
        }

        return uploaderConfigs.get(0);
    }
}
