package com.zeydie.telegram.bot.monitor.modules.token;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zeydie.sgson.SGsonFile;
import com.zeydie.telegram.bot.monitor.api.data.TokenData;
import com.zeydie.telegram.bot.monitor.api.modules.token.IToken;
import com.zeydie.telegram.bot.monitor.exceptions.TokenRegisteredException;
import com.zeydie.telegrambot.api.utils.FileUtil;
import com.zeydie.telegrambot.api.utils.LoggerUtil;
import com.zeydie.telegrambot.api.utils.ReferencePaths;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.zeydie.telegram.bot.monitor.api.util.ReferencePaths.TOKENS_FOLDER_FILE;
import static com.zeydie.telegram.bot.monitor.api.util.ReferencePaths.TOKENS_FOLDER_PATH;

public class TokenImpl implements IToken {
    private final @NotNull Map<Long, List<TokenData>> usersTokens = Maps.newHashMap();

    @Override
    public void preInit() {
        FileUtil.createFolder(TOKENS_FOLDER_PATH);
    }

    @Override
    public void init() {
        @Nullable val files = TOKENS_FOLDER_FILE.listFiles();

        if (files != null)
            try (val stream = Arrays.stream(files)) {
                stream.forEach(file -> {
                            try {
                                LoggerUtil.info("Restoring {}", file.getName());

                                val userId = Long.parseLong(FileUtil.getFileName(file));
                                @NonNull val tokenDatas = SGsonFile.createPretty(file).fromJsonToObject(new TokenData[]{});

                                if (tokenDatas.length <= 0) FileUtil.deleteFile(file);
                                else {
                                    LoggerUtil.info("User {} restored {}", userId, tokenDatas);
                                    this.usersTokens.put(userId, Arrays.asList(tokenDatas));
                                }
                            } catch (final Exception exception) {
                                exception.printStackTrace(System.out);
                            }
                        }
                );
            }
    }

    @Override
    public void postInit() {

    }

    @Override
    public void save() {
        this.usersTokens.forEach(
                (userId, tokenDatas) -> {
                    if (tokenDatas.isEmpty()) return;

                    @NonNull val file = FileUtil.createFileWithNameAndType(TOKENS_FOLDER_PATH, userId, ReferencePaths.CONFIG_TYPE);

                    LoggerUtil.info("Saving {}", file);

                    SGsonFile.create(file).writeJsonFile(tokenDatas.toArray(new TokenData[]{}));
                }
        );
    }

    @Override
    public boolean register(
            final long userId,
            @NonNull final String token
    ) throws TokenRegisteredException {
        @NonNull val datas = this.usersTokens.getOrDefault(userId, Lists.newArrayList());

        if (datas.isEmpty()) {
            datas.add(new TokenData(token));
            this.usersTokens.put(userId, datas);
            return true;
        }

        try (val stream = datas.stream()) {
            if (stream.anyMatch(tokenData -> tokenData.getToken().equals(token)))
                throw new TokenRegisteredException(token);

            datas.add(new TokenData(token));
            this.usersTokens.put(userId, datas);
            return true;
        }
    }

    @Override
    public @NotNull List<TokenData> getRegisteredTokens(final long userId) {
        return this.usersTokens.getOrDefault(userId, Lists.newArrayList());
    }

    @Override
    public boolean isRegistered(final long userId, @NonNull final TokenData tokenData) {
        return this.isRegistered(userId, tokenData.getToken());
    }

    @Override
    public boolean isRegistered(final long userId, @NonNull final String token) {
        try (val stream = this.usersTokens.getOrDefault(userId, Lists.newArrayList()).stream()) {
            return stream.filter(tokenData -> tokenData.getToken().equals(token))
                    .findFirst()
                    .isPresent();
        }
    }

    @Override
    public @Nullable TokenData getTokenData(final long userId, @NonNull final String token) {
        try (val stream = this.usersTokens.getOrDefault(userId, Lists.newArrayList()).stream()) {
            return stream.filter(tokenData -> tokenData.getToken().equals(token))
                    .findFirst()
                    .orElse(null);
        }
    }

    @Override
    public void removeTokenDatas(final long userId) {
        this.usersTokens.remove(userId);
    }

    @Override
    public void removeTokenData(
            final long userId,
            @NonNull final String token
    ) {
        this.usersTokens.getOrDefault(userId, Lists.newArrayList())
                .removeIf(tokenData -> tokenData.getToken().equals(token));
    }
}