package com.zeydie.telegram.bot.monitor.chat.buttons;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.zeydie.telegrambot.api.telegram.events.MessageEvent;
import com.zeydie.telegrambot.api.telegram.events.subscribes.EventSubscribesRegister;
import com.zeydie.telegrambot.api.telegram.events.subscribes.MessageEventSubscribe;
import com.zeydie.telegrambot.api.utils.LanguageUtil;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.Nullable;

@EventSubscribesRegister
public final class SettingsButton extends KeyboardButton {
    private final @NonNull String data;

    public SettingsButton() {
        this("buttons.settings");
    }

    public SettingsButton(@NonNull final String text) {
        super(text);

        this.data = text;
    }

    @SneakyThrows
    @MessageEventSubscribe
    public void settings(@NonNull final MessageEvent event) {
        val senderId = event.getSender().id();
        @Nullable val data = event.getMessage().text();

        if (data != null && data.equals(LanguageUtil.localize(senderId, this.data))) {
        }
    }
}