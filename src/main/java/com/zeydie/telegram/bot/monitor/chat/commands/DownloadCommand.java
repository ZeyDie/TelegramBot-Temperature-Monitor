package com.zeydie.telegram.bot.monitor.chat.commands;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.zeydie.telegrambot.api.telegram.events.CommandEvent;
import com.zeydie.telegrambot.api.telegram.events.subscribes.CommandEventSubscribe;
import com.zeydie.telegrambot.api.telegram.events.subscribes.EventSubscribesRegister;
import com.zeydie.telegrambot.modules.keyboard.impl.MessageKeyboardImpl;
import lombok.NonNull;

@EventSubscribesRegister
public final class DownloadCommand {
    @CommandEventSubscribe(commands = "/download")
    public void download(final @NonNull CommandEvent event) {
        MessageKeyboardImpl.create("Download page")
                .addButton(new InlineKeyboardButton("GitHub").url("https://github.com/ZeyDie/Temperature-Monitor-Library/releases"))
                .sendKeyboard(event.getSender());
    }
}