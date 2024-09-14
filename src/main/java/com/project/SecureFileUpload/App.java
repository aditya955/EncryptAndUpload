package com.project.SecureFileUpload;

import java.util.Arrays;

import javax.crypto.SecretKey;

import com.project.SecureFileUpload.EncryptionAlgorighms.Aes;
import com.project.SecureFileUpload.EncryptionAlgorighms.Ecc;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // SecretKey secretKey = Aes.generateKey(256);
        // String encrypted = Aes.encrypt("Hello World", secretKey, "text");
        // String decrypted = Aes.decrypt(encrypted, secretKey, "text");

        // System.out.println("Secret key in pure form: " + secretKey);
        // System.out.println("Your key: " + Aes.secretKeyToString(secretKey));
        // System.out.println("Encrypted: " + encrypted);
        // System.out.println("decrypted: " + decrypted);

        String decrypted = Aes.decrypt("ROeYQQIf3mpWT9Ki13kzEQ==", Aes.stringToSecretKey("mFzDb36tnd3stZjULwEiQcG49FuWqbcrO/UN5oaKJ1E="), "text");
        System.out.println("Decrypted text: " + decrypted);
        // String encrypted = Aes.encrypt("Hello World", Aes.stringToSecretKey("mFzDb36tnd3stZjULwEiQcG49FuWqbcrO/UN5oaKJ1E="), "text");
        // System.out.println("Encrypted text: " + encrypted);0
    }
}
