package com.eau.EncryptAndUpload.builder.uploader;

import com.eau.EncryptAndUpload.upload.CloudUploader;

/**
 * Builder interface for constructing {@link CloudUploader} instances for different cloud providers.
 * <p>
 * Implementations of this interface should provide the logic to build and configure
 * a specific {@code CloudUploader} (e.g., Google Drive, Dropbox, etc.) based on the
 * provided configuration.
 * </p>
 */
public interface UploaderBuilder {
    /**
     * Builds and returns a configured {@link CloudUploader} instance.
     *
     * @return a new {@code CloudUploader} instance
     */
    public CloudUploader build();
}
