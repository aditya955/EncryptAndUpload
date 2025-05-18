package com.eau.EncryptAndUpload.upload.gdrive;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;

import com.eau.EncryptAndUpload.builder.uploader.UploaderBuilder;
import com.eau.EncryptAndUpload.config.uploadConfig.UploaderConfig;
import com.eau.EncryptAndUpload.exceptions.InvalidConfigException;
import com.eau.EncryptAndUpload.upload.CloudUploader;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;

public class GoogleDriveBuilder implements UploaderBuilder {
    private UploaderConfig config;

    public GoogleDriveBuilder(UploaderConfig config) throws InvalidConfigException {
        this.config = config;
        validateConfig();
    }

    public CloudUploader build() {
        HttpRequestInitializer HTTP_REQUEST_INIT = getCredentials();

        Drive service = initializeService(HTTP_REQUEST_INIT);
        GoogleDriveIO gDriveIO = new GoogleDriveIO(service);
        GoogleDrive gDrive = new GoogleDrive(gDriveIO);
        return gDrive;
    }

    private <T> boolean isConfigValueOfValidType(String key, Class<T> expectedType) throws InvalidConfigException {
        try {
            config.get(key, expectedType);
            return true;
        } catch (ClassCastException e) {
            throw new InvalidConfigException(key);
        }
    }

    private <T, U> boolean isConfigValueOfValidSubType(String key, Class<T> expectedType, Class<U> subType) throws InvalidConfigException {
        T baseObj = config.get(key, expectedType);

        if (Collection.class.isAssignableFrom(expectedType) && subType != null) {
            Collection<?> collection = (Collection<?>) baseObj;
            for (Object element : collection) {
                if (!subType.isInstance(element)) {
                    throw new InvalidConfigException(key);
                }
            }
        }
        return true;
    }

    private void validateConfig() throws InvalidConfigException {
        for (GoogleDriveKeys entry: GoogleDriveKeys.values()) {
            isConfigValueOfValidType(entry.getKey(), entry.getType());

            if(entry.getSubType() != null) {
                isConfigValueOfValidSubType(entry.getKey(), entry.getType(), entry.getSubType());
            }
        }
    }

    /**
     * 
     * @param HTTP_TRANSPORT The network HTTP Transport
     * @return Drive object
     */
    private Drive initializeService(HttpRequestInitializer HTTP_REQUEST_INIT) {
        return new Drive.
                    Builder(config.get(GoogleDriveKeys.HTTP_TRANSPORT.getKey(), NetHttpTransport.class), 
                            config.get(GoogleDriveKeys.JSON_FACTORY.getKey(), JsonFactory.class), 
                            HTTP_REQUEST_INIT)
                    .setApplicationName(config.get(GoogleDriveKeys.APPLICATION_NAME.getKey(), String.class))
                    .build();
    }

    private GoogleClientSecrets loadClientSecrets() throws IOException {
        try(InputStream in = new FileInputStream(config.get(GoogleDriveKeys.CREDENTIALS_FILE_PATH.getKey(), String.class))) {
            return GoogleClientSecrets.load(config.get(GoogleDriveKeys.JSON_FACTORY.getKey(), JsonFactory.class), new InputStreamReader(in));
        }
    }

    public Credential getCredentials() {
        // Load client secrets.
        GoogleClientSecrets clientSecrets;

        try {
            clientSecrets = loadClientSecrets();
        } catch (IOException e) {
            System.out.println(String.format("[-] Failed to load client secrets from %s: %s", e.getMessage(), config.get(GoogleDriveKeys.CREDENTIALS_FILE_PATH.getKey(), String.class)));
            return null;
        }

        // Build flow and trigger user authorization request.
        try {
            @SuppressWarnings("unchecked")
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                config.get(GoogleDriveKeys.HTTP_TRANSPORT.getKey(), NetHttpTransport.class), config.get(GoogleDriveKeys.JSON_FACTORY.getKey(), JsonFactory.class), clientSecrets, config.get(GoogleDriveKeys.SCOPES.getKey(), List.class))
                    .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(config.get(GoogleDriveKeys.TOKENS_DIRECTORY_PATH.getKey(), String.class))))
                    .setAccessType("offline")
                    .build();
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(config.get(GoogleDriveKeys.AUTH_PORT.getKey(), Integer.class)).build();

            Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

            // returns an authorized Credential object.
            return credential;
        } catch (IOException e) {
            System.out.println("[-] Failed to authenticate user: " + e.getMessage());
            return null;
        }
    }
}