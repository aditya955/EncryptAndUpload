package com.eau.EncryptAndUpload.service;

import com.eau.EncryptAndUpload.defaults.encryptionDefault.AESDefault;
import com.eau.EncryptAndUpload.defaults.encryptionDefault.EncryptionDefaults;

public enum EncryptionTypes {
    AES128(AESDefault.getAes128Default()),
    AES256(AESDefault.getAes256Default());

    private final EncryptionDefaults defaults;

    EncryptionTypes(EncryptionDefaults defaults) {
        this.defaults = defaults;
    }

    public EncryptionDefaults getDefaults() {
        return defaults;
    }
}
