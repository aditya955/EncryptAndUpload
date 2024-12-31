package com.project.secureFileUpload;

// import java.util.Arrays;

// import javax.crypto.SecretKey;

// import com.project.secureFileUpload.encryptionAlgorighms.Aes;
// import com.project.secureFileUpload.encryptionAlgorighms.Ecc;
import com.project.secureFileUpload.upload.GoogleDrive;

public class App 
{
    public static void main( String[] args )
    {
        // SecretKey secretKey = Aes.generateKey(256);
        // String encrypted = Aes.encrypt("Hello World", secretKey, "text");
        // String decrypted = Aes.decrypt(encrypted, secretKey, "text");

        // System.out.println("Secret key in pure form: " + secretKey);
        // System.out.println("Your key: " + Aes.secretKeyToString(secretKey));
        // System.out.println("Encrypted: " + encrypted);
        // System.out.println("decrypted: " + decrypted);

        // String decrypted = Aes.decrypt("ROeYQQIf3mpWT9Ki13kzEQ==", Aes.stringToSecretKey("mFzDb36tnd3stZjULwEiQcG49FuWqbcrO/UN5oaKJ1E="), "text");
        // System.out.println("Decrypted text: " + decrypted);
        // String encrypted = Aes.encrypt("Hello World", Aes.stringToSecretKey("mFzDb36tnd3stZjULwEiQcG49FuWqbcrO/UN5oaKJ1E="), "text");
        // System.out.println("Encrypted text: " + encrypted);

        GoogleDrive uploadToGDrive = GoogleDrive.createWithDefaultTransport();
        // String fileToUpload = "/home/aditya/Pictures/arch_logo.png";
        // String uploadId = uploadToGDrive.upload(fileToUpload);
        // System.out.println("File ID: " + uploadId);
        System.out.println(uploadToGDrive.getAllFiles());
        System.out.println(uploadToGDrive.upload("/home/aditya/Pictures/arch_logo.png"));
        System.out.println(uploadToGDrive.getAllFiles());
        uploadToGDrive.download("1LV2m6oIkDU8PUVsLVeA2eWi5xKkdJYUP", "/tmp/arch_logo.png");
    }
}
