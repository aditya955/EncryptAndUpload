package com.eau.EncryptAndUpload;

import com.eau.EncryptAndUpload.ui.AppUi;
import com.eau.EncryptAndUpload.ui.cli.MainCLI;

import picocli.CommandLine;

public class App 
{
    public static void main(String[] args) {
        AppUi ui;

        String mode = System.getProperty("mode", "cli");

        switch (mode) {
            case "cli":
            default:
                ui = new MainCLI();
                break;
        }

        int exitCode = new CommandLine(ui).execute(args);
        System.exit(exitCode);
    }
}
