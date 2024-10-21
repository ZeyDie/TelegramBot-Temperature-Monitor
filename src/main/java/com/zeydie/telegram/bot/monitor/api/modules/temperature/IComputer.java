package com.zeydie.telegram.bot.monitor.api.modules.temperature;

import com.zeydie.telegram.bot.monitor.api.data.ComputerData;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IComputer {
    @NotNull List<ComputerData> getComputersData();

    @Nullable ComputerData getComputerData(@NonNull final String token);

    void addComputerData(@NonNull final ComputerData temperatureData);

    void updateComputerData(@NonNull final ComputerData temperatureData);

    void removeComputerData(@NonNull final String token);
}