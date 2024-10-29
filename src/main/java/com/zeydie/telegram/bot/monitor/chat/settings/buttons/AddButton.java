package com.zeydie.telegram.bot.monitor.chat.settings.buttons;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.zeydie.telegram.bot.monitor.TemperatureMonitorBot;
import com.zeydie.telegram.bot.monitor.api.util.TokenUtil;
import com.zeydie.telegram.bot.monitor.configs.ConfigStore;
import com.zeydie.telegram.bot.monitor.registries.ComputerRegistry;
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
public final class AddButton extends KeyboardButton {
    private final @NonNull String data;

    public AddButton() {
        this("buttons.settings.add");
    }

    public AddButton(@NonNull final String text) {
        super(text);

        this.data = text;
    }

    @SneakyThrows
    @MessageEventSubscribe
    public void add(@NonNull final MessageEvent event) {
        val senderId = event.getSender().id();
        @Nullable val data = event.getMessage().text();

        if (data == null || !data.equals(LanguageUtil.localize(senderId, this.data))) return;
        else
            event.setCancelled(true);

        if (!ComputerRegistry.isRegistry(senderId)) {
            ComputerRegistry.registry(senderId);
            SendMessageUtil.sendMessage(
                    senderId,
                    LanguageUtil.localize(senderId, "messages.settings.add.waiting")
            );
        }
    }

    @SneakyThrows
    @MessageEventSubscribe
    public void registry(@NonNull final MessageEvent event) {
        val senderId = event.getSender().id();
        @Nullable val data = event.getMessage().text();

        if (ComputerRegistry.isRegistry(senderId)) {
            event.setCancelled(true);

            val tokenModule = TemperatureMonitorBot.getInstance().getTokenModule();
            val encryptedToken = TokenUtil.encryptToken(data, senderId);

            if (tokenModule.getRegisteredTokens(senderId).size() >= ConfigStore.monitorConfig.getMaxMonitorsUsers() + 1) {
                SendMessageUtil.sendMessage(
                        senderId,
                        LanguageUtil.localize(senderId, "messages.settings.add.registered.max_monitors")
                );
                return;
            }

            if (tokenModule.isRegistered(senderId, encryptedToken)) {
                SendMessageUtil.sendMessage(
                        senderId,
                        LanguageUtil.localize(senderId, "messages.settings.add.registered.error")
                );
                return;
            }

            ComputerRegistry.unregistry(senderId);
            tokenModule.register(senderId, encryptedToken);

            SendMessageUtil.sendMessage(
                    senderId,
                    LanguageUtil.localize(senderId, "messages.settings.add.registered.ok")
                            .replace("%token%", encryptedToken),
                    ParseMode.Markdown
            );
        }
    }
}