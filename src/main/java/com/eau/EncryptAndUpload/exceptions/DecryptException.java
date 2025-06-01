package com.eau.EncryptAndUpload.exceptions;

/**
 * Exception thrown when a decryption operation fails.
 * <p>
 * This exception is used to indicate errors encountered during the decryption process,
 * such as invalid keys, corrupted data, or algorithm failures.
 * </p>
 */
public class DecryptException extends Exception {
    /**
     * Constructs a new {@code DecryptException} with the specified cause.
     *
     * @param error the underlying exception that caused this decryption failure
     */
    public DecryptException(Exception error) {
        super(error);
    }
}
