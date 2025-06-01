package com.eau.EncryptAndUpload.encrypt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.eau.EncryptAndUpload.builder.encryptor.EncryptorConfigBuilder;
import com.eau.EncryptAndUpload.exceptions.DecryptException;
import com.eau.EncryptAndUpload.exceptions.EncryptException;

/**
 * Generic encryption utility supporting configurable algorithms and key derivation.
 * <p>
 * This class provides methods for encrypting and decrypting data using password-based encryption (PBE),
 * supporting configurable algorithms, key sizes, salt, IV, and iteration count. It is designed to be
 * flexible and reusable for different encryption strategies.
 * </p>
 */
public class GenericEncryptor {
    private final SecureRandom random = new SecureRandom();
    private String algorithm;
    private String keyAlgorithm;
    private int keySize;
    private int iterationCount;
    private int saltSize;
    private int ivSize;

    /**
     * Constructs a new {@code GenericEncryptor} using the provided configuration builder.
     *
     * @param builder the encryption configuration builder
     */
    public GenericEncryptor(EncryptorConfigBuilder builder) {
        this.algorithm = builder.getAlgorithm();
        this.keyAlgorithm = builder.getKeyAlgorithm();
        this.keySize = builder.getKeySize();
        this.iterationCount = builder.getIterationCount();
        this.saltSize = builder.getSaltSize();
        this.ivSize = builder.getIvSize();
    }

    /**
     * Encrypts the given data using the configured algorithm and password.
     *
     * @param data the plaintext data to encrypt
     * @param password the password for key derivation
     * @return the encrypted data as a byte array
     * @throws EncryptException if encryption fails
     */
    public byte[] encrypt(byte[] data, String password) throws EncryptException {
        try {
            byte[] salt = this.generateSalt();
            byte[] iv = this.generateIv();

            SecretKey secretKey = deriveKey(password, salt);

            Cipher cipher = Cipher.getInstance(this.algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(this.ivSize * 8, iv));
            byte[] ciphertext = cipher.doFinal(data);

            return combineSaltIvCiphertext(salt, iv, ciphertext);
        } catch(Exception e) {
            throw new EncryptException(e);
        }
    }

    /**
     * Decrypts the given data using the configured algorithm and password.
     *
     * @param encryptedData the encrypted data to decrypt
     * @param password the password for key derivation
     * @return the decrypted data as a byte array
     * @throws DecryptException if decryption fails
     */
    public byte[] decrypt(byte[] encryptedData, String password) throws DecryptException {
        try {
            // 1. Extract salt and IV
            final int saltAndIvSize = this.saltSize + this.ivSize;
            byte[] salt = Arrays.copyOfRange(encryptedData, 0, this.saltSize);
            byte[] iv = Arrays.copyOfRange(encryptedData, this.saltSize, saltAndIvSize);
            byte[] ciphertext = Arrays.copyOfRange(encryptedData, saltAndIvSize, encryptedData.length);
        
            // 2. Derive key using extracted salt
            SecretKey secretKey = deriveKey(password, salt);

            // 3. Decrypt
            Cipher cipher = Cipher.getInstance(this.algorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(this.ivSize * 8, iv));
            return cipher.doFinal(ciphertext);
        } catch(Exception e) {
            throw new DecryptException(e);
        }
    }

    /**
     * Generates a random salt of the configured size.
     *
     * @return a new random salt as a byte array
     */
    private byte[] generateSalt() {
        byte[] salt = new byte[this.saltSize];
        this.random.nextBytes(salt);
        return salt;
    }

    /**
     * Generates a random initialization vector (IV) of the configured size.
     *
     * @return a new random IV as a byte array
     */
    private byte[] generateIv() {
        byte[] iv = new byte[this.ivSize];
        this.random.nextBytes(iv);
        return iv;
    }

    /**
     * Derives a cryptographic key from the given password and salt using the configured key algorithm.
     *
     * @param password the password to derive the key from
     * @param salt the salt to use in key derivation
     * @return the derived secret key
     * @throws NoSuchAlgorithmException if the key algorithm is not available
     * @throws InvalidKeySpecException if the key specification is invalid
     */
    private SecretKey deriveKey(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String baseAlgorithm = this.algorithm.contains("/") ? this.algorithm.split("/")[0] : this.algorithm;

        SecretKeyFactory factory = SecretKeyFactory.getInstance(this.keyAlgorithm);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, this.iterationCount, this.keySize);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), baseAlgorithm);
    }

    /**
     * Combines the salt, IV, and ciphertext into a single byte array for storage or transmission.
     *
     * @param salt the salt used for encryption
     * @param iv the initialization vector used for encryption
     * @param ciphertext the encrypted data
     * @return a byte array containing salt, IV, and ciphertext concatenated
     * @throws IOException if an I/O error occurs
     */
    private byte[] combineSaltIvCiphertext(byte[] salt, byte[] iv, byte[] ciphertext) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(salt);
            outputStream.write(iv);
            outputStream.write(ciphertext);
            return outputStream.toByteArray();
        }
    }
}
