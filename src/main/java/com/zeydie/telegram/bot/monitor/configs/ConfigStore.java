package com.zeydie.telegram.bot.monitor.configs;

import com.zeydie.telegrambot.api.events.config.ConfigSubscribe;
import com.zeydie.telegrambot.api.events.subscribes.ConfigSubscribesRegister;
import org.jetbrains.annotations.NotNull;

@ConfigSubscribesRegister
public final class ConfigStore {
    @ConfigSubscribe(name = "config", path = "monitor")
    public static @NotNull MonitorConfig monitorConfig = new MonitorConfig();
}