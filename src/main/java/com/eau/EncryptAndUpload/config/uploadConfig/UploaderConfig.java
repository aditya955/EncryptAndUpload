package com.eau.EncryptAndUpload.config.uploadConfig;

import java.util.HashMap;
import java.util.Map;

import com.eau.EncryptAndUpload.builder.uploader.UploaderBuilder;
import com.eau.EncryptAndUpload.exceptions.InvalidConfigException;
import com.eau.EncryptAndUpload.exceptions.InvalidProviderException;
import com.eau.EncryptAndUpload.service.CloudProvider;
import com.eau.EncryptAndUpload.upload.gdrive.GoogleDriveBuilder;

public class UploaderConfig {
    private final CloudProvider provider;
    private Map<String, Object> config = new HashMap<>();

    public UploaderConfig(CloudProvider provider) {
        this.provider = provider;
    }

    public UploaderBuilder getBuilder() throws InvalidConfigException, InvalidProviderException { 
        switch (provider) {
            case GOOGLE_DRIVE:
                return new GoogleDriveBuilder(this);
            default:
                throw new InvalidProviderException(provider.name());
        }
    }

    public UploaderConfig set(String key, Object value) { 
        config.put(key, value);
        return this;
    }

    public <T> T get(String key, Class<T> type) {
        Object value = config.get(key);

        if (!type.isInstance(value)) {
            throw new ClassCastException("Value for key '" + key + "' is not of type " + type.getSimpleName());
        }

        return type.cast(value);
    }
}
