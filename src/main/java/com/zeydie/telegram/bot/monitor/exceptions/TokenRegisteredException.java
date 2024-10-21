package com.zeydie.telegram.bot.monitor.exceptions;

import com.zeydie.telegram.bot.monitor.api.data.TokenData;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

@Getter
public class TokenRegisteredException extends Exception {
    private final @NotNull String message;

    public TokenRegisteredException(@NonNull final TokenData tokenData) {
        this(tokenData.getToken());
    }

    public TokenRegisteredException(@NonNull final String token) {
        this.message = String.format("%s is already registered!", token);
    }
}