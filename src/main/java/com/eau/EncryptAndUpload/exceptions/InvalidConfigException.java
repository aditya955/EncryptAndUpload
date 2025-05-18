package com.eau.EncryptAndUpload.exceptions;

public class InvalidConfigException extends Exception {
    public InvalidConfigException(String invalidConfigName) {
        super("Invalid config value for: " + invalidConfigName);
    }
}
