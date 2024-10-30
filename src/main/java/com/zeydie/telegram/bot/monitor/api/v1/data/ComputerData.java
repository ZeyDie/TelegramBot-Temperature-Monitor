package com.zeydie.telegram.bot.monitor.api.v1.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComputerData {
    private @Nullable String token;

    private @Nullable TemperatureData cpu;
    private @Nullable TemperatureData gpu;

    private long lastUpdateTimestamp;
}