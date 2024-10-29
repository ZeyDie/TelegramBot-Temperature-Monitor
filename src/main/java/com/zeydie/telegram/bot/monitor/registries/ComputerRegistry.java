package com.zeydie.telegram.bot.monitor.registries;

import com.beust.jcommander.internal.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ComputerRegistry {
    private static final @NotNull List<Long> COMPUTER_REGISTRY = Lists.newArrayList();

    public static void registry(final long userId) {
        COMPUTER_REGISTRY.add(userId);
    }

    public static void unregistry(final long senderId) {
        COMPUTER_REGISTRY.remove(senderId);
    }

    public static boolean isRegistry(final long userId) {
        return COMPUTER_REGISTRY.contains(userId);
    }
}