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

public class GenericEncryptor {
    private final SecureRandom random = new SecureRandom();

    private String algorithm;
    private String keyAlgorithm;
    private int keySize;
    private int iterationCount;
    private int saltSize;
    private int ivSize;

    public GenericEncryptor(EncryptorConfigBuilder builder) {
            this.algorithm = builder.getAlgorithm();
            this.keyAlgorithm = builder.getKeyAlgorithm();
            this.keySize = builder.getKeySize();
            this.iterationCount = builder.getIterationCount();
            this.saltSize = builder.getSaltSize();
            this.ivSize = builder.getIvSize();
    }

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
            // GenericEncryptorStrategy.logger.error("Encryption failed: {}", e.getMessage(), e);
            throw new EncryptException(e);
        }
    }

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
            // GenericEncryptorStrategy.logger.error("Decryption failed: {}", e.getMessage(), e);
            throw new DecryptException(e);
        }
    }

    // Helper method to generate salt
    private byte[] generateSalt() {
        byte[] salt = new byte[this.saltSize];
        this.random.nextBytes(salt);
        return salt;
    }

    // Helper method to generate IV
    private byte[] generateIv() {
        byte[] iv = new byte[this.ivSize];
        this.random.nextBytes(iv);
        return iv;
    }

    // Helper method to derive a key from password + salt
    private SecretKey deriveKey(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String baseAlgorithm = this.algorithm.contains("/") ? this.algorithm.split("/")[0] : this.algorithm;

        SecretKeyFactory factory = SecretKeyFactory.getInstance(this.keyAlgorithm);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, this.iterationCount, this.keySize);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), baseAlgorithm);
    }

    // Helper method to combine salt, IV, and ciphertext
    private byte[] combineSaltIvCiphertext(byte[] salt, byte[] iv, byte[] ciphertext) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(salt);
            outputStream.write(iv);
            outputStream.write(ciphertext);
            return outputStream.toByteArray();
        }
    }
}
