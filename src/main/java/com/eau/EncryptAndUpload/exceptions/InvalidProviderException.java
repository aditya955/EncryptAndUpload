package com.eau.EncryptAndUpload.exceptions;

public class InvalidProviderException extends Exception {
    public InvalidProviderException(String suppliedProvider) {
        super(suppliedProvider + " is not a valid provider");
    }
}
