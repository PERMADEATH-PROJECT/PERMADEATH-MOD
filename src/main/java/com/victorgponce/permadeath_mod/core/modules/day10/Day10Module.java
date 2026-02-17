package com.victorgponce.permadeath_mod.core.modules.day10;

import com.victorgponce.permadeath_mod.core.modules.GameModule;
import net.minecraft.server.MinecraftServer;

public class Day10Module implements GameModule {

    @Override
    public String getName() {
        return "Day10";
    }

    @Override
    public boolean shouldBeActive(int currentDay) {
        return currentDay >= 10;
    }
}
