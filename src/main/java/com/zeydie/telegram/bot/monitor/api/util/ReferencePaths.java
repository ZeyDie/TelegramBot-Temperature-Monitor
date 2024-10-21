package com.zeydie.telegram.bot.monitor.api.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public final class ReferencePaths {
    public static final @NotNull Path TOKENS_FOLDER_PATH = Paths.get(Category.TOKENS.toString());
    public static final @NotNull File TOKENS_FOLDER_FILE = TOKENS_FOLDER_PATH.toFile();

    public enum Category {
        TOKENS;

        @Override
        public @NotNull String toString() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}