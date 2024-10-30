package com.zeydie.telegram.bot.monitor.chat.menu;

import com.zeydie.telegram.bot.monitor.chat.menu.buttons.SettingsButton;
import com.zeydie.telegrambot.modules.keyboard.impl.UserKeyboardImpl;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public final class MenuKeyboard extends UserKeyboardImpl {
    @Getter
    private static final @NotNull MenuKeyboard instance = new MenuKeyboard();

    public MenuKeyboard() {
        super("keyboard.main_menu");

        this.minimizeButtons(true);

        this.addButton(new SettingsButton());
    }
}