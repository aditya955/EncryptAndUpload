package com.eau.EncryptAndUpload.encryptTest;

import org.junit.jupiter.api.Test;

import com.eau.EncryptAndUpload.builder.encryptor.EncryptorConfigBuilder;
import com.eau.EncryptAndUpload.encrypt.AESEncryptor;
import com.eau.EncryptAndUpload.presets.encrypt.AESDefault;

public class AesTest {
    @Test
    public void aes128Test() {
        EncryptorConfigBuilder builder = new EncryptorConfigBuilder(AESDefault.getAes128Default());
        AESEncryptor aes = new AESEncryptor(builder);
        EncryptionTest.testEncryptionAndDecryption(aes, "AES128");
    }
    
    @Test
    public void aes256Test() {
        EncryptorConfigBuilder builder = new EncryptorConfigBuilder(AESDefault.getAes256Default());
        AESEncryptor aes = new AESEncryptor(builder);
        EncryptionTest.testEncryptionAndDecryption(aes, "AES256");
    }
}
