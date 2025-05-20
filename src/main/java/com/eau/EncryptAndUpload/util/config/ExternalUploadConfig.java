package com.eau.EncryptAndUpload.util.config;

import com.eau.EncryptAndUpload.enums.CloudProvider;
import com.eau.EncryptAndUpload.upload.ProviderOptions;
import com.eau.EncryptAndUpload.upload.gdrive.GoogleDriveOptions;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class ExternalUploadConfig {
    private CloudProvider provider;

    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "provider"
    )
    @JsonSubTypes({
        @JsonSubTypes.Type(value = GoogleDriveOptions.class, name = "GOOGLE_DRIVE")
    })
    private ProviderOptions options;

    public void setProvider(String provider) {
        this.provider = CloudProvider.valueOf(provider);
    }

    public CloudProvider getProvider() {
        return this.provider;
    }

    public void setOptions(ProviderOptions options) {
        this.options = options;
    }

    public ProviderOptions getOptions() {
        return this.options;
    }
}
