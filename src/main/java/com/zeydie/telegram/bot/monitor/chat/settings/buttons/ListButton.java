package com.zeydie.telegram.bot.monitor.chat.settings.buttons;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.zeydie.telegram.bot.monitor.TemperatureMonitorBot;
import com.zeydie.telegram.bot.monitor.api.util.TokenUtil;
import com.zeydie.telegrambot.api.telegram.events.MessageEvent;
import com.zeydie.telegrambot.api.telegram.events.subscribes.EventSubscribesRegister;
import com.zeydie.telegrambot.api.telegram.events.subscribes.MessageEventSubscribe;
import com.zeydie.telegrambot.api.utils.LanguageUtil;
import com.zeydie.telegrambot.api.utils.SendMessageUtil;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.Nullable;

@EventSubscribesRegister
public final class ListButton extends KeyboardButton {
    private final @NonNull String data;

    public ListButton() {
        this("buttons.settings.list");
    }

    public ListButton(@NonNull final String text) {
        super(text);

        this.data = text;
    }

    @SneakyThrows
    @MessageEventSubscribe
    public void list(@NonNull final MessageEvent event) {
        val senderId = event.getSender().id();
        @Nullable val data = event.getMessage().text();

        if (data == null || !data.equals(LanguageUtil.localize(senderId, this.data))) return;
        else
            event.setCancelled(true);

        @NonNull val tokenModule = TemperatureMonitorBot.getInstance().getTokenModule();
        @NonNull val tokens = tokenModule.getRegisteredTokens(senderId);

        if (tokens.isEmpty())
            SendMessageUtil.sendMessage(
                    senderId,
                    LanguageUtil.localize(senderId, "messages.settings.list.empty")
            );
        else {
            @NonNull val stringBuilder = new StringBuilder(
                    LanguageUtil.localize(
                            senderId,
                            "messages.settings.list.computers"
                    )
            ).append("\n\n");

            tokens.forEach(
                    tokenData -> {
                        @Nullable val token = tokenData.getToken();

                        if (token == null) return;

                        stringBuilder.append("  - ").append(TokenUtil.decryptToken(token).getName()).append("\n");
                    }
            );

            SendMessageUtil.sendMessage(senderId, stringBuilder.toString());
        }
    }
}