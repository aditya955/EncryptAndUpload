package com.eau.EncryptAndUpload.builder.uploader;

import com.eau.EncryptAndUpload.config.uploadConfig.UploaderConfig;
import com.eau.EncryptAndUpload.exceptions.InvalidConfigException;
import com.eau.EncryptAndUpload.exceptions.InvalidProviderException;
import com.eau.EncryptAndUpload.upload.CloudUploader;

public class UploaderServiceBuilder {
    private final UploaderConfig config;

    public UploaderServiceBuilder(UploaderConfig config) {
        this.config = config;
    }

    public CloudUploader build() {
        try {
            UploaderBuilder builder = config.getBuilder();
            return builder.build();
        } catch (InvalidConfigException e) {
            System.out.println("[-] Invalid config..." + e);
        } catch (InvalidProviderException e) {
            System.out.println("[-] Invalid provider..." + e);
        }
        return null;
    }
}
