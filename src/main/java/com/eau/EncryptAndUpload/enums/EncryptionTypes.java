package com.eau.EncryptAndUpload.enums;

import com.eau.EncryptAndUpload.presets.encrypt.AESDefault;
import com.eau.EncryptAndUpload.presets.encrypt.EncryptionDefaults;

/**
 * Enum representing supported encryption types and their default configurations.
 * <p>
 * Each enum constant provides a set of default parameters for a specific encryption algorithm.
 * </p>
 *  <p>
 * Add more encryption types as needed to support additional algorithms.
 * </p>

 */
public enum EncryptionTypes {
    /**
     * AES encryption with 128-bit key and default parameters.
     */
    AES128(AESDefault.getAes128Default()),

    /**
     * AES encryption with 256-bit key and default parameters.
     */
    AES256(AESDefault.getAes256Default());

    private final EncryptionDefaults defaults;

    /**
     * Constructs an {@code EncryptionTypes} enum constant with the specified defaults.
     *
     * @param defaults the default encryption parameters for this type
     */
    EncryptionTypes(EncryptionDefaults defaults) {
        this.defaults = defaults;
    }

    /**
     * Returns the default encryption parameters for this type.
     *
     * @return the default {@link EncryptionDefaults} for this encryption type
     */
    public EncryptionDefaults getDefaults() {
        return defaults;
    }
}
