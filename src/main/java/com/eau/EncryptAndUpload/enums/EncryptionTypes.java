package com.eau.EncryptAndUpload.enums;

import com.eau.EncryptAndUpload.builder.encryptor.EncryptorConfigBuilder;
import com.eau.EncryptAndUpload.encrypt.AESEncryptor;
import com.eau.EncryptAndUpload.encrypt.EncryptorStrategy;
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
    AES128(AESDefault.getAes128Default(), AESEncryptor.class),

    /**
     * AES encryption with 256-bit key and default parameters.
     */
    AES256(AESDefault.getAes256Default(), AESEncryptor.class);

    private final EncryptionDefaults defaults;
    private final Class<? extends EncryptorStrategy> strategyClass;

    /**
     * Constructs an {@code EncryptionTypes} enum constant with the specified defaults.
     *
     * @param defaults the default encryption parameters for this type
     */
    EncryptionTypes(EncryptionDefaults defaults, Class<? extends EncryptorStrategy> strategyClass) {
        this.defaults = defaults;
        this.strategyClass = strategyClass;
    }

    /**
     * Returns the default encryption parameters for this type.
     *
     * @return the default {@link EncryptionDefaults} for this encryption type
     */
    public EncryptionDefaults getDefaults() {
        return defaults;
    }

    /**
     * Returns an instance of the encryption strategy for this type.
     *
     * @return a new instance of the {@link EncryptorStrategy} for this encryption type
     */
    public EncryptorStrategy getStrategy(EncryptorConfigBuilder iniObjects) {
        try {
            return strategyClass.getDeclaredConstructor(EncryptorConfigBuilder.class).newInstance(iniObjects);
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate encryption strategy", e);
        }
    }
}
