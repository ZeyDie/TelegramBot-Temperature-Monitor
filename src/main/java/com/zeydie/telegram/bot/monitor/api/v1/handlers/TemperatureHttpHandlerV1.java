package com.zeydie.telegram.bot.monitor.api.v1.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.zeydie.sgson.SGsonBase;
import com.zeydie.telegram.bot.monitor.TemperatureMonitorBot;
import com.zeydie.telegram.bot.monitor.api.util.HttpExchangeUtil;
import com.zeydie.telegram.bot.monitor.api.util.TokenUtil;
import com.zeydie.telegram.bot.monitor.api.v1.data.ComputerData;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;

public final class TemperatureHttpHandlerV1 implements HttpHandler {
    @Override
    public void handle(@NonNull final HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equalsIgnoreCase("POST")) {
            @NonNull val json = HttpExchangeUtil.readJson(httpExchange);
            @NonNull val computerData = SGsonBase.create().fromJsonToObject(json, new ComputerData());

            @NonNull val token = computerData.getToken();

            //if (!token.isEmpty()) {
            computerData.setToken("test");
                //LoggerUtil.debug(SGsonBase.create().fromObjectToJson(computerData));

                @NonNull val instance = TemperatureMonitorBot.getInstance();

                @NonNull val encryptedData = TokenUtil.decryptToken(computerData.getToken());

                //if (instance.getTokenModule().isRegistered(encryptedData.getChatId(), token))
            instance.getTemperatureModule().addComputerData(computerData);
                    instance.getTemperatureModule().updateComputerData(computerData);
            //}
        }

        httpExchange.sendResponseHeaders(200, -1);
    }
}