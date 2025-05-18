package com.eau.EncryptAndUpload.upload.gdrive;

import java.util.List;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;

public enum GoogleDriveKeys {
    APPLICATION_NAME("applicationName", String.class),
    AUTH_PORT("authPort", Integer.class),
    CREDENTIALS_FILE_PATH("credsFilePath", String.class),
    HTTP_TRANSPORT("httpTransport", NetHttpTransport.class),
    JSON_FACTORY("jsonFactory", JsonFactory.class),
    SCOPES("scopes", List.class),
    TOKENS_DIRECTORY_PATH("tokensDirPath", String.class);

    private final String keyPrefix = "googleDrive-";
    private final String key;
    private final Class<?> type;
    private Class<?> subType;

    GoogleDriveKeys(String key, Class<?> type) { 
        this.key = keyPrefix + key;
        this.type = type;
        this.subType = null;
    }

    GoogleDriveKeys(String key, Class<?> type, Class<?> subType) { 
        this(key, type);
        this.subType = subType;
    }

    public String getKey() { return key; }
    public Class<?> getType() { return type; }
    public Class<?> getSubType() { return subType; }
}
