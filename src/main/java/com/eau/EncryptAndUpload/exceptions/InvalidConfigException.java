package com.eau.EncryptAndUpload.exceptions;

/**
 * Exception thrown when a configuration value is invalid or missing.
 * <p>
 * This exception is used to indicate errors in configuration parameters required for
 * application setup or operation.
 * </p>
 */
public class InvalidConfigException extends Exception {
    /**
     * Constructs a new {@code InvalidConfigException} for the specified configuration name.
     *
     * @param invalidConfigName the name of the invalid configuration parameter
     */
    public InvalidConfigException(String invalidConfigName) {
        super("Invalid config value for: " + invalidConfigName);
    }
}
