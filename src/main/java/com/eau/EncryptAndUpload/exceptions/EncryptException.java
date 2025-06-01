package com.eau.EncryptAndUpload.exceptions;

/**
 * Exception thrown when an encryption operation fails.
 * <p>
 * This exception is used to indicate errors encountered during the encryption process,
 * such as invalid keys, algorithm failures, or other cryptographic issues.
 * </p>
 */
public class EncryptException extends Exception {
    /**
     * Constructs a new {@code EncryptException} with the specified cause.
     *
     * @param error the underlying exception that caused this encryption failure
     */
    public EncryptException(Exception error) {
        super(error);
    }
}
