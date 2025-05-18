package com.eau.EncryptAndUpload.enums;

import com.eau.EncryptAndUpload.presets.encrypt.AESDefault;
import com.eau.EncryptAndUpload.presets.encrypt.EncryptionDefaults;

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
