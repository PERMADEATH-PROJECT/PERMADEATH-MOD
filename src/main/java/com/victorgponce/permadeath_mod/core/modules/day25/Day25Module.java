package com.victorgponce.permadeath_mod.core.modules.day25;

import com.victorgponce.permadeath_mod.core.modules.GameModule;

public class Day25Module implements GameModule {

    @Override
    public String getName() {
        return "Day25";
    }

    @Override
    public boolean shouldBeActive(int currentDay) {
        return currentDay >= 25;
    }
}
