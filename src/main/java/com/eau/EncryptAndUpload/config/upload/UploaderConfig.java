package com.eau.EncryptAndUpload.config.upload;

import java.util.HashMap;
import java.util.Map;

import com.eau.EncryptAndUpload.builder.uploader.UploaderBuilder;
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
public class UploaderConfig {
    private final CloudProvider provider;

    /**
     * Internal configuration map holding key-value pairs for provider-specific options.
     */
    private Map<String, Object> config = new HashMap<>();

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

    /**
     * Sets a configuration value for the given key.
     *
     * @param key the configuration key
     * @param value the value to set
     * @return this config instance for method chaining
     */
    public UploaderConfig set(String key, Object value) { 
        config.put(key, value);
        return this;
    }

    /**
     * Retrieves a configuration value for the given key and type.
     *
     * @param <T> the expected type of the value
     * @param key the configuration key
     * @param type the class of the expected value type
     * @return the configuration value cast to the specified type
     * @throws ClassCastException if the value is not of the expected type
     */
    public <T> T get(String key, Class<T> type) {
        Object value = config.get(key);

        try {
            if(type == Integer.class) {
                value = Integer.parseInt(value.toString());
            }
        } catch (Exception e) {
            throw new ClassCastException("Value for key '" + key + "' is not of type " + type.getSimpleName());
        }

        if (!type.isInstance(value)) {
            throw new ClassCastException("Value for key '" + key + "' is not of type " + type.getSimpleName());
        }

        return type.cast(value);
    }
}
