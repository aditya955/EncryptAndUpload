package com.project.secureFileUpload;

import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Config {
    private static final Map<String, Object> config = new ConcurrentHashMap<>();

    static {
        loadConfig();
    }

    private static void loadConfig() {
        Yaml yaml = new Yaml();
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.yaml")) {
            if (input == null) {
                throw new FileNotFoundException("Configuration file not found");
            }
            Map<String, Object> yamlConfig = yaml.load(input);
            if (yamlConfig != null) {
                config.putAll(yamlConfig);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public static List<?> getList(String key) {
        Object value = config.get(key);
        if (value instanceof List) {
            return (List<?>) value;
        }
        return Collections.emptyList();
    }
    

    public static String getStr(String key) {
        Object value = config.get(key);
        return value != null ? value.toString() : null;
    }

    public static Integer getInt(String key) {
        Object value = config.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        throw new IllegalArgumentException("Key " + key + " is not an integer");
    }

    public static void set(String key, Object value) {
        config.put(key, value);
    }

    public static boolean containsKey(String key) {
        return config.containsKey(key);
    }

    public static Object getNested(String key) {
        String[] keys = key.split("\\.");
        Map<String, Object> current = config;
        for (int i = 0; i < keys.length - 1; i++) {
            Object nested = current.get(keys[i]);
            if (nested instanceof Map) {
                current = (Map<String, Object>) nested;
            } else {
                return null;
            }
        }
        return current.get(keys[keys.length - 1]);
    }
}
