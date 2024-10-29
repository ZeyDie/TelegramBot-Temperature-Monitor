package com.zeydie.telegram.bot.monitor.chat.settings.buttons;

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
public final class DeleteButton  extends KeyboardButton {
    private final @NonNull String data;

    public DeleteButton() {
        this("buttons.settings.delete");
    }

    public DeleteButton(@NonNull final String text) {
        super(text);

        this.data = text;
    }

    @SneakyThrows
    @MessageEventSubscribe
    public void delete(@NonNull final MessageEvent event) {
        val senderId = event.getSender().id();
        @Nullable val data = event.getMessage().text();

        if (data == null || !data.equals(LanguageUtil.localize(senderId, this.data))) return;
        else
            event.setCancelled(true);
    }
}