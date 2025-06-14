package com.eau.EncryptAndUpload.utils.config;

import com.eau.EncryptAndUpload.config.encrypt.EncryptorConfig;
import com.eau.EncryptAndUpload.config.upload.UploaderConfig;

public class AppConfig {
    private EncryptorConfig encryptorConfig;
    private UploaderConfig uploaderConfig;

    public AppConfig(EncryptorConfig encryptorConfig, UploaderConfig uploaderConfig) {
        this.encryptorConfig = encryptorConfig;
        this.uploaderConfig = uploaderConfig;
    }

    public EncryptorConfig getEncryptorConfig() {
        return this.encryptorConfig;
    }

    public UploaderConfig getUploaderConfig() {
        return this.uploaderConfig;
    }
}
