package com.eau.EncryptAndUpload.encrypt;

import com.eau.EncryptAndUpload.builder.encryptor.EncryptorConfigBuilder;
import com.eau.EncryptAndUpload.exceptions.DecryptException;
import com.eau.EncryptAndUpload.exceptions.EncryptException;

public class AESEncryptor implements EncryptorStrategy {
    private GenericEncryptor genericEncryptor;

    public AESEncryptor(EncryptorConfigBuilder builder) {
        genericEncryptor = new GenericEncryptor(builder);
    }

    @Override
    public byte[] encrypt(byte[] data, String password) throws EncryptException {
        return genericEncryptor.encrypt(data, password);
    }

    @Override
    public byte[] decrypt(byte[] data, String password) throws DecryptException {
        return genericEncryptor.decrypt(data, password);
    }
}
