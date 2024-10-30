package com.zeydie.telegram.bot.monitor.chat.commands;

import com.zeydie.telegram.bot.monitor.TemperatureMonitorBot;
import com.zeydie.telegram.bot.monitor.api.util.TokenUtil;
import com.zeydie.telegrambot.api.telegram.events.CommandEvent;
import com.zeydie.telegrambot.api.telegram.events.subscribes.CommandEventSubscribe;
import com.zeydie.telegrambot.api.telegram.events.subscribes.EventSubscribesRegister;
import com.zeydie.telegrambot.api.utils.SendMessageUtil;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;

@EventSubscribesRegister
public final class TemperatureCommand {
    @CommandEventSubscribe(commands = "/temperature")
    public void temperature(final @NonNull CommandEvent event) {
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
                            val timestamp = computerData.getLastUpdateTimestamp();

                            @NonNull val stringBuilder = new StringBuilder();

                            stringBuilder.append("==========||==========").append("\n");

                            stringBuilder.append("Computer: ")
                                    .append(TokenUtil.decryptToken(token).getName())
                                    .append("\n")
                                    .append("\n");

                            if (cpu != null) {
                                stringBuilder.append("CPU:")
                                        .append("\n");

                                if (cpu.getMinInt() > 0)
                                    stringBuilder.append("  Min: ")
                                            .append(cpu.getMinInt())
                                            .append("℃")
                                            .append("\n");
                                if (cpu.getAvgInt() > 0)
                                    stringBuilder.append("  AVG: ")
                                            .append(cpu.getAvgInt())
                                            .append("℃")
                                            .append("\n");
                                if (cpu.getMaxInt() > 0)
                                    stringBuilder.append("  Max: ")
                                            .append(cpu.getMaxInt())
                                            .append("℃")
                                            .append("\n");

                                stringBuilder.append("\n");
                            }

                            if (gpu != null) {
                                stringBuilder.append("GPU:")
                                        .append("\n");

                                if (gpu.getMinInt() > 0)
                                    stringBuilder.append("  Min: ")
                                            .append(gpu.getMinInt())
                                            .append("℃")
                                            .append("\n");
                                if (gpu.getAvgInt() > 0)
                                    stringBuilder.append("  AVG: ")
                                            .append(gpu.getAvgInt())
                                            .append("℃")
                                            .append("\n");
                                if (gpu.getMaxInt() > 0)
                                    stringBuilder.append("  Max: ")
                                            .append(gpu.getMaxInt())
                                            .append("℃")
                                            .append("\n");
                            }

                            stringBuilder.append("\n")
                                    .append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp))
                                    .append("\n");

                            stringBuilder.append("==========||==========");

                            SendMessageUtil.sendMessage(
                                    event.getSender(),
                                    stringBuilder.toString()
                            );
                        }
                );
    }
}