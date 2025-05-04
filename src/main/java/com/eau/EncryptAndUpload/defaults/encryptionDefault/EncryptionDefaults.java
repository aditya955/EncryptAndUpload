package com.eau.EncryptAndUpload.defaults.encryptionDefault;

public abstract class EncryptionDefaults {
    private final String algorithm;
    private final String keyAlgorithm;
    private final int keySize;
    private final int iterationCount;
    private final int saltSize;
    private final int ivSize;

    protected EncryptionDefaults(String algorithm, String keyAlgorithm, int keySize, int iterationCount, int saltSize, int ivSize) {
        this.algorithm = algorithm;
        this.keyAlgorithm = keyAlgorithm;
        this.keySize = keySize;
        this.iterationCount = iterationCount;
        this.saltSize = saltSize;
        this.ivSize = ivSize;
    }

    // Getters
    public String getAlgorithm() { return algorithm; }
    public String getKeyAlgorithm() { return keyAlgorithm; }
    public int getKeySize() { return keySize; }
    public int getIterationCount() { return iterationCount; }
    public int getSaltSize() { return saltSize; }
    public int getIvSize() { return ivSize; }
}
