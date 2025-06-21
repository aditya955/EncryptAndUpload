package com.eau.EncryptAndUpload.ui.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

import com.eau.EncryptAndUpload.enums.EncryptionTypes;
import com.eau.EncryptAndUpload.service.FileProcessor;

@Command(name = "encrypt", mixinStandardHelpOptions = true, version = "1.0", description = "Encrypt a file using the specified algorithm")
public class EncryptCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "Input file to encrypt/decrypt")
    private String inputFile;

    @Parameters(index = "1", description = "Output file. Default = 'inputFile'.out")
    private String outputFile = inputFile + ".out";

    @Option(names = {"-a", "--algorithm"}, description = "Encryption algorithm (AES128, AES256)", required = true)
    private String algorithm;

    @Option(names = {"-p", "--password"}, description = "Password for encryption", arity = "0..1", required = true, interactive = true)
    private String password;

    @Option(names = {"-d", "--decrypt"}, description = "Decrypt? (Default behaviour is to encrypt the given file)")
    private boolean decrypt;

    @Override
    public Integer call() {
        FileProcessor fileProcessor = new FileProcessor(EncryptionTypes.valueOf(algorithm));

        try {
            if(decrypt) {
                fileProcessor.decryptFile(inputFile, password, outputFile);
            } else {
                fileProcessor.encryptFile(inputFile, password, outputFile);
            }
        } catch (Exception e) {
            System.out.println("Unable to encrypt/decrypt file:\n" + e);
            return 1;
        }
        return 0;
    }
}
