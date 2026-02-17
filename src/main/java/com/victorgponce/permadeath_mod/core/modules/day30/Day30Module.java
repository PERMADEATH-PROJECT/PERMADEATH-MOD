package com.victorgponce.permadeath_mod.core.modules.day30;

import com.victorgponce.permadeath_mod.core.modules.GameModule;

public class Day30Module implements GameModule {

    @Override
    public String getName() {
        return "Day30";
    }

    @Override
    public boolean shouldBeActive(int currentDay) {
        return currentDay >= 30;
    }
}
