package com.eau.EncryptAndUpload.ui.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import com.eau.EncryptAndUpload.builder.uploader.UploaderServiceBuilder;
import com.eau.EncryptAndUpload.config.upload.UploaderConfig;
import com.eau.EncryptAndUpload.enums.CloudProvider;
import com.eau.EncryptAndUpload.upload.CloudUploader;
import com.eau.EncryptAndUpload.upload.gdrive.GoogleDriveKeys;
import com.eau.EncryptAndUpload.utils.config.AppConfig;
import com.eau.EncryptAndUpload.utils.config.ConfigLoader;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

@Command(name = "upload", mixinStandardHelpOptions = true, version = "1.0", description = "Upload an encrypted file to the cloud")
public class UploadCommand implements Callable<Integer> {
    @Option(names = {"--provider"}, description = "Cloud Provider", required = false)
    CloudProvider provider;

    @Option(names = {"-l", "--list"}, description = "Lists files", required = false)
    private boolean listFiles;

    @Option(names = {"-u", "--upload"}, description = "Upload file", required = false)
    private String uploadFile;

    @Option(names = {"-d", "--download"}, 
    description = "File ID to download & file name for downloaded file", 
    arity = "2", required = false, paramLabel = "<fileId> <fileName>")
    private String[] downloadFile;

    @Option(names = {"--name"}, description = "Application Name", required = false)
    private String applicationName = "UploadTest";
    
    @Option(names = {"-p", "--port"}, description = "Authentication port", required = false)
    private int authPort = 8888;
    
    @Option(names = {"-c", "--creds"}, description = "Credentials file path (Default = 'local-config/credentials.json')", required = false)
    private String credsFilePath = "local-config/credentials.json";
    
    @Option(names = {"-t", "--tokens"}, description = "Tokens directory path", required = false)
    private String tokensDir = "local-config";
    
    @Option(names = {"--config"}, description = "Config file path", required = false)
    private String configFilePath;

    @Override
    public Integer call() {
        try {
            UploaderConfig uploaderConfig;

            if(configFilePath != null) {
                uploaderConfig = getUploaderConfigFromConfigFile();
            } else {
                uploaderConfig = getUploaderConfigFromCommandLineArgs();
                verifyCommandLineArgument();
            }


            populateUploaderConfig(uploaderConfig);

            UploaderServiceBuilder uploaderBuilder = new UploaderServiceBuilder(uploaderConfig);

            CloudUploader uploader = uploaderBuilder.build();
            if(uploader == null) {
                throw new Exception("Unable to create uploader");
            }

            if(listFiles) {
                listFiles(uploader);
            }

            if(uploadFile != null) {
                uploadFile(uploader);
            }

            if(downloadFile != null) {
                downloadFile(uploader);
            }

            return 0;
        } catch (Exception e) {
            System.out.println("Unable to execute upload file: " + e);
            e.printStackTrace();
            return 1;
        }
    }

    private void verifyCommandLineArgument() throws Exception {
        if(provider == null) {
            System.out.println("Required argument --provider");
            CommandLine.usage(this, System.out);
            System.exit(1);
        }
    }

    private void populateUploaderConfig(UploaderConfig uploaderConfig) throws GeneralSecurityException, IOException {
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        uploaderConfig.set(GoogleDriveKeys.HTTP_TRANSPORT.getKey(), HTTP_TRANSPORT);

        uploaderConfig.set(GoogleDriveKeys.JSON_FACTORY.getKey(), GsonFactory.getDefaultInstance());

        uploaderConfig.set(GoogleDriveKeys.SCOPES.getKey(), Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY, DriveScopes.DRIVE_FILE, DriveScopes.DRIVE));
    }

    private UploaderConfig getUploaderConfigFromConfigFile() throws FileNotFoundException, IOException {
        AppConfig config = ConfigLoader.getPropertiesConfig(configFilePath);
        UploaderConfig uploaderConfig = config.getUploaderConfig();
        return uploaderConfig;
    }

    private UploaderConfig getUploaderConfigFromCommandLineArgs() {
            UploaderConfig uploaderConfig = new UploaderConfig(provider);
            uploaderConfig.set("applicationName", applicationName);
            uploaderConfig.set("authPort", authPort);
            uploaderConfig.set("credsFilePath", credsFilePath);
            uploaderConfig.set("tokensDirPath", tokensDir);
            return uploaderConfig;
    }

    private void listFiles(CloudUploader uploader) {
            List<File> files = uploader.getAllFiles();

            System.out.println("Sr No.\t\tFile Name\t\t\tFile ID");
            for(int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                System.out.println((i + 1) + "\t\t" + file.getName() + "\t\t\t" + file.getId());
            }
    }

    private void uploadFile(CloudUploader uploader) {
        uploader.upload(uploadFile);
    }

    private void downloadFile(CloudUploader uploader) {
        uploader.download(downloadFile[0], downloadFile[1]);
    }
}
