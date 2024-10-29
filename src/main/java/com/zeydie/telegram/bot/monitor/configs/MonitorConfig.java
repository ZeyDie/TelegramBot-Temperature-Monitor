package com.zeydie.telegram.bot.monitor.configs;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;

@Data
public final class MonitorConfig {
    private @NotNull String encryptionKey = RandomStringUtils.secureStrong().nextAlphabetic(8, 16);

    private int maxMonitorsUsers = 3;
}