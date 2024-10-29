package com.zeydie.telegram.bot.monitor.api.v1.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComputerData {
    private String token;

    private TemperatureData cpu;
    private TemperatureData gpu;
}