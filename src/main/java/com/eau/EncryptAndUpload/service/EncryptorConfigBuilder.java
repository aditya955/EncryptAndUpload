package com.eau.EncryptAndUpload.service;

import com.eau.EncryptAndUpload.defaults.encryptionDefault.EncryptionDefaults;
import com.eau.EncryptAndUpload.encrypt.GenericEncryptor;

public class EncryptorConfigBuilder {
    private String algorithm;
    private String keyAlgorithm;
    private int keySize;
    private int iterationCount;
    private int saltSize;
    private int ivSize;

    public EncryptorConfigBuilder(EncryptionDefaults defaults) {
        this.algorithm = defaults.getAlgorithm();
        this.keyAlgorithm = defaults.getKeyAlgorithm();
        this.keySize = defaults.getKeySize();
        this.iterationCount = defaults.getIterationCount();
        this.saltSize = defaults.getSaltSize();
        this.ivSize = defaults.getIvSize();
    }

    public EncryptorConfigBuilder setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public EncryptorConfigBuilder setKeyAlgorithm(String keyAlgorithm) {
        this.keyAlgorithm = keyAlgorithm;
        return this;
    }

    public EncryptorConfigBuilder setKeySize(int keySize) {
        this.keySize = keySize;
        return this;
    }

    public EncryptorConfigBuilder setIterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
        return this;
    }

    public EncryptorConfigBuilder setSaltSize(int saltSize) {
        this.saltSize = saltSize;
        return this;
    }

    public EncryptorConfigBuilder setIvSize(int ivSize) {
        this.ivSize = ivSize;
        return this;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getKeyAlgorithm() {
        return keyAlgorithm;
    }

    public int getKeySize() {
        return keySize;
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public int getSaltSize() {
        return saltSize;
    }

    public int getIvSize() {
        return ivSize;
    }

    public GenericEncryptor build() {
        return new GenericEncryptor(this);
    }
}
