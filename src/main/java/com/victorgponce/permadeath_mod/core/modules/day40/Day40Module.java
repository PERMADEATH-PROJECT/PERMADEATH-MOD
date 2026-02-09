package com.victorgponce.permadeath_mod.core.modules.day40;

import com.victorgponce.permadeath_mod.core.modules.GameModule;

public class Day40Module implements GameModule {

    @Override
    public String getName() {
        return "Day40";
    }

    @Override
    public boolean shouldBeActive(int currentDay) {
        return currentDay >= 40;
    }
}
