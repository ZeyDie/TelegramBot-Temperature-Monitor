package com.zeydie.telegram.bot.monitor;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.Service;
import com.sun.net.httpserver.HttpServer;
import com.zeydie.telegram.bot.monitor.api.modules.computer.IComputer;
import com.zeydie.telegram.bot.monitor.api.modules.token.IToken;
import com.zeydie.telegram.bot.monitor.api.v1.handlers.TemperatureHttpHandlerV1;
import com.zeydie.telegram.bot.monitor.modules.computer.ComputerImpl;
import com.zeydie.telegram.bot.monitor.modules.token.TokenImpl;
import com.zeydie.telegrambot.TelegramBotCore;
import com.zeydie.telegrambot.api.modules.interfaces.ISubcore;
import com.zeydie.telegrambot.api.utils.LoggerUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.NonFinal;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public final class TemperatureMonitorBot implements ISubcore {
    @Getter
    private static final @NotNull TemperatureMonitorBot instance = new TemperatureMonitorBot();

    @Getter
    private static final @NotNull TelegramBotCore telegramBotCore = TelegramBotCore.getInstance();

    @Getter
    private IToken tokenModule;
    @Getter
    private IComputer computerModule;

    private final @NotNull Service saveScheduler = new AbstractScheduledService() {
        @Override
        protected void runOneIteration() throws Exception {
            CompletableFuture.runAsync(tokenModule::save);
        }

        @Override
        protected @NotNull Scheduler scheduler() {
            return Scheduler.newFixedRateSchedule(0, 1, TimeUnit.MINUTES);
        }
    };

    @SneakyThrows
    public static void main(@Nullable final String[] args) {
        LoggerUtil.info("Starting {}...", TemperatureMonitorBot.class.getName());

        LoggerUtil.info("Register subcore {}...", instance.getName());
        telegramBotCore.registerSubcore(instance);

        LoggerUtil.info("Launch {}...", telegramBotCore.getName());
        telegramBotCore.launch(args);
    }

    @Override
    public @NotNull String getName() {
        return this.getClass().getName();
    }

    @NonFinal
    @Parameter(names = {"-p", "-port"}, description = "Port for HTTP server")
    private int port = 8100;

    @Override
    public void launch(@Nullable final String[] strings) {
        JCommander.newBuilder()
                .addObject(instance)
                .build()
                .parse(strings);

        this.tokenModule = new TokenImpl();
        this.computerModule = new ComputerImpl();
    }

    @Override
    public void stop() {
        this.tokenModule.save();
    }

    @Override
    public void preInit() {
        this.tokenModule.preInit();
    }

    @Override
    public void init() {
        this.tokenModule.init();
    }

    @SneakyThrows
    @Override
    public void postInit() {
        this.tokenModule.postInit();

        this.saveScheduler.startAsync();

        @NonNull val httpServer = HttpServer.create(new InetSocketAddress(this.port), 0);

        httpServer.createContext("/api/v1/temperature", new TemperatureHttpHandlerV1());
        httpServer.start();

        LoggerUtil.warn("HTTP server started on port {}", this.port);
    }
}