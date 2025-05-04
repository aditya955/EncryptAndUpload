package com.eau.EncryptAndUpload;

import org.junit.jupiter.api.Test;

import com.eau.EncryptAndUpload.defaults.encryptionDefault.AESDefault;
import com.eau.EncryptAndUpload.encrypt.AESEncryptor;
import com.eau.EncryptAndUpload.encrypt.EncryptorStrategy;
import com.eau.EncryptAndUpload.exceptions.DecryptException;
import com.eau.EncryptAndUpload.exceptions.EncryptException;
import com.eau.EncryptAndUpload.service.EncryptorConfigBuilder;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;

public class AppTest {
    private final String originalText = "The quick brown fox jumps over the lazy dog!\n12345667890\n!@#$%^&*()-_=+[{]}|\\;:,<.>/?`~]";
    private final byte[] originalData = originalText.getBytes(StandardCharsets.UTF_8);
    private final String password = "hello";

    public void testEncryptionAndDecryption(EncryptorStrategy strategy, String algo) {
        try {
            byte[] encryptedData = strategy.encrypt(originalData, password);
            assertNotNull(encryptedData, algo + ": Encrypted data should not be null");
            assertNotEquals(0, encryptedData.length, algo + ": Encrypted data should not be empty");
            
            byte[] decryptedData = strategy.decrypt(encryptedData, password);
            assertNotNull(decryptedData, algo + ": Decrypted data should not be null");
            
            String decryptedText = new String(decryptedData, StandardCharsets.UTF_8);
            assertEquals(originalText, decryptedText, algo + ": Decrypted text should match original");

        } catch (EncryptException | DecryptException e) {
            fail("Encryption or decryption for " + algo + " threw an unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void aes128Text() {
        EncryptorConfigBuilder builder = new EncryptorConfigBuilder(AESDefault.getAes128Default());
        AESEncryptor aes = new AESEncryptor(builder);
        testEncryptionAndDecryption(aes, "AES128");
    }
    
    @Test
    public void aes256Text() {
        EncryptorConfigBuilder builder = new EncryptorConfigBuilder(AESDefault.getAes256Default());
        AESEncryptor aes = new AESEncryptor(builder);
        testEncryptionAndDecryption(aes, "AES256");
    }
}
