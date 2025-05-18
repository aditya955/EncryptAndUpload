package com.eau.EncryptAndUpload.encryptTest;

import org.junit.jupiter.api.Test;

import com.eau.EncryptAndUpload.builder.encryptor.EncryptorConfigBuilder;
import com.eau.EncryptAndUpload.encrypt.AESEncryptor;
import com.eau.EncryptAndUpload.enums.EncryptionTypes;

public class AesTest {
    @Test
    public void aes128Test() {
        EncryptorConfigBuilder builder = new EncryptorConfigBuilder(EncryptionTypes.AES128.getDefaults());
        AESEncryptor aes = new AESEncryptor(builder);
        EncryptionTest.testEncryptionAndDecryption(aes, "AES128");
    }
    
    @Test
    public void aes256Test() {
        EncryptorConfigBuilder builder = new EncryptorConfigBuilder(EncryptionTypes.AES256.getDefaults());
        AESEncryptor aes = new AESEncryptor(builder);
        EncryptionTest.testEncryptionAndDecryption(aes, "AES256");
    }
}
