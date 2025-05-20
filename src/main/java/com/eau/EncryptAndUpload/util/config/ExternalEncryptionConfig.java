package com.eau.EncryptAndUpload.util.config;

public class ExternalEncryptionConfig {
    private String type;
    private String key;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}