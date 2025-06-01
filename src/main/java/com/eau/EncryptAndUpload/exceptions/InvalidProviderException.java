package com.eau.EncryptAndUpload.exceptions;

/**
 * Exception thrown when an unsupported or invalid cloud provider is specified.
 * <p>
 * This exception is used to indicate that the supplied provider is not recognized or supported
 * by the application.
 * </p>
 */
public class InvalidProviderException extends Exception {
    /**
     * Constructs a new {@code InvalidProviderException} for the supplied provider name.
     *
     * @param suppliedProvider the name of the invalid or unsupported provider
     */
    public InvalidProviderException(String suppliedProvider) {
        super(suppliedProvider + " is not a valid provider");
    }
}
