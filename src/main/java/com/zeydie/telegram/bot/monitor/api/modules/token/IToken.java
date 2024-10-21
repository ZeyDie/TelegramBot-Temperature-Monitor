package com.zeydie.telegram.bot.monitor.api.modules.token;

import com.zeydie.telegram.bot.monitor.api.data.TokenData;
import com.zeydie.telegram.bot.monitor.exceptions.TokenRegisteredException;
import com.zeydie.telegrambot.api.modules.interfaces.IData;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IToken extends IData {
    boolean register(
            final long userId,
            @NonNull final String string
    ) throws TokenRegisteredException;

    @NotNull
    List<TokenData> getRegisteredTokens(final long userId);

    boolean isRegistered(
            final long userId,
            @NonNull final TokenData tokenData
    );

    boolean isRegistered(
            final long userId,
            @NonNull final String token
    );

    @Nullable TokenData getTokenData(
            final long userId,
            @NonNull final String token
    );

    void removeTokenDatas(final long userId);

    void removeTokenData(
            final long userId,
            @NonNull final String token
    );
}