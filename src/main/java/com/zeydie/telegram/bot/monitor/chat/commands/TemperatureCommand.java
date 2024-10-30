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
        @NonNull val sender = event.getSender();

        try (
                @NonNull val stream = TemperatureMonitorBot.getInstance()
                        .getComputerModule()
                        .getComputersData()
                        .stream()
        ) {
            @NonNull val filtered = stream.filter(
                    computerData -> {
                        @Nullable val token = computerData.getToken();

                        return computerData != null && token != null && TokenUtil.decryptToken(token).getChatId() == sender.id();
                    }
            ).toList();

            if (filtered.isEmpty())
                SendMessageUtil.sendMessage(
                        sender,
                        "messages.temperatures.empty"
                );
            else
                filtered.forEach(
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
                                @NonNull val cpuHardwares = cpu.getHardwares();

                                if (!cpuHardwares.isEmpty()) {
                                    stringBuilder.append("CPU:")
                                            .append("\n");

                                    cpuHardwares.forEach(
                                            (name, value) -> stringBuilder.append(String.format("  %s: ", name))
                                                    .append(value)
                                                    .append("℃")
                                                    .append("\n")
                                    );

                                    stringBuilder.append("\n");
                                }
                            }

                            if (gpu != null) {
                                @NonNull val gpuHardwares = gpu.getHardwares();

                                if (!gpuHardwares.isEmpty()) {
                                    stringBuilder.append("GPU:")
                                            .append("\n");

                                    gpuHardwares.forEach(
                                            (name, value) -> stringBuilder.append(String.format("  %s: ", name))
                                                    .append(value)
                                                    .append("℃")
                                                    .append("\n")
                                    );

                                    stringBuilder.append("\n");
                                }
                            }

                            stringBuilder.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp)).append("\n");
                            stringBuilder.append("==========||==========");

                            SendMessageUtil.sendMessage(
                                    sender,
                                    stringBuilder.toString()
                            );
                        }
                );
        }
    }
}