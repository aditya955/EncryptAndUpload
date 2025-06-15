package com.eau.EncryptAndUpload.config.upload;

import com.eau.EncryptAndUpload.builder.uploader.UploaderBuilder;
import com.eau.EncryptAndUpload.config.BaseConfig;
import com.eau.EncryptAndUpload.enums.CloudProvider;
import com.eau.EncryptAndUpload.exceptions.InvalidConfigException;
import com.eau.EncryptAndUpload.exceptions.InvalidProviderException;
import com.eau.EncryptAndUpload.upload.gdrive.GoogleDriveBuilder;

/**
 * Configuration class for cloud upload providers.
 * <p>
 * This class encapsulates the provider type and configuration options required to
 * build a cloud uploader for a specific provider (e.g., Google Drive). It provides
 * methods to set and retrieve configuration values, and to obtain a suitable
 * {@link UploaderBuilder} for the configured provider.
 * </p>
 */
public class UploaderConfig extends BaseConfig {
    private final CloudProvider provider;

    /**
     * Constructs a new {@code UploaderConfig} for the specified provider.
     *
     * @param provider the cloud provider for this configuration
     */
    public UploaderConfig(CloudProvider provider) {
        this.provider = provider;
    }

    /**
     * Returns a suitable {@link UploaderBuilder} for the configured provider.
     *
     * @return an uploader builder for the provider
     * @throws InvalidConfigException if the configuration is invalid
     * @throws InvalidProviderException if the provider is not supported
     */
    public UploaderBuilder getBuilder() throws InvalidConfigException, InvalidProviderException { 
        switch (provider) {
            case GOOGLE_DRIVE:
                return new GoogleDriveBuilder(this);
            default:
                throw new InvalidProviderException(provider.name());
        }
    }
}
