package com.project.SecureFileUpload.EncryptionAlgorighms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;

public class Ecc {
    static String python_venv_path = ".venv/bin/python";
    static String ecc_script_path = "scripts/python/ecc.py";
    public static void encrypt(String data) {
        try {
            InputStream scriptStream = Ecc.class.getClassLoader().getResourceAsStream(ecc_script_path);
            // InputStream venvURL = Ecc.class.getClassLoader().getResourceAsStream(python_venv_path);
            
            if(scriptStream != null) {
                // Convert URL to File
                File tempScriptFile = File.createTempFile("script", ".py");
                tempScriptFile.deleteOnExit();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(scriptStream));
                    FileWriter writer = new FileWriter(tempScriptFile)) {

                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.write(System.lineSeparator());
                    }
                }

                // Create a ProcessBuilder to call the Python script with arguments
                // ProcessBuilder pb = new ProcessBuilder("python3", tempScriptFile.getAbsolutePath());
                ProcessBuilder pb = new ProcessBuilder("/home/aditya/Documents/Programs/Projects/UploadToCloudEncrypted/SecureFileUpload/src/main/resources/.venv/bin/python3", tempScriptFile.getAbsolutePath(), "encrypt", "/tmp/input.txt", "/tmp/output.ecc");
                Process process = pb.start();

                // Read and print Python script output
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                StringBuilder finalOutput = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    finalOutput.append(line + "\n");
                    System.out.println(line);
                }
                // System.out.println(finalOutput);
    
                // Wait for the process to complete
                process.waitFor();
            } else {
                System.out.println("Unable to locate python file or venv python");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception");
        }
    }
}
