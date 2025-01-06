package com.project.secureFileUpload.encryptionAlgorighms;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;


public class Aes implements IEncryption {
    private static Aes classInstance;
    private String algorithm = "AES";
    private int bufferSize = 1024;  // In bytes

    public Aes() { }
    
    public static IEncryption getClassInstance() {
        if(classInstance == null) {
            Aes.classInstance = new Aes();
        }
        return Aes.classInstance;
    }

    @Override
    public IEncryption getInstance() {
        return Aes.getClassInstance();
    }
    
    @Override
    public String encryptStr(SecretKey key, String strToEncrypt) {
        return null;
    }

    @Override
    public String decryptStr(SecretKey key, String strToDecrypt) {
        return null;
    }

    private boolean processFile(SecretKey key, String inputFile, String outputFile, int mode) {
        boolean status = false;
        try(FileInputStream fis = new FileInputStream(inputFile);
            FileOutputStream fos = new FileOutputStream(outputFile)) {
            Cipher cipher = Cipher.getInstance(this.algorithm);
            cipher.init(mode, key);

            byte[] buffer = new byte[this.bufferSize];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    fos.write(output);
                }
            }

            byte[] outputBytes = cipher.doFinal();

            if (outputBytes != null) {
                fos.write(outputBytes);
            }
            status = true;
        } catch(Exception e) {
            System.out.println("[-] Some error occured: " + e.getMessage());
        }
        return status;
    }
    
    @Override
    public boolean encryptFile(SecretKey key, String inputFile, String outputFile) {
        return processFile(key, inputFile, outputFile, Cipher.ENCRYPT_MODE);
    }

    @Override
    public boolean decryptFile(SecretKey key, String inputFile, String outputFile) {
        return processFile(key, inputFile, outputFile, Cipher.DECRYPT_MODE);
    }

    @Override
    public SecretKey generateKey(int keySize) {
        KeyGenerator keyGenerator = null; 
        try {
            keyGenerator = KeyGenerator.getInstance(this.algorithm);
            keyGenerator.init(keySize); // Key size (128, 192, or 256 bits)
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey;
        } catch(NoSuchAlgorithmException e) {
            System.out.println("[-] " + this.algorithm + " doesn't exist");
            // throw new NoSuchAlgorithmException(String.format("%s Algorithm doesn't exist", this.algorithm));
        }
        return null;
    }

    @Override
    public SecretKey generateKey(String keyStr) {
        byte[] decodedKey = null;
        try {
            decodedKey = Base64.getDecoder().decode(keyStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid key: " + e);
            return null;
        }

        return new SecretKeySpec(decodedKey, 0, decodedKey.length, this.algorithm);
    }

    @Override
    public String getKeyStr(SecretKey secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }
}
