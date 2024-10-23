package com.zeydie.telegram.bot.monitor.modules.temperature;

import com.zeydie.telegram.bot.monitor.api.modules.temperature.IComputer;
import com.zeydie.telegram.bot.monitor.api.v1.data.ComputerData;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComputerImpl implements IComputer {
    private final @NotNull Map<String, ComputerData> tokenComputers = new HashMap<>();

    @Override
    public @NotNull List<ComputerData> getComputersData() {
        return this.tokenComputers.values()
                .stream()
                .toList();
    }

    @Override
    public @Nullable ComputerData getComputerData(@NonNull final String token) {
        return this.tokenComputers.get(token);
    }

    @Override
    public void addComputerData(@NonNull final ComputerData computerData) {
        val token = computerData.getToken();

        if (!this.tokenComputers.containsKey(token))
            this.tokenComputers.put(token, computerData);
    }

    @Override
    public void updateComputerData(@NonNull final ComputerData computerData) {
        val token = computerData.getToken();

        this.addComputerData(computerData);

        val data = this.tokenComputers.get(token);

        data.setCpu(computerData.getCpu());
        data.setGpu(computerData.getGpu());
    }

    @Override
    public void removeComputerData(@NonNull final String token) {
        this.tokenComputers.remove(token);
    }
}