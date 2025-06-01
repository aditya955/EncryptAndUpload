package com.eau.EncryptAndUpload.upload.gdrive;

import java.util.List;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;

/**
 * Enum representing configuration keys and types for Google Drive integration.
 * <p>
 * Each enum constant defines a configuration key, its expected value type, and optionally a subtype
 * (for collections). These keys are used to configure Google Drive upload and authentication.
 * </p>
 */
public enum GoogleDriveKeys {
    /**
     * Application name for Google Drive API usage.
     */
    APPLICATION_NAME("applicationName", String.class),

    /**
     * Port for OAuth2 authentication callback.
     */
    AUTH_PORT("authPort", Integer.class),

    /**
     * Path to the credentials JSON file.
     */
    CREDENTIALS_FILE_PATH("credsFilePath", String.class),

    /**
     * HTTP transport implementation for Google API client.
     */
    HTTP_TRANSPORT("httpTransport", NetHttpTransport.class),

    /**
     * JSON factory for Google API client.
     */
    JSON_FACTORY("jsonFactory", JsonFactory.class),

    /**
     * List of OAuth2 scopes required for Google Drive access.
     */
    SCOPES("scopes", List.class),
    
    /**
     * Directory path for storing OAuth2 tokens.
     */
    TOKENS_DIRECTORY_PATH("tokensDirPath", String.class);

    private final static String keyPrefix = "googleDrive-";
    private final String key;
    private final Class<?> type;
    private Class<?> subType;

    /**
     * Constructs a new {@code GoogleDriveKeys} enum constant with the specified key and type.
     *
     * @param key the configuration key
     * @param type the expected value type
     */
    GoogleDriveKeys(String key, Class<?> type) { 
        this.key = keyPrefix + key;
        this.type = type;
        this.subType = null;
    }

    /**
     * Constructs a new {@code GoogleDriveKeys} enum constant with the specified key, type, and subtype.
     *
     * @param key the configuration key
     * @param type the expected value type
     * @param subType the expected subtype (for collections)
     */
    GoogleDriveKeys(String key, Class<?> type, Class<?> subType) { 
        this(key, type);
        this.subType = subType;
    }

    /**
     * Returns the full configuration key (with prefix {@value #keyPrefix}).
     *
     * @return the configuration key
     */
    public String getKey() { return key; }

    /**
     * Returns the expected value type for this key.
     *
     * @return the value type
     */
    public Class<?> getType() { return type; }

    /**
     * Returns the expected subtype for this key (if applicable).
     *
     * @return the value subtype, or {@code null} if not applicable
     */
    public Class<?> getSubType() { return subType; }
}
