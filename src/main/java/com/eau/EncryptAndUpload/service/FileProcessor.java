package com.eau.EncryptAndUpload.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.BiFunction;

import com.eau.EncryptAndUpload.builder.encryptor.EncryptorConfigBuilder;
import com.eau.EncryptAndUpload.encrypt.EncryptorStrategy;
import com.eau.EncryptAndUpload.enums.EncryptionTypes;
import com.eau.EncryptAndUpload.exceptions.DecryptException;
import com.eau.EncryptAndUpload.exceptions.EncryptException;

public class FileProcessor {
    private EncryptorStrategy strategy;
    public FileProcessor(EncryptionTypes algo) {
        EncryptorConfigBuilder builder = new EncryptorConfigBuilder(algo.getDefaults());
        this.strategy = algo.getStrategy(builder);
    }

    private void processFile(String inputFilePath, String password, String outputFilePath, BiFunction<byte[], String, byte[]> operation) throws IOException {
        try (FileInputStream input = new FileInputStream(inputFilePath)) {
            byte[] data = input.readAllBytes();
            byte[] operatedData = operation.apply(data, password);
            this.writeOutputToFile(operatedData, outputFilePath);
        }
    }

    private void writeOutputToFile(byte[] data, String outputFilePath) throws IOException {
        try (FileOutputStream output = new FileOutputStream(outputFilePath)) {
            output.write(data);
        }
    }

    public void encryptFile(String filePath, String password, String outputFilePath) throws IOException, EncryptException {
        this.processFile(filePath, password, outputFilePath, (data, pwd) -> {
            try {
                return this.strategy.encrypt(data, pwd);
            } catch (EncryptException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void decryptFile(String filePath, String password, String outputFilePath) throws IOException, DecryptException {
        this.processFile(filePath, password, outputFilePath, (data, pwd) -> {
            try {
                return this.strategy.decrypt(data, pwd);
            } catch (DecryptException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
