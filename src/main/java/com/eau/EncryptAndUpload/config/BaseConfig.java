package com.eau.EncryptAndUpload.config;

import java.util.HashMap;
import java.util.Map;

public class BaseConfig {
    /**
     * Internal configuration map holding key-value pairs.
     */
    private Map<String, Object> config = new HashMap<>();

    public BaseConfig() {

    }

    /**
     * Sets a configuration value for the given key.
     *
     * @param key the configuration key
     * @param value the value to set
     * @return this config instance for method chaining
     */
    public BaseConfig set(String key, Object value) { 
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

        try {
            if(type == Integer.class) {
                value = Integer.parseInt(value.toString());
            }
        } catch (Exception e) {
            throw new ClassCastException("Value for key '" + key + "' is not of type " + type.getSimpleName());
        }

        if (!type.isInstance(value)) {
            throw new ClassCastException("Value for key '" + key + "' is not of type " + type.getSimpleName());
        }

        return type.cast(value);
    }
}
