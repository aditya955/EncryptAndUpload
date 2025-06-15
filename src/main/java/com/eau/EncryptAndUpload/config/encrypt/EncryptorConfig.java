package com.eau.EncryptAndUpload.config.encrypt;

import com.eau.EncryptAndUpload.builder.encryptor.EncryptorConfigBuilder;
import com.eau.EncryptAndUpload.config.BaseConfig;
import com.eau.EncryptAndUpload.enums.EncryptionTypes;
import com.eau.EncryptAndUpload.exceptions.InvalidConfigException;
import com.eau.EncryptAndUpload.exceptions.InvalidProviderException;

/**
 * Configuration class for encryption modules.
 * <p>
 * This class encapsulates configuration options required to build an encryptor for a specific
 * encryption strategy. It provides methods to set and retrieve configuration values.
 * </p>
 */
public class EncryptorConfig extends BaseConfig {
    private final EncryptionTypes encryptionAlgo;

    /**
     * Constructs a new {@code EncryptionTypes} for the specified provider.
     *
     * @param encryptionAlgo the encryption algorithm for this configuration
     */
    public EncryptorConfig(EncryptionTypes algo) {
        this.encryptionAlgo = algo;
    }

    /**
     * Returns a suitable {@link EncryptorConfigBuilder} for the configured algorithm.
     *
     * @return Encryptor config builder for the algorithm
     * @throws InvalidConfigException if the configuration is invalid
     * @throws InvalidProviderException if the provider is not supported
     */
    public EncryptorConfigBuilder getBuilder() { 
        return new EncryptorConfigBuilder(this.encryptionAlgo.getDefaults());
    }
}
