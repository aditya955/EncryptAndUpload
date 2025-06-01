package com.eau.EncryptAndUpload.presets.encrypt;

/**
 * Abstract base class for encryption parameter presets.
 * <p>
 * Encapsulates common configuration values for encryption algorithms, such as algorithm name,
 * key derivation algorithm, key size, iteration count, salt size, and IV size. Concrete subclasses
 * provide specific defaults for particular encryption schemes (e.g., AES-128, AES-256).
 * </p>
 */
public abstract class EncryptionDefaults {
    private final String algorithm;
    private final String keyAlgorithm;
    private final int keySize;
    private final int iterationCount;
    private final int saltSize;
    private final int ivSize;

    /**
     * Constructs a new {@code EncryptionDefaults} with the specified parameters.
     *
     * @param algorithm the encryption algorithm (e.g., "AES/GCM/NoPadding")
     * @param keyAlgorithm the key derivation algorithm (e.g., "PBKDF2WithHmacSHA256")
     * @param keySize the key size in bits
     * @param iterationCount the number of iterations for key derivation
     * @param saltSize the salt size in bytes
     * @param ivSize the IV size in bytes
     */
    protected EncryptionDefaults(String algorithm, String keyAlgorithm, int keySize, int iterationCount, int saltSize, int ivSize) {
        this.algorithm = algorithm;
        this.keyAlgorithm = keyAlgorithm;
        this.keySize = keySize;
        this.iterationCount = iterationCount;
        this.saltSize = saltSize;
        this.ivSize = ivSize;
    }

    /**
     * Returns the encryption algorithm name (e.g., "AES/GCM/NoPadding").
     *
     * @return the encryption algorithm name
     */
    public String getAlgorithm() { return algorithm; }

    /**
     * Returns the key derivation algorithm name (e.g., "PBKDF2WithHmacSHA256").
     *
     * @return the key derivation algorithm name
     */
    public String getKeyAlgorithm() { return keyAlgorithm; }

    /**
     * Returns the key size in bits.
     *
     * @return the key size in bits
     */
    public int getKeySize() { return keySize; }

    /**
     * Returns the number of iterations for key derivation.
     *
     * @return the iteration count
     */
    public int getIterationCount() { return iterationCount; }

    /**
     * Returns the salt size in bytes.
     *
     * @return the salt size in bytes
     */
    public int getSaltSize() { return saltSize; }

    /**
     * Returns the IV (Initialization Vector) size in bytes.
     *
     * @return the IV size in bytes
     */
    public int getIvSize() { return ivSize; }
}
