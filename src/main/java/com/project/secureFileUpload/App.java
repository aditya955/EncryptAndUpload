package com.project.secureFileUpload;

import java.util.Base64;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.crypto.SecretKey;

import org.apache.commons.cli.*;

import com.project.secureFileUpload.encryptionAlgorighms.CipherInstance;
import com.project.secureFileUpload.encryptionAlgorighms.IEncryption;
import com.project.secureFileUpload.upload.GoogleDrive;
import com.project.secureFileUpload.upload.IUpload;
import com.google.api.services.drive.model.File;
import java.util.List;

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
        algOption.setRequired(false);
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

        Option interactive = new Option(null, "interactive", false, "Interactive mode");
        interactive.setRequired(false);
        options.addOption(interactive);

        return options;
    }

    private void process(String[] args) {
        Options options = defineOptions();
        CommandLine cmd = parseArgument(options, args);

        if(cmd.hasOption("help")) {
            printHelp(options);
            System.exit(0);
        }

        boolean interactive = cmd.hasOption("interactive");

        try {
            if(interactive) {
                this.interactiveMode();
            } else {
                this.nonInteractiveMode(cmd);
            }
        } catch(Exception e) {
            System.out.println("[-] Some error occured while processing...\n");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private boolean isNumber(String str) {
        return str.chars().allMatch( Character::isDigit );
    }

    private void interactiveMode() {
        Scanner in = new Scanner(System.in);
        int user_input;

        this.interactiveHelp();
        do {
            System.out.print("> ");
            try {
                user_input = in.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("[-] Invalid input");
                user_input = -1;
                in.nextLine();
                continue;
            }  

            switch (user_input) {
            case 0:
                System.out.println("Exiting...");
                break;
            case 1:
                System.out.print("File to encrypt > ");
                String fileToEncrypt = in.next();

                System.out.print("Output file name > ");
                String outputEncryptedFile = in.next();

                System.out.print("Key size > ");
                int keySize = in.nextInt();

                System.out.print("Algorithm > ");
                String algorithm = in.next();

                System.out.print("Mode [n for none] > ");
                String mode = in.next();

                if(mode.strip().toLowerCase().equals("n")) {
                    mode = null;
                }


                SecretKey secretKey = CipherInstance.getClassInstance(algorithm, mode).generateKey(keySize);
                boolean status = encrypt(algorithm, mode, secretKey, fileToEncrypt, outputEncryptedFile, null);

                
                if(status) {
                    System.out.println("[+] File encrypted successfully");
                    System.out.println("Key: " + CipherInstance.getClassInstance(algorithm, mode).getKeyStr(secretKey));
                } else {
                    System.out.println("[-] Error encrypting the file");
                }
                break;
            case 2:
                System.out.print("File to decrypt > ");
                String fileToDecrypt = in.next();

                System.out.print("Output file name > ");
                String outputDecryptedFile = in.next();

                System.out.print("Key size > ");
                keySize = in.nextInt();
                
                System.out.print("Algorithm > ");
                algorithm = in.next();
                
                System.out.print("Mode [n for none] > ");
                mode = in.next();

                if(mode.strip().toLowerCase().equals("n")) {
                    mode = null;
                }
                
                
                System.out.print("Key > ");
                String key = in.next();
                secretKey = CipherInstance.getClassInstance(algorithm, mode).generateKey(key);
                
                status = decrypt(algorithm, mode, secretKey, fileToDecrypt, outputDecryptedFile, null);
                
                System.out.println(status ? "[+] File decrypted successfully" : "[-] Error decrypting the file");
                break;
            case 3:
                System.out.print("File to upload > ");
                String fileToUpload = in.next();
                String fileId = upload(fileToUpload);

                if(fileId == null) {
                    System.out.println("[-] Error uploading the file");
                    break;
                }

                System.out.println("File ID: " + fileId);
                break;
            case 4:
                List<File> files = drive.getAllFiles();
                System.out.println("Sr No.\t\tFile Name\t\t\tFile ID");
                for(int i = 0; i < files.size(); i++) {
                    File file = files.get(i);
                    System.out.println((i + 1) + "\t\t" + file.getName() + "\t\t\t" + file.getId());
                }
                System.out.print("File ID/Sr No. > ");
                fileId = in.next();

                if(isNumber(fileId)) {
                    int id = Integer.parseInt(fileId);
                    if(id > 0 && id <= files.size()) {
                        fileId = files.get(Integer.parseInt(fileId) - 1).getId();
                    }
                }

                boolean found = false;
                for(File file: files) {
                    if(file.getId().equals(fileId)) {
                        found = true;
                        break;
                    }
                }

                if(!found) {
                    System.out.println("[-] Invalid file ID/Sr No.");
                    break;
                }

                System.out.print("Output file name > ");
                outputDecryptedFile = in.next();

                status = download(fileId, outputDecryptedFile);
                System.out.println(status ? "[+] File downloaded successfully" : "[-] Error downloading the file");
                break;
            case 5:
                files = drive.getAllFiles();
                System.out.println("Sr No.\t\tFile Name\t\t\tFile ID");
                for(int i = 0; i < files.size(); i++) {
                    File file = files.get(i);
                    System.out.println((i + 1) + "\t\t" + file.getName() + "\t\t\t" + file.getId());
                }
                break;
            case 99:
                this.interactiveHelp();
                break;
            default:
                System.out.println("Invalid Option");
                this.interactiveHelp();
            }
        } while(user_input != 0);

        in.close();
    }

    private void interactiveHelp() {
        String help = "0. Exit\n" +
                      "1. Encrypt\n" +
                      "2. Decrypt\n" +
                      "3. Upload\n" +
                      "4. Download\n" +
                      "5. List files\n" +
                      "99. Help\n";
        System.out.println(help);
    }

    private void nonInteractiveMode(CommandLine cmd) {
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

        if(algorithm == null) {
            System.out.println("[-] Algorithm not provided");
            printHelp(defineOptions());
            System.exit(1);
        }

        // TODO: Implement these
        // String encryptAndUpload = cmd.getOptionValue("encryptAndUpload");
        // String downloadAndDecrypt = cmd.getOptionValue("downloadAndDecrypt");

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
