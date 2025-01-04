package com.project.secureFileUpload.encryptionAlgorighms;

import javax.crypto.SecretKey;

public interface IEncryption {
    public String encryptStr(SecretKey key);
    public String decryptStr(SecretKey key);

    public boolean encryptFile(SecretKey key, String inputFile, String outputFile);
    public boolean decryptFile(SecretKey key, String inputFile, String outputFile);

    public SecretKey generateKey(int keySize);
    public SecretKey generateKey(String keyStr);

    public String getKeyStr(SecretKey secretKey);

    public IEncryption getInstance();

    public static IEncryption getInstance(String algorithm) {
        switch (algorithm.toLowerCase()) {
            case "aes":
                return new Aes();
            default:
                break;
        }
        return new Aes();
    }
}
