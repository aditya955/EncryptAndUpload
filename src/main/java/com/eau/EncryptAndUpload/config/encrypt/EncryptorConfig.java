package com.eau.EncryptAndUpload.config.encrypt;

import java.util.HashMap;
import java.util.Map;

import com.eau.EncryptAndUpload.builder.encryptor.EncryptorConfigBuilder;
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
public class EncryptorConfig {
    private final EncryptionTypes encryptionAlgo;

    /**
     * Internal configuration map holding key-value pairs for provider-specific options.
     */
    private Map<String, Object> config = new HashMap<>();

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

    /**
     * Sets a configuration value for the given key.
     *
     * @param key the configuration key
     * @param value the value to set
     * @return this config instance for method chaining
     */
    public EncryptorConfig set(String key, Object value) { 
        config.put(key, value);
        return this;
    }

    /**
     * Retrieves a configuration value for the given key and type.
     *
     * @param <T> the expected type of the value
     * @param key the configuration key
     * @param type the class of the expected value type
     * @return the configuration value cast to the specified type
     * @throws ClassCastException if the value is not of the expected type
     */
    public <T> T get(String key, Class<T> type) {
        Object value = config.get(key);

        if (!type.isInstance(value)) {
            throw new ClassCastException("Value for key '" + key + "' is not of type " + type.getSimpleName());
        }

        return type.cast(value);
    }
}
