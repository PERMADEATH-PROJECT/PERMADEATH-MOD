package com.victorgponce.permadeath_mod.core.modules;

import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

// Central registry that dispatches events to active modules based on current day
public class ModuleManager {

    private static final ModuleManager INSTANCE = new ModuleManager();
    private final List<GameModule> modules = new ArrayList<>();

    private ModuleManager() {}

    public static ModuleManager getInstance() {
        return INSTANCE;
    }

    public void register(GameModule module) {
        modules.add(module);
    }

    public void onServerTick(MinecraftServer server) {
        int day = ConfigFileManager.readConfig().getDay();
        for (GameModule module : modules) {
            if (module.shouldBeActive(day)) {
                module.onServerTick(server);
            }
        }
    }

    public void onEntitySpawn(Entity entity) {
        int day = ConfigFileManager.readConfig().getDay();
        for (GameModule module : modules) {
            if (module.shouldBeActive(day)) {
                module.onEntitySpawn(entity);
            }
        }
    }

    public void onEntityRemoved(Entity entity) {
        int day = ConfigFileManager.readConfig().getDay();
        for (GameModule module : modules) {
            if (module.shouldBeActive(day)) {
                module.onEntityRemoved(entity);
            }
        }
    }

    public List<GameModule> getActiveModules() {
        int day = ConfigFileManager.readConfig().getDay();
        List<GameModule> active = new ArrayList<>();
        for (GameModule module : modules) {
            if (module.shouldBeActive(day)) {
                active.add(module);
            }
        }
        return active;
    }
}
