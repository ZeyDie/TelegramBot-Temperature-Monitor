package com.zeydie.telegram.bot.monitor.api.v1.data;

import lombok.Data;

@Data
public class TemperatureData {
    private String min;
    private String avg;
    private String max;

    public int getMinInt() {
        return Integer.parseInt(this.min);
    }

    public int getAvgInt() {
        return Integer.parseInt(this.avg);
    }

    public int getMaxInt() {
        return Integer.parseInt(this.max);
    }
}