package com.zeydie.telegram.bot.monitor.api.v1.data;

import lombok.Data;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

@Data
public class TemperatureData {
    private String min;
    private String avg;
    private String max;

    public double getMinDouble() {
        return Double.parseDouble(this.toDoubleString(this.min));
    }

    public double getAvgDouble() {
        return Double.parseDouble(this.toDoubleString(this.avg));
    }

    public double getMaxDouble() {
        return Double.parseDouble(this.toDoubleString(this.max));
    }

    private @NotNull String toDoubleString(@NonNull final String value) {
        return value.replace(",", ".");
    }
}