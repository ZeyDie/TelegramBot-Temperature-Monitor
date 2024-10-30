package com.zeydie.telegram.bot.monitor.api.v2.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.zeydie.sgson.SGsonBase;
import com.zeydie.telegram.bot.monitor.TemperatureMonitorBot;
import com.zeydie.telegram.bot.monitor.api.util.HttpExchangeUtil;
import com.zeydie.telegram.bot.monitor.api.util.TokenUtil;
import com.zeydie.telegram.bot.monitor.api.v2.data.ComputerData;
import com.zeydie.telegrambot.api.utils.LoggerUtil;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public final class TemperatureHttpHandlerV2 implements HttpHandler {
    @Override
    public void handle(@NonNull final HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equalsIgnoreCase("POST")) {
            @NonNull val json = HttpExchangeUtil.readJson(httpExchange);

            LoggerUtil.debug(json);

            @NonNull val computerData = SGsonBase.create().fromJsonToObject(json, new ComputerData());

            @Nullable val token = computerData.getToken();

            if (token != null) {
                @NonNull val encryptedData = TokenUtil.decryptToken(token);

                @NonNull val instance = TemperatureMonitorBot.getInstance();
                @NonNull val tokenModule = instance.getTokenModule();
                @NonNull val computerModule = instance.getComputerModule();

                if (tokenModule.isRegistered(encryptedData.getChatId(), token))
                    computerModule.updateComputerData(computerData);
            }

            httpExchange.sendResponseHeaders(200, -1);
        }
    }
}
