package com.eau.EncryptAndUpload.presets.encrypt;

import com.eau.EncryptAndUpload.enums.EncryptionTypes;

/**
 * Preset configuration for AES encryption algorithms.
 * <p>
 * Provides static factory methods for obtaining default configurations for AES-128 and AES-256
 * encryption using GCM mode and PBKDF2 key derivation.
 * </p>
 * <p>
 * This class is intended to be accessed through {@link EncryptionTypes} to enforce consistency.
 * </p>
 */
public class AESDefault extends EncryptionDefaults {
    /**
     * Constructs a new {@code AESDefault} with the specified parameters.
     *
     * @param algorithm the encryption algorithm (e.g., "AES/GCM/NoPadding")
     * @param keyAlgorithm the key derivation algorithm (e.g., "PBKDF2WithHmacSHA256")
     * @param keySize the key size in bits
     * @param iterationCount the number of iterations for key derivation
     * @param saltSize the salt size in bytes
     * @param ivSize the IV size in bytes
     */
    AESDefault(String algorithm, String keyAlgorithm, int keySize, int iterationCount, int saltSize, int ivSize) {
        super(algorithm, keyAlgorithm, keySize, iterationCount, saltSize, ivSize);
    }

    /**
     * Returns the default configuration for AES-128 encryption.
     * <p>Uses GCM mode and appropriate key derivation for secure AES-128 encryption.</p>

     *
     * @return an {@code AESDefault} instance with AES-128 parameters
     */
    public static AESDefault getAes128Default() {
        return new AESDefault("AES/GCM/NoPadding", "PBKDF2WithHmacSHA1", 128, 10000, 8, 16);
    }

    /**
     * Returns the default configuration for AES-256 encryption.
     * <p>Uses GCM mode and appropriate key derivation for secure AES-256 encryption.</p>
     *
     * @return an {@code AESDefault} instance with AES-256 parameters
     */
    public static AESDefault getAes256Default() {
        return new AESDefault("AES/GCM/NoPadding", "PBKDF2WithHmacSHA256", 256, 65536, 16, 16);
    }
}

