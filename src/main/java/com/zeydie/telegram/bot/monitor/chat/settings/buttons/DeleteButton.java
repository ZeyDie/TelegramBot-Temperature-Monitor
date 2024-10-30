package com.zeydie.telegram.bot.monitor.chat.settings.buttons;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.zeydie.telegram.bot.monitor.TemperatureMonitorBot;
import com.zeydie.telegram.bot.monitor.api.util.TokenUtil;
import com.zeydie.telegrambot.api.telegram.events.CallbackQueryEvent;
import com.zeydie.telegrambot.api.telegram.events.MessageEvent;
import com.zeydie.telegrambot.api.telegram.events.subscribes.CallbackQueryEventSubscribe;
import com.zeydie.telegrambot.api.telegram.events.subscribes.EventSubscribesRegister;
import com.zeydie.telegrambot.api.telegram.events.subscribes.MessageEventSubscribe;
import com.zeydie.telegrambot.api.utils.LanguageUtil;
import com.zeydie.telegrambot.api.utils.SendMessageUtil;
import com.zeydie.telegrambot.modules.keyboard.impl.MessageKeyboardImpl;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.Nullable;

@EventSubscribesRegister
public final class DeleteButton extends KeyboardButton {
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

        @NonNull val tokenModule = TemperatureMonitorBot.getInstance().getTokenModule();
        @NonNull val tokens = tokenModule.getRegisteredTokens(senderId);

        if (tokens.isEmpty())
            SendMessageUtil.sendMessage(
                    senderId,
                    LanguageUtil.localize(senderId, "messages.settings.list.empty")
            );
        else {
            @NonNull val keyboard = MessageKeyboardImpl.create("keyboard.delete");

            tokens.forEach(
                    tokenData -> {
                        @Nullable val token = tokenData.getToken();

                        if (token == null) return;

                        @NonNull val encrtypedToken = TokenUtil.decryptToken(token);

                        keyboard.addButton(
                                new InlineKeyboardButton(
                                        LanguageUtil.localize(
                                                senderId,
                                                "messages.settings.delete.computer"
                                        ).replaceAll("%computer%", encrtypedToken.getName())
                                ).callbackData(
                                        String.format(
                                                "delete.token.%s.%d",
                                                encrtypedToken.getName(),
                                                encrtypedToken.getChatId()
                                        )
                                )
                        );
                    }
            );

            keyboard.sendKeyboard(senderId);
        }
    }

    @CallbackQueryEventSubscribe(callbacks = "delete.token.", startWith = true)
    public void delete(@NonNull final CallbackQueryEvent event) {
        @NonNull val datas = event.getCallbackQuery().data().split("\\.");
        val userId = Long.parseLong(datas[datas.length - 1]);
        @NonNull val computer = datas[datas.length - 2];

        TemperatureMonitorBot.getInstance()
                .getTokenModule()
                .getRegisteredTokens(userId)
                .removeIf(
                        tokenData -> {
                            @Nullable val token = tokenData.getToken();

                            if (token == null) return false;

                            return TokenUtil.decryptToken(token).getName().equals(computer);
                        }
                );

        @NonNull val sender = event.getSender();

        SendMessageUtil.sendMessage(
                sender,
                LanguageUtil.localize(
                        sender,
                        "messages.settings.delete.ok"
                ).replaceAll("%computer%", computer)
        );
    }
}