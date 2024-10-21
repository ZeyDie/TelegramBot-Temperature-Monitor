package com.zeydie.telegram.bot.monitor.api.data;

import lombok.Data;

@Data
public class ComputerData {
    private String token;

    private TemperatureData cpu;
    private TemperatureData gpu;
}