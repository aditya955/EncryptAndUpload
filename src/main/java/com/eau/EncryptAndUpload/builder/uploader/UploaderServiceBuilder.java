package com.eau.EncryptAndUpload.builder.uploader;

import com.eau.EncryptAndUpload.config.upload.UploaderConfig;
import com.eau.EncryptAndUpload.exceptions.InvalidConfigException;
import com.eau.EncryptAndUpload.exceptions.InvalidProviderException;
import com.eau.EncryptAndUpload.upload.CloudUploader;

/**
 * Service builder for constructing and configuring a {@link CloudUploader} instance.
 * <p>
 * This class uses a provided {@link UploaderConfig} to select and build the appropriate
 * uploader for a specific cloud provider. It handles configuration validation and
 * provider selection, returning a ready-to-use {@code CloudUploader}.
 * </p>
 */
public class UploaderServiceBuilder {
    private final UploaderConfig config;

    /**
     * Constructs a new {@code UploaderServiceBuilder} with the specified configuration.
     *
     * @param config the uploader configuration to use
     */
    public UploaderServiceBuilder(UploaderConfig config) {
        this.config = config;
    }

    /**
     * Builds and returns a configured {@link CloudUploader} instance for the specified provider.
     * <p>
     * Handles configuration and provider validation. Returns {@code null} if configuration is invalid
     * or the provider is not supported.
     * </p>
     *
     * @return a new {@code CloudUploader} instance, or {@code null} on error
     */
    public CloudUploader build() {
        try {
            UploaderBuilder builder = config.getBuilder();
            return builder.build();
        } catch (InvalidConfigException e) {
            System.out.println("[-] Invalid config..." + e);
        } catch (InvalidProviderException e) {
            System.out.println("[-] Invalid provider..." + e);
        }
        return null;
    }
}
