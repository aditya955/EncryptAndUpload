package com.eau.EncryptAndUpload.encrypt;

import com.eau.EncryptAndUpload.exceptions.DecryptException;
import com.eau.EncryptAndUpload.exceptions.EncryptException;

/**
 * Strategy interface for encryption and decryption operations.
 * <p>
 * Implementations of this interface provide concrete algorithms for encrypting and decrypting data
 * using a password-based approach (e.g., AES, DES, etc.).
 * </p>
 */
public interface EncryptorStrategy {
    /**
     * Encrypts the given data using the specified password.
     *
     * @param data the plaintext data to encrypt
     * @param password the password for key derivation
     * @return the encrypted data as a byte array
     * @throws EncryptException if encryption fails
     */
    byte[] encrypt(byte[] data, String password) throws EncryptException;

    /**
     * Decrypts the given data using the specified password.
     *
     * @param data the encrypted data to decrypt
     * @param password the password for key derivation
     * @return the decrypted data as a byte array
     * @throws DecryptException if decryption fails
     */
    byte[] decrypt(byte[] data, String password) throws DecryptException;
}
