package com.project.secureFileUpload.encryptionAlgorighms;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.crypto.NoSuchPaddingException;

public class CipherInstance implements IEncryption {
    private static Map<String, CipherInstance> classInstance = new HashMap<>();
    private String algorithm;
    private String modeOfOperation;
    private int bufferSize = 1024;  // In bytes

    public CipherInstance(String algorithm, String mode) {
        this.algorithm = algorithm;
        if(mode == null) {
            mode = "";
        }
        this.modeOfOperation = mode;
     }
    
    public static IEncryption getClassInstance(String algorithm, String mode) {
        if(classInstance.containsKey(algorithm)) {
            return classInstance.get(algorithm);
        }
        
        CipherInstance cipherInstance = new CipherInstance(algorithm, mode);
        classInstance.put(algorithm, cipherInstance);
        return cipherInstance;
    }

    @Override
    public IEncryption getInstance(String algorithm, String mode) {
        return CipherInstance.getClassInstance(algorithm, mode);
    }
    
    @Override
    public String encryptStr(SecretKey key, String strToEncrypt) {
        return null;
    }

    @Override
    public String decryptStr(SecretKey key, String strToDecrypt) {
        return null;
    }

    public static Set<String> getSupportedAlgorithms() {
        Set<String> supportedAlgos = Security.getAlgorithms("Cipher");
        Set<String> strippedAlgos = new HashSet<>();
        for(String algo : supportedAlgos) {
            strippedAlgos.add(algo.split("/")[0]);
        }
        return strippedAlgos;
    }

    private boolean processFile(SecretKey key, String inputFile, String outputFile, int mode, byte[] combinedBytes) {
        boolean status = false;
        try(FileInputStream fis = new FileInputStream(inputFile);
            FileOutputStream fos = new FileOutputStream(outputFile)) {

            Cipher cipher = Cipher.getInstance(this.algorithm + ((this.modeOfOperation != "") ? ("/" + this.modeOfOperation) : ""));


            byte[] iv = new byte[16];;
            byte[] ciphertext = null;
            IvParameterSpec ivSpec = null;
            
            if(this.modeOfOperation.contains("CBC")) {
                if(mode == Cipher.ENCRYPT_MODE) {
                    new SecureRandom().nextBytes(iv);
                    ivSpec = new IvParameterSpec(iv);
                } else if (mode == Cipher.DECRYPT_MODE) {
                    iv = Arrays.copyOfRange(combinedBytes, 0, 16);
                    ciphertext = Arrays.copyOfRange(combinedBytes, 16, combinedBytes.length);
                    ivSpec = new IvParameterSpec(iv);
                }
                cipher.init(mode, key, ivSpec);
            } else {
                cipher.init(mode, key);
            }


            byte[] buffer = new byte[this.bufferSize];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    fos.write(output);
                }
            }
            
            byte[] outputBytes = null;

            if (mode == Cipher.DECRYPT_MODE && this.modeOfOperation.contains("CBC")) {
                outputBytes = cipher.doFinal(ciphertext);
            } else {
                outputBytes = cipher.doFinal();
            }

            if (outputBytes != null) {
                fos.write(outputBytes);

                if(mode == Cipher.ENCRYPT_MODE && this.modeOfOperation.contains("CBC")) {
                    byte[] combined = new byte[iv.length + outputBytes.length];
                    System.arraycopy(iv, 0, combined, 0, iv.length);
                    System.arraycopy(outputBytes, 0, combined, iv.length, outputBytes.length);
                    System.out.println("Combined: " + Base64.getEncoder().encodeToString(combined));
                }
            }

            status = true;
        } catch(InvalidKeyException e) {
            System.out.println("[-] Invalid key: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("[-] File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("[-] Error processing the file: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("[-] No such algorithm found: " + e.getMessage());
        } catch (NoSuchPaddingException e) {
            System.out.println("[-] Bad padding " + e.getMessage());
        } catch (IllegalBlockSizeException e) {
            System.out.println("[-] Bad padding " + e.getMessage());
        } catch (BadPaddingException e) {
            System.out.println("[-] Bad padding " + e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("[-] Invalid Algorithm parameters " + e.getMessage());
        }

        return status;
    }
    
    @Override
    public boolean encryptFile(SecretKey key, String inputFile, String outputFile, byte[] combinedBytes) {
        return processFile(key, inputFile, outputFile, Cipher.ENCRYPT_MODE, combinedBytes);
    }

    @Override
    public boolean decryptFile(SecretKey key, String inputFile, String outputFile, byte[] combinedBytes) {
        return processFile(key, inputFile, outputFile, Cipher.DECRYPT_MODE, combinedBytes);
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
            System.out.println("[-] " + this.algorithm + " algorithm doesn't exist");
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
