package com.zeydie.telegram.bot.monitor.api.v2.data;

import com.google.common.collect.Maps;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Data
public class TemperatureData {
    private @NotNull Map<String, String> hardwares = Maps.newHashMap();
}