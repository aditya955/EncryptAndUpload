package com.eau.EncryptAndUpload.encryptTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.charset.StandardCharsets;

import com.eau.EncryptAndUpload.encrypt.EncryptorStrategy;
import com.eau.EncryptAndUpload.exceptions.DecryptException;
import com.eau.EncryptAndUpload.exceptions.EncryptException;

public class EncryptionTest {
    private static final String originalText = "The quick brown fox jumps over the lazy dog!\n12345667890\n!@#$%^&*()-_=+[{]}|\\;:,<.>/?`~]";
    private static final byte[] originalData = originalText.getBytes(StandardCharsets.UTF_8);
    private static final String password = "hello";

    public static void testEncryptionAndDecryption(EncryptorStrategy strategy, String algo) {
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
}
