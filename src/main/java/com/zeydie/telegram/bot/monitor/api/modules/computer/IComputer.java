package com.zeydie.telegram.bot.monitor.api.modules.computer;

import com.zeydie.telegram.bot.monitor.api.v2.data.ComputerData;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IComputer {
    @NotNull List<ComputerData> getComputersData();

    @Nullable ComputerData getComputerData(@NonNull final ComputerData computerData);

    @Nullable ComputerData getComputerData(@NonNull final String token);

    void addComputerData(@NonNull final ComputerData computerData);

    void updateComputerData(@NonNull final ComputerData computerData);

    void removeComputerData(@NonNull final ComputerData computerData);

    void removeComputerData(@NonNull final String token);
}