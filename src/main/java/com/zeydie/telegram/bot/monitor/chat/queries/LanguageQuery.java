package com.zeydie.telegram.bot.monitor.chat.queries;

import com.zeydie.telegram.bot.monitor.chat.buttons.SettingsButton;
import com.zeydie.telegram.bot.monitor.chat.buttons.TemperaturesButton;
import com.zeydie.telegrambot.api.events.EventPriority;
import com.zeydie.telegrambot.api.events.subscribes.PrioritySubscribe;
import com.zeydie.telegrambot.api.telegram.events.CallbackQueryEvent;
import com.zeydie.telegrambot.api.telegram.events.subscribes.CallbackQueryEventSubscribe;
import com.zeydie.telegrambot.api.telegram.events.subscribes.EventSubscribesRegister;
import com.zeydie.telegrambot.modules.keyboard.impl.UserKeyboardImpl;
import lombok.NonNull;
import lombok.val;

@EventSubscribesRegister
public final class LanguageQuery {
    @PrioritySubscribe(priority = EventPriority.LOWEST)
    @CallbackQueryEventSubscribe(callbacks = "language.select", startWith = true)
    public void select(@NonNull final CallbackQueryEvent event) {
        @NonNull val datas = event.getCallbackQuery().data().split("\\.");
        val userId = Long.parseLong(datas[datas.length - 1]);

        new UserKeyboardImpl()
                .minimizeButtons(true)
                .addButton(new TemperaturesButton())
                .addButton(new SettingsButton())
                .sendKeyboard(userId, "keyboard.menu");
    }
}