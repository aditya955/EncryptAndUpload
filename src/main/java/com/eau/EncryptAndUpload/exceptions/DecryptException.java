package com.eau.EncryptAndUpload.exceptions;

public class DecryptException extends Exception {
    public DecryptException(Exception error) {
        super(error);
    }
}
