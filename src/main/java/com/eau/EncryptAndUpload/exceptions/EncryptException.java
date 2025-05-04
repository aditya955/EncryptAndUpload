package com.eau.EncryptAndUpload.exceptions;

public class EncryptException extends Exception {
    public EncryptException(Exception error) {
        super(error);
    }
}
