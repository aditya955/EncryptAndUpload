package com.eau.EncryptAndUpload.util.config;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigLoader {
    public static JsonConfig getJsonConfig(String configFilePath) throws StreamReadException, DatabindException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

        JsonConfig config = mapper.readValue(
            new File(configFilePath), 
            JsonConfig.class
        );
        return config;
    }
}
