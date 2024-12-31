package com.project.secureFileUpload.encryptionAlgorighms;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class Aes {

    // Method to generate a secret key for AES
    public static SecretKey generateKey(int n) {
        KeyGenerator keyGenerator = null; 
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch(NoSuchAlgorithmException e) {
            System.out.println("No such algo exist (Weird!): " + e);
        }

        keyGenerator.init(n); // Key size (128, 192, or 256 bits)
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey;
    }

    // Method to encrypt a string using AES
    private static String encryptAes(String plainText, SecretKey secretKey) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
        } catch(NoSuchPaddingException e) {
            System.out.println("No such padding exception: " + e);
            return null;
        } catch(NoSuchAlgorithmException e) {
            System.out.println("No such algo exception (should never happen): " + e);
            return null;
        }

        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } catch(InvalidKeyException e) {
            System.out.println("Invalid key");
            return null;
        }

        byte[] encryptedBytes = null;
        try {
            encryptedBytes = cipher.doFinal(plainText.getBytes());
        } catch(IllegalBlockSizeException e) {
            System.out.println("Invalid block size for AES" + e);
            return null;
        } catch(BadPaddingException e) {
            System.out.println("Bad padding exception" + e);
            return null;
        }
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Method to decrypt an encrypted string using AES
    private static String decryptAes(String encryptedText, SecretKey secretKey) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
        } catch(NoSuchPaddingException e) {
            System.out.println("No such padding exception: " + e);
            return null;
        } catch(NoSuchAlgorithmException e) {
            System.out.println("No such algo exception (should never happen): " + e);
            return null;
        }

        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        } catch (InvalidKeyException e) {
            System.out.println("Invalid key");
            return null;
        }


        byte[] decryptedBytes = null;
        try {
            decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        } catch(IllegalBlockSizeException e) {
            System.out.println("Invalid block size for AES" + e);
            return null;
        } catch(BadPaddingException e) {
            System.out.println("Bad padding exception" + e);
            return null;
        }

        return new String(decryptedBytes);
    }

    // Method to convert SecretKey to Base64 String
    public static String secretKeyToString(SecretKey secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    // Method to convert Base64 String back to SecretKey
    public static SecretKey stringToSecretKey(String keyStr) {
        byte[] decodedKey = null;
        try {
            decodedKey = Base64.getDecoder().decode(keyStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid key: " + e);
            return null;
        }

        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public static void saveKeyToFile(String keyString, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(keyString);
        }
    }

    public static String loadKeyFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.readLine();
        }
    }

    public static String encrypt(String data, SecretKey secretKey, String type) {
        StringBuilder encryptData = null;

        if(type.toLowerCase() == "file") {
            try {
                File myObj = new File("filename.txt");
                Scanner myReader = new Scanner(myObj);
                encryptData = new StringBuilder();
                while (myReader.hasNextLine()) {
                    encryptData.append(myReader.nextLine());
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred: " + e);
                e.printStackTrace();
                return null;
            }    
        } else if (type.toLowerCase() == "text") {
            encryptData = new StringBuilder(data);
        } else {
            System.out.println("Unknown type for AES encryption");
        }

        return encryptAes(encryptData.toString(), secretKey);
    }

    public static String decrypt(String data, SecretKey secretKey, String type) {
        StringBuilder encryptData = null;
        if(type.toLowerCase() == "file") {
            try {
                File myObj = new File("filename.txt");
                Scanner myReader = new Scanner(myObj);
                encryptData = new StringBuilder();
                while (myReader.hasNextLine()) {
                    encryptData.append(myReader.nextLine());
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred: " + e);
                e.printStackTrace();
                return null;
            }    
        } else {
            encryptData = new StringBuilder(data);
        }

        return decryptAes(encryptData.toString(), secretKey);
    }

    public static void main(String[] args) {
        try {
            // Generate a secret key
            SecretKey secretKey = generateKey(256); // AES key size: 128 bits

            // Example data to encrypt and decrypt
            String originalText = "Hello, World!";

            String keyString = secretKeyToString(secretKey);
            System.out.println("Secret Key (Base64): " + keyString);


            // Encrypt the data
            String encryptedText = encryptAes(originalText, secretKey);
            System.out.println("Encrypted Text: " + encryptedText);

            // Decrypt the data
            String decryptedText = decryptAes(encryptedText, secretKey);
            System.out.println("Decrypted Text: " + decryptedText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
