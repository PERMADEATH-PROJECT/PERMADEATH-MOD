package com.victorgponce.permadeath_mod.core.modules.day20;

import com.victorgponce.permadeath_mod.core.modules.GameModule;

public class Day20Module implements GameModule {

    @Override
    public String getName() {
        return "Day20";
    }

    @Override
    public boolean shouldBeActive(int currentDay) {
        return currentDay >= 20;
    }
}
