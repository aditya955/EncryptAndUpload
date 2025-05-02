package com.eau.EncryptAndUpload.encrypt;

import com.eau.EncryptAndUpload.exceptions.DecryptException;
import com.eau.EncryptAndUpload.exceptions.EncryptException;

public interface EncryptorStrategy {
    byte[] encrypt(byte[] data, String password) throws EncryptException;
    byte[] decrypt(byte[] data, String password) throws DecryptException;
}
