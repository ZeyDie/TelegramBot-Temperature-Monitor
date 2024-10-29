package com.zeydie.telegram.bot.monitor.api.util;

import com.zeydie.sgson.SGsonBase;
import com.zeydie.telegram.bot.monitor.api.v1.data.EncryptedData;
import com.zeydie.telegram.bot.monitor.configs.ConfigStore;
import lombok.NonNull;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jetbrains.annotations.NotNull;

public final class TokenUtil {
    private static final @NotNull StandardPBEStringEncryptor ENCRYPTOR = new StandardPBEStringEncryptor();

    static {
        ENCRYPTOR.setPassword(ConfigStore.monitorConfig.getEncryptionKey());
    }

    public static @NotNull String encryptToken(
            @NonNull final String name,
            final long userId
    ) {
        return ENCRYPTOR.encrypt(SGsonBase.create().fromObjectToJson(new EncryptedData(name, userId)));
    }

    public static @NotNull EncryptedData decryptToken(@NonNull final String token) {
        return SGsonBase.create()
                .fromJsonToObject(
                        ENCRYPTOR.decrypt(token),
                        new EncryptedData()
                );
    }
}