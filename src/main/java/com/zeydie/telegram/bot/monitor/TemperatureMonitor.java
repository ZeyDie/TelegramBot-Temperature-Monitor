package com.zeydie.telegram.bot.monitor;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.sun.net.httpserver.HttpServer;
import com.zeydie.telegram.bot.monitor.api.modules.temperature.IComputer;
import com.zeydie.telegram.bot.monitor.api.modules.token.IToken;
import com.zeydie.telegram.bot.monitor.handlers.TemperatureHttpHandler;
import com.zeydie.telegram.bot.monitor.modules.temperature.ComputerImpl;
import com.zeydie.telegram.bot.monitor.modules.token.TokenImpl;
import com.zeydie.telegrambot.TelegramBotCore;
import com.zeydie.telegrambot.api.modules.interfaces.ISubcore;
import com.zeydie.telegrambot.api.utils.LoggerUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;

public final class TemperatureMonitor implements ISubcore {
    @Getter
    private static final @NotNull TemperatureMonitor instance = new TemperatureMonitor();
    @Getter
    private static final @NotNull TelegramBotCore telegramBotCore = TelegramBotCore.getInstance();

    @Getter
    private final IToken token = new TokenImpl();
    @Getter
    private final IComputer temperature = new ComputerImpl();

    @SneakyThrows
    public static void main(@Nullable final String[] args) {
        LoggerUtil.info("Starting {}...", TemperatureMonitor.class.getName());

        LoggerUtil.info("Register subcore {}...", instance.getName());
        telegramBotCore.registerSubcore(instance);

        LoggerUtil.info("Launch {}...", telegramBotCore.getName());
        telegramBotCore.launch(args);
    }

    @Override
    public @NotNull String getName() {
        return this.getClass().getName();
    }

    @Parameter(names = "-port", description = "Port for HTTP server")
    private int port = 7777;

    @Override
    public void launch(@Nullable final String[] strings) {
        JCommander.newBuilder()
                .addObject(instance)
                .build()
                .parse(strings);
    }

    @Override
    public void stop() {
        this.token.save();
    }

    @Override
    public void preInit() {
        this.token.preInit();
    }

    @Override
    public void init() {
        this.token.init();
    }

    @SneakyThrows
    @Override
    public void postInit() {
        this.token.postInit();

        val httpServer = HttpServer.create(new InetSocketAddress(this.port), 0);

        httpServer.createContext("/api/v1/temperature", new TemperatureHttpHandler());
        httpServer.start();

        LoggerUtil.info("HTTP server started on port {}", this.port);
    }
}