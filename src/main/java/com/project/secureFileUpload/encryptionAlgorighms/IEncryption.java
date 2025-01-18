package com.project.secureFileUpload.encryptionAlgorighms;

import java.util.Set;

import javax.crypto.SecretKey;

public interface IEncryption {
    public String encryptStr(SecretKey key, String strToEncrypt);
    public String decryptStr(SecretKey key, String strToDecrypt);

    public boolean encryptFile(SecretKey key, String inputFile, String outputFile, byte[] combinedBytes);
    public boolean decryptFile(SecretKey key, String inputFile, String outputFile, byte[] combinedBytes);

    public SecretKey generateKey(int keySize);
    public SecretKey generateKey(String keyStr);

    public String getKeyStr(SecretKey secretKey);

    public IEncryption getInstance(String algorithm, String mode);

    public static Set<String> getSupportedAlgorithms() {
        return null;
    }

    public static IEncryption get(String algorithm, String mode) {
        Set<String> algos = CipherInstance.getSupportedAlgorithms();
        if(!algos.contains(algorithm)) {
            throw new IllegalArgumentException(algorithm + " Algorithm not supported");
        }
        return new CipherInstance(algorithm, mode);
    }
}
