package com.eau.EncryptAndUpload.util.config;

import java.util.List;

public class JsonConfig {
    private List<ExternalEncryptionConfig> encryption;
    private List<ExternalUploadConfig> upload;

    public List<ExternalEncryptionConfig> getEncryptionConfig() {
        return this.encryption;
    }

    public void setEncryption(List<ExternalEncryptionConfig> encryption) {
        this.encryption = encryption;
    }

    public List<ExternalUploadConfig> getUploadConfig() {
        return this.upload;
    }

    public void setUpload(List<ExternalUploadConfig> upload) {
        this.upload = upload;
    }
}
