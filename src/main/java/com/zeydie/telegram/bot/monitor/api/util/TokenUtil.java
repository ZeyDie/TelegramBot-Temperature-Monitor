package com.zeydie.telegram.bot.monitor.api.util;

import com.zeydie.sgson.SGsonBase;
import com.zeydie.telegram.bot.monitor.api.v1.data.EncryptedData;
import lombok.NonNull;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jetbrains.annotations.NotNull;

public final class TokenUtil {
    public static @NotNull String encryptToken(
            @NonNull final String name,
            final long userId
    ) {
        return new StandardPBEStringEncryptor().encrypt(SGsonBase.create().fromObjectToJson(new EncryptedData(name, userId)));
    }

    public static @NotNull EncryptedData decryptToken(@NonNull final String token) {
        return SGsonBase.create()
                .fromJsonToObject(
                        new StandardPBEStringEncryptor().decrypt(token),
                        new EncryptedData()
                );
    }
}