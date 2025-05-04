package com.eau.EncryptAndUpload.defaults.encryptionDefault;

public class AESDefault extends EncryptionDefaults {
    
    AESDefault(String algorithm, String keyAlgorithm, int keySize, int iterationCount, int saltSize, int ivSize) {
        super(algorithm, keyAlgorithm, keySize, iterationCount, saltSize, ivSize);
    }
    
    public static AESDefault getAes128Default() {
        return new AESDefault("AES", "PBKDF2WithHmacSHA1", 128, 10000, 8, 16);
    }

    public static AESDefault getAes256Default() {
        return new AESDefault("AES", "PBKDF2WithHmacSHA256", 256, 65536, 16, 16);
    }
}

