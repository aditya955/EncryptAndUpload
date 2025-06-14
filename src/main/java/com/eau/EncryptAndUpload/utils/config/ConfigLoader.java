package com.eau.EncryptAndUpload.utils.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.eau.EncryptAndUpload.config.encrypt.EncryptorConfig;
import com.eau.EncryptAndUpload.config.upload.UploaderConfig;
import com.eau.EncryptAndUpload.enums.CloudProvider;
import com.eau.EncryptAndUpload.enums.EncryptionTypes;

public class ConfigLoader {
    public static AppConfig getPropertiesConfig(String configFilePath) throws FileNotFoundException, IOException {
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream(configFilePath)) {
            props.load(input);

            String algo = props.getProperty("encryption.algorithm");
            String provider = props.getProperty("upload.provider");

            EncryptorConfig eConfig = new EncryptorConfig(EncryptionTypes.valueOf(algo));
            UploaderConfig uConfig = new UploaderConfig(CloudProvider.valueOf(provider));

            for (String key : props.stringPropertyNames()) {
                String value = props.getProperty(key);

                if (key.startsWith("encryption.")) {
                    String subKey = key.substring("encryption.".length());
                    eConfig.set(subKey, value);
                } else if (key.startsWith("upload.")) {
                    String subKey = key.substring("upload.".length());
                    uConfig.set(subKey, value);
                }
            }

            AppConfig config = new AppConfig(eConfig, uConfig);
            return config;
        }
    }
}

