package com.project.secureFileUpload;

import java.util.Base64;
import java.util.Scanner;

import javax.crypto.SecretKey;

import org.apache.commons.cli.*;

import com.project.secureFileUpload.encryptionAlgorighms.CipherInstance;
import com.project.secureFileUpload.encryptionAlgorighms.IEncryption;
import com.project.secureFileUpload.upload.GoogleDrive;
import com.project.secureFileUpload.upload.IUpload;

public class App 
{
    IUpload drive = GoogleDrive.createWithDefaultTransport();
    public static void main( String[] args )
    {
        App app = new App();
        app.process(args);
    }

    private boolean encrypt(String algorithm, String mode, SecretKey key, String fileToEncrypt, String outputEncryptedFile, byte[] combinedBytes) {
        IEncryption e = CipherInstance.getClassInstance(algorithm, mode);
        return e.encryptFile(key, fileToEncrypt, outputEncryptedFile, combinedBytes);
    }

    private String upload(String fileToUpload) {
        String fileId = drive.upload(fileToUpload);
        return fileId;
    }

    private boolean download(String fileId, String outputDecryptedFile) {
        return drive.download(fileId, outputDecryptedFile);
    }

    private boolean decrypt(String algorithm, String mode, SecretKey key, String outputEncryptedFile, String outputDecryptedFile, byte[] combinedBytes) {
        IEncryption e = CipherInstance.getClassInstance(algorithm, mode);
        return e.decryptFile(key, outputEncryptedFile, outputDecryptedFile, combinedBytes);
    }

    private Options defineOptions() {
        // Create the command line parser
        Options options = new Options();

        Option algOption = new Option("a", "algorithm", true, "algorithm to use for encryption");
        algOption.setRequired(true);
        options.addOption(algOption);

        Option mode = new Option("m", "mode", true, "mode of operatoin to use for encryption (without algorithm name)");
        mode.setRequired(false);
        options.addOption(mode);

        Option combinedBytes = new Option("c", "combinedBytes", true, "Combined bytes (in case of decryption)");
        combinedBytes.setRequired(false);
        options.addOption(combinedBytes);

        Option help = new Option("h", "help", false, "print this help message");
        help.setRequired(false);
        options.addOption(help);

        Option encryptedFileName = new Option(null, "encryptedFile", true, "This name will be used as output file name for encrypted file");
        encryptedFileName.setRequired(false);
        options.addOption(encryptedFileName);

        Option decryptedFileName = new Option(null, "decryptedFile", true, "This name will be used as output file name for decrypted file");
        decryptedFileName.setRequired(false);
        options.addOption(decryptedFileName);

        Option downloadedFileName = new Option(null, "downloadedFile", true, "This name will be used as output file name for downloaded file");
        downloadedFileName.setRequired(false);
        options.addOption(downloadedFileName);

        Option upload = new Option("u", "upload", true, "Upload the file to the cloud");
        upload.setRequired(false);
        options.addOption(upload);

        Option download = new Option("d", "download", true, "Download the file to the cloud. Use --downloadedFile flag to specify the output file name");
        download.setRequired(false);
        options.addOption(download);

        Option encrypt = new Option("e", "encrypt", true, "Encrypt the file. Use --encryptedFile flag to specify the output file name");
        encrypt.setRequired(false);
        options.addOption(encrypt);

        Option decrypt = new Option("i", "decrypt", true, "Interpret/Decrypt the file. Use --decryptedFile flag to specify the output file name");
        decrypt.setRequired(false);
        options.addOption(decrypt);

        Option encryptAndUpload = new Option("s", "up", false, "Encrypt the file and upload to cloud. See options related to encryption and upload for more details (Not implemented)");
        encryptAndUpload.setRequired(false);
        options.addOption(encryptAndUpload);

        Option downloadAndDecrypt = new Option("r", "down", false, "Download the file and decrypt. See options related to download and decrypt for more details (Not implemented)");
        downloadAndDecrypt.setRequired(false);
        options.addOption(downloadAndDecrypt);

        Option key = new Option("k", "key", true, "Key to be used for decryption (Only required if decrypting). If not provided, it will be asked for");
        key.setRequired(false);
        options.addOption(key);

        return options;
    }

    private void process(String[] args) {
        Options options = defineOptions();
        CommandLine cmd = parseArgument(options, args);

        if(cmd.hasOption("help")) {
            printHelp(options);
            System.exit(0);
        }

        // String algorithm = cmd.getOptionValue("algorithm");
        String algorithm = cmd.getOptionValue("algorithm");
        String mode = cmd.getOptionValue("mode");
        String combinedBytes = cmd.getOptionValue("combinedBytes");
        String encryptdFileName = cmd.getOptionValue("encryptedFile");
        String decryptedFileName = cmd.getOptionValue("decryptedFile");
        String downloadedFileName = cmd.getOptionValue("downloadedFile");
        String upload = cmd.getOptionValue("upload");
        String download = cmd.getOptionValue("download");
        String encrypt = cmd.getOptionValue("encrypt");
        String decrypt = cmd.getOptionValue("decrypt");
        String key = cmd.getOptionValue("key");

        // TODO: Implement these
        String encryptAndUpload = cmd.getOptionValue("encryptAndUpload");
        String downloadAndDecrypt = cmd.getOptionValue("downloadAndDecrypt");

        Scanner in = new Scanner(System.in);
        if(encrypt != null) {
            int keySize = 0;
            try {
                keySize = Config.getInt("keySize");
            } catch (IllegalArgumentException e) {
                System.out.println("[-] Key size not found in the configuration file");
                System.out.print("Enter key size > ");
                keySize = in.nextInt();
            }

            SecretKey secretKey = CipherInstance.getClassInstance(algorithm, mode).generateKey(keySize);

            String encryptedFileName = encrypt + ".encrypted";

            if(encryptdFileName != null) {
                encryptedFileName = encryptdFileName;
            }

            boolean status = encrypt(algorithm, mode, secretKey, encrypt, encryptedFileName, null);

            if(!status) {
                System.out.println("[-] Error encrypting the file");
                System.exit(1);
            }

            System.out.println("[+] File encrypted successfully");
            System.out.println("Key: " + CipherInstance.getClassInstance(algorithm, mode).getKeyStr(secretKey));
            System.out.println("Encrypted file: " + encryptedFileName + "\n");
        }

        if(upload != null) {
            String fileId = upload(upload);

            if(fileId == null) {
                System.out.println("[-] Error uploading the file");
                System.exit(1);
            }

            System.out.println("[+] File uploaded successfully");
            System.out.println("File ID: " + fileId + "\n");
        }

        if(download != null) {
            String downloadFilePath = download + ".encrypted";

            if(downloadedFileName != null) {
                downloadFilePath = downloadedFileName;
            }

            boolean status = download(download, downloadFilePath);

            if(!status) {
                System.out.println("[-] Error downloading the file");
                System.exit(1);
            }

            System.out.println("[+] File downloaded successfully\n");
            System.out.println("File saved as: " + downloadFilePath + "\n");
        }

        if(decrypt != null) {
            if(key == null) {
                System.out.println("[-] Key not provided");
                System.out.print("Enter key > ");
                key = in.nextLine();
            }

            SecretKey secretKey = CipherInstance.getClassInstance(algorithm, mode).generateKey(key);

            String output = decrypt + ".decrypted";
            if(decryptedFileName != null) {
                output = decryptedFileName;
            }

            byte[] decodedByte = combinedBytes != null ? Base64.getDecoder().decode(combinedBytes) : null;
            boolean status = decrypt(algorithm, mode, secretKey, decrypt, output, decodedByte);

            if(!status) {
                System.out.println("[-] Error decrypting the file");
                System.exit(1);
            }

            System.out.println("[+] File decrypted successfully\n");
            System.out.println("File saved as: " + output + "\n");
        }
        in.close();
    }

    private void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar <arguments>", options);
    }

    private CommandLine parseArgument(Options options, String[] args) {
        // Parse the command line arguments
        CommandLineParser parser = new DefaultParser();
        
        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            printHelp(options);

            System.exit(1);
        }
        return null;
    }
}
