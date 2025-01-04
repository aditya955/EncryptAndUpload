package com.project.SecureFileUpload;

import javax.crypto.SecretKey;

import com.project.secureFileUpload.encryptionAlgorighms.Aes;
import com.project.secureFileUpload.encryptionAlgorighms.IEncryption;
import com.project.secureFileUpload.upload.GoogleDrive;
import com.project.secureFileUpload.upload.IUpload;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    static IEncryption e = Aes.getClassInstance();
    static SecretKey key = e.generateKey(256);
    static IUpload drive = GoogleDrive.createWithDefaultTransport();
    static String fileId;
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        
    }

    public void testUploadAndDownload() {
        String[][] files = new String[][] {
            {
                "/tmp/test/input.txt", "/tmp/test/input.txt.encrypted", "/tmp/test/output.txt"
            },
            {
                "/tmp/test/input.png", "/tmp/test/input.png.encrypted", "/tmp/test/output.png"
            }
        };

        for(int i = 0; i < files.length; i++) {
            String[] currentFile = files[i];

            String fileToEncrypt = currentFile[0];
            String outputEncryptedFile = currentFile[1];
            String outputDecryptedFile = currentFile[2];

            System.out.println("[+] Processing: " + fileToEncrypt);

            boolean encryptStatus = encrypt(fileToEncrypt, outputEncryptedFile);
            assertTrue(encryptStatus);
    
            boolean uploadStatus = upload(outputEncryptedFile);
            assertTrue(uploadStatus);
    
            boolean downloadStatus = download(outputDecryptedFile);
            assertTrue(downloadStatus);
    
            boolean decryptStatus = decrypt(outputEncryptedFile, outputDecryptedFile);
            assertTrue(decryptStatus);
        }
    }

    private static boolean encrypt(String fileToEncrypt, String outputEncryptedFile) {
        return e.encryptFile(key, fileToEncrypt, outputEncryptedFile);
    }

    private static boolean upload(String outputEncryptedFile) {
        fileId = drive.upload(outputEncryptedFile);
        return (fileId != null);
    }

    private static boolean download(String outputDecryptedFile) {
        return drive.download(fileId, outputDecryptedFile);
    }

    private static boolean decrypt(String outputEncryptedFile, String outputDecryptedFile) {
        return e.decryptFile(key, outputEncryptedFile, outputDecryptedFile);
    }
}
