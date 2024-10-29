package com.zeydie.telegram.bot.monitor.chat.menu.buttons;

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
public final class TemperaturesButton extends KeyboardButton {
    private final @NonNull String data;

    public TemperaturesButton() {
        this("buttons.temperatures");
    }

    public TemperaturesButton(@NonNull final String text) {
        super(text);

        this.data = text;
    }

    @SneakyThrows
    @MessageEventSubscribe
    public void temperatures(@NonNull final MessageEvent event) {
        val senderId = event.getSender().id();
        @Nullable val data = event.getMessage().text();

        if (data == null || !data.equals(LanguageUtil.localize(senderId, this.data))) return;
        else
            event.setCancelled(true);

        @NonNull val stringBuilder = new StringBuilder();

        TemperatureMonitorBot.getInstance()
                .getComputerModule()
                .getComputersData()
                .forEach(
                        computerData -> {
                            if (computerData == null)
                                return;

                            @Nullable val token = computerData.getToken();

                            if (token == null)
                                return;

                            @Nullable val cpu = computerData.getCpu();
                            @Nullable val gpu = computerData.getGpu();

                            stringBuilder.append("==========||==========").append("\n");

                            stringBuilder.append("Computer: ").append(TokenUtil.decryptToken(token).getName()).append("\n").append("\n");

                            if (cpu != null) {
                                stringBuilder.append("CPU:").append("\n");

                                if (cpu.getMinInt() > 0)
                                    stringBuilder.append("  Min: ").append(cpu.getMinInt()).append("℃").append("\n");
                                if (cpu.getAvgInt() > 0)
                                    stringBuilder.append("  AVG: ").append(cpu.getAvgInt()).append("℃").append("\n");
                                if (cpu.getMaxInt() > 0)
                                    stringBuilder.append("  Max: ").append(cpu.getMaxInt()).append("℃").append("\n");

                                stringBuilder.append("\n");
                            }

                            if (gpu != null) {
                                stringBuilder.append("GPU:").append("\n");

                                if (gpu.getMinInt() > 0)
                                    stringBuilder.append("  Min: ").append(gpu.getMinInt()).append("℃").append("\n");
                                if (gpu.getAvgInt() > 0)
                                    stringBuilder.append("  AVG: ").append(gpu.getAvgInt()).append("℃").append("\n");
                                if (gpu.getMaxInt() > 0)
                                    stringBuilder.append("  Max: ").append(gpu.getMaxInt()).append("℃").append("\n");
                            }

                            stringBuilder.append("==========||==========");

                            SendMessageUtil.sendMessage(senderId, stringBuilder.toString());
                        }
                );
    }
}