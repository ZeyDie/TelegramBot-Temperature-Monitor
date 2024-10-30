package com.zeydie.telegram.bot.monitor.chat.settings;

import com.zeydie.telegram.bot.monitor.chat.menu.buttons.MenuButton;
import com.zeydie.telegram.bot.monitor.chat.settings.buttons.AddButton;
import com.zeydie.telegram.bot.monitor.chat.settings.buttons.DeleteButton;
import com.zeydie.telegram.bot.monitor.chat.settings.buttons.ListButton;
import com.zeydie.telegrambot.modules.keyboard.impl.UserKeyboardImpl;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public final class SettingsKeyboard extends UserKeyboardImpl {
    @Getter
    private static final @NotNull SettingsKeyboard instance = new SettingsKeyboard();

    public SettingsKeyboard() {
        super("keyboard.settings");

        this.minimizeButtons(true);

        this.addButton(new ListButton());
        this.completeRow();
        this.addButton(new AddButton());
        this.addButton(new DeleteButton());
        this.completeRow();
        this.addButton(new MenuButton());
    }
}