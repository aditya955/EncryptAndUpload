package com.eau.EncryptAndUpload.builder.encryptor;

import com.eau.EncryptAndUpload.encrypt.GenericEncryptor;
import com.eau.EncryptAndUpload.presets.encrypt.EncryptionDefaults;

/**
 * Builder class for constructing encryption configurations.
 * <p>
 * This builder allows chaining of setter methods to configure
 * encryption parameters before building a {@link GenericEncryptor} instance.
 */
public class EncryptorConfigBuilder {
    private String algorithm;
    private String keyAlgorithm;
    private int keySize;
    private int iterationCount;
    private int saltSize;
    private int ivSize;

    /**
     * Constructs a new {@code EncryptorConfigBuilder} using default encryption settings.
     *
     * @param defaults the default encryption values to initialize this builder with
     */
    public EncryptorConfigBuilder(EncryptionDefaults defaults) {
        this.algorithm = defaults.getAlgorithm();
        this.keyAlgorithm = defaults.getKeyAlgorithm();
        this.keySize = defaults.getKeySize();
        this.iterationCount = defaults.getIterationCount();
        this.saltSize = defaults.getSaltSize();
        this.ivSize = defaults.getIvSize();
    }

    /**
     * Sets the encryption algorithm (e.g., AES).
     *
     * @param algorithm the encryption algorithm name
     * @return this builder instance for method chaining
     */
    public EncryptorConfigBuilder setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    /**
     * Sets the key generation algorithm (e.g., PBKDF2WithHmacSHA256).
     *
     * @param keyAlgorithm the key algorithm name
     * @return this builder instance for method chaining
     */
    public EncryptorConfigBuilder setKeyAlgorithm(String keyAlgorithm) {
        this.keyAlgorithm = keyAlgorithm;
        return this;
    }

    /**
     * Sets the key size in bits.
     *
     * @param keySize the key size
     * @return this builder instance for method chaining
     */
    public EncryptorConfigBuilder setKeySize(int keySize) {
        this.keySize = keySize;
        return this;
    }

    /**
     * Sets the number of iterations for key derivation.
     *
     * @param iterationCount the iteration count
     * @return this builder instance for method chaining
     */
    public EncryptorConfigBuilder setIterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
        return this;
    }

    /**
     * Sets the salt size in bytes.
     *
     * @param saltSize the salt size
     * @return this builder instance for method chaining
     */
    public EncryptorConfigBuilder setSaltSize(int saltSize) {
        this.saltSize = saltSize;
        return this;
    }

    /**
     * Sets the IV (Initialization Vector) size in bytes.
     *
     * @param ivSize the IV size
     * @return this builder instance for method chaining
     */
    public EncryptorConfigBuilder setIvSize(int ivSize) {
        this.ivSize = ivSize;
        return this;
    }

    /**
     * Returns the configured encryption algorithm.
     *
     * @return the algorithm name
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * Returns the configured key generation algorithm.
     *
     * @return the key algorithm name
     */
    public String getKeyAlgorithm() {
        return keyAlgorithm;
    }

    /**
     * Returns the configured key size.
     *
     * @return the key size
     */
    public int getKeySize() {
        return keySize;
    }

    /**
     * Returns the configured iteration count.
     *
     * @return the iteration count
     */
    public int getIterationCount() {
        return iterationCount;
    }

    /**
     * Returns the configured salt size in bytes.
     *
     * @return the salt size in bytes
     */
    public int getSaltSize() {
        return saltSize;
    }

    /**
     * Returns the configured IV (Initialization Vector) size in bytes.
     *
     * @return the IV size in bytes
     */
    public int getIvSize() {
        return ivSize;
    }

    /**
     * Builds and returns a {@link GenericEncryptor} instance using the current configuration.
     *
     * @return a new {@code GenericEncryptor} instance
     */
    public GenericEncryptor build() {
        return new GenericEncryptor(this);
    }
}
