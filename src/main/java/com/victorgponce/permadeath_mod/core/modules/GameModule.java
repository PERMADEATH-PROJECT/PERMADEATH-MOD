package com.victorgponce.permadeath_mod.core.modules;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

// Defines the contract for a day-based game module
public interface GameModule {

    String getName();

    boolean shouldBeActive(int currentDay);

    default void onServerTick(MinecraftServer server) {}

    default void onEntitySpawn(Entity entity) {}

    default void onEntityRemoved(Entity entity) {}
}
