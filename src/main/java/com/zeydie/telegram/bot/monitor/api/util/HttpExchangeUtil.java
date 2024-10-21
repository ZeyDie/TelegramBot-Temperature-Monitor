package com.zeydie.telegram.bot.monitor.api.util;

import com.sun.net.httpserver.HttpExchange;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public final class HttpExchangeUtil {
    public static final @NotNull String readJson(@NonNull final HttpExchange httpExchange) throws IOException {
        @NonNull val inputStream = httpExchange.getRequestBody();

        try (
                @NonNull val inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                @NonNull val bufferedReader = new BufferedReader(inputStreamReader);
        ) {
            @NonNull val stringBuilder = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null)
                stringBuilder.append(line);

            return stringBuilder.toString();
        }
    }
}