package com.zeydie.telegram.bot.monitor.chat.buttons;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.zeydie.sgson.SGsonBase;
import com.zeydie.telegram.bot.monitor.TemperatureMonitorBot;
import com.zeydie.telegrambot.api.telegram.events.MessageEvent;
import com.zeydie.telegrambot.api.telegram.events.subscribes.EventSubscribesRegister;
import com.zeydie.telegrambot.api.telegram.events.subscribes.MessageEventSubscribe;
import com.zeydie.telegrambot.api.utils.LanguageUtil;
import com.zeydie.telegrambot.api.utils.LoggerUtil;
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

        if (data != null && data.equals(LanguageUtil.localize(senderId, this.data))) {
            val stringBuilder = new StringBuilder();

            val computerData = TemperatureMonitorBot.getInstance()
                    .getTemperatureModule()
                    .getComputerData("test");

            System.out.println(computerData);

            if (computerData == null) {
                event.setCancelled(true);
                return;
            }


            LoggerUtil.debug(SGsonBase.create().fromObjectToJson(computerData));

            val cpu = computerData.getCpu();
            val gpu = computerData.getGpu();

            stringBuilder.append("==========vv==========");

            stringBuilder.append("Computer: ").append(computerData.getToken());
            stringBuilder.append("CPU:");

            if (cpu.getMinDouble() > 0.0)
                stringBuilder.append("  Min: ").append(cpu.getMinDouble()).append("℃");
            if (cpu.getAvgDouble() > 0.0)
                stringBuilder.append("  AVG: ").append(cpu.getAvgDouble()).append("℃");
            if (cpu.getMaxDouble() > 0.0)
                stringBuilder.append("  Max: ").append(cpu.getMaxDouble()).append("℃");

            stringBuilder.append("GPU:");

            if (gpu.getMinDouble() > 0.0)
                stringBuilder.append("  Min: ").append(gpu.getMinDouble()).append("℃");
            if (gpu.getAvgDouble() > 0.0)
                stringBuilder.append("  AVG: ").append(gpu.getAvgDouble()).append("℃");
            if (gpu.getMaxDouble() > 0.0)
                stringBuilder.append("  Max: ").append(gpu.getMaxDouble()).append("℃");

            stringBuilder.append("==========^^==========");

            LoggerUtil.debug("Send message to user " + stringBuilder);
            TemperatureMonitorBot.getTelegramBotCore().sendMessage(senderId, stringBuilder.toString());

        }
    }
}