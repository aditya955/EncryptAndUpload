package com.eau.EncryptAndUpload.upload.gdrive;

import java.util.List;

import com.eau.EncryptAndUpload.upload.ProviderOptions;

public class GoogleDriveOptions implements ProviderOptions {
    private String applicationName;
    private int authPort;
    private String credsFilePath;
    private List<String> scopes;
    private String tokensDirPath;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public int getAuthPort() {
        return authPort;
    }

    public void setAuthPort(int authPort) {
        this.authPort = authPort;
    }

    public String getCredsFilePath() {
        return credsFilePath;
    }

    public void setCredsFilePath(String credFilePath) {
        this.credsFilePath = credFilePath;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public String getTokensDirPath() {
        return tokensDirPath;
    }

    public void setTokensDirPath(String tokensDirPath) {
        this.tokensDirPath = tokensDirPath;
    }
}
