package com.zeydie.telegram.bot.monitor.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.zeydie.sgson.SGsonBase;
import com.zeydie.telegram.bot.monitor.TemperatureMonitor;
import com.zeydie.telegram.bot.monitor.api.data.ComputerData;
import com.zeydie.telegram.bot.monitor.api.util.HttpExchangeUtil;
import com.zeydie.telegram.bot.monitor.api.util.TokenUtil;
import com.zeydie.telegrambot.api.utils.LoggerUtil;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;

public final class TemperatureHttpHandler implements HttpHandler {
    @Override
    public void handle(@NonNull final HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equalsIgnoreCase("POST")) {
            @NonNull val json = HttpExchangeUtil.readJson(httpExchange);
            @NonNull val computerData = SGsonBase.create().fromJsonToObject(json, new ComputerData());

            @NonNull val token = computerData.getToken();

            if (!token.isEmpty()) {
                LoggerUtil.debug(SGsonBase.create().fromObjectToJson(computerData));

                @NonNull val instance = TemperatureMonitor.getInstance();

                @NonNull val encryptedData = TokenUtil.decryptToken(computerData.getToken());

                if (instance.getTokenModule().isRegistered(encryptedData.getChatId(), token))
                    instance.getTemperatureModule().updateComputerData(computerData);
            }
        }

        httpExchange.sendResponseHeaders(200, -1);
    }
}