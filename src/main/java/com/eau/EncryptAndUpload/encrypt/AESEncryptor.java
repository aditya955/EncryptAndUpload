package com.eau.EncryptAndUpload.encrypt;

import com.eau.EncryptAndUpload.builder.encryptor.EncryptorConfigBuilder;
import com.eau.EncryptAndUpload.exceptions.DecryptException;
import com.eau.EncryptAndUpload.exceptions.EncryptException;

/**
 * AES encryption strategy implementation using a configurable {@link GenericEncryptor}.
 * <p>
 * This class provides AES encryption and decryption functionality by delegating
 * to a {@code GenericEncryptor} instance configured via an {@link EncryptorConfigBuilder}.
 * </p>
 * 
 * Implements {@link EncryptorStrategy}
 */
public class AESEncryptor implements EncryptorStrategy {
    private GenericEncryptor genericEncryptor;

    /**
     * Constructs a new {@code AESEncryptor} with the specified configuration builder.
     *
     * @param builder the encryption configuration builder
     */
    public AESEncryptor(EncryptorConfigBuilder builder) {
        genericEncryptor = new GenericEncryptor(builder);
    }

    /**
     * Encrypts the given data using the configured AES algorithm.
     * 
     * <p>
     * {@inheritDoc}
     * </p>
     */
    @Override
    public byte[] encrypt(byte[] data, String password) throws EncryptException {
        return genericEncryptor.encrypt(data, password);
    }

    /**
     * Decrypts the given data using the configured AES algorithm and password.
     *
     * <p>
     * {@inheritDoc}
     * </p>
     */
    @Override
    public byte[] decrypt(byte[] data, String password) throws DecryptException {
        return genericEncryptor.decrypt(data, password);
    }
}
