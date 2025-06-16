package com.eau.EncryptAndUpload.ui.cli;

import com.eau.EncryptAndUpload.ui.AppUi;

import picocli.CommandLine.Command;

@Command(
    name = "eau-cli",
    mixinStandardHelpOptions = true,
    version = "EncryptAndUpload 1.0",
    description = "Encrypts and uploads files to cloud storage.",
    subcommands = {
        EncryptCommand.class,
        UploadCommand.class
    }
)
public class MainCLI implements AppUi {
    
}
