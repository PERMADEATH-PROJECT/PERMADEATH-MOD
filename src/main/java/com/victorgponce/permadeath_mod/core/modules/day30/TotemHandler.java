package com.victorgponce.permadeath_mod.core.modules.day30;

import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.util.math.random.Random;

// Adds a small chance for totems of undying to fail during days 30-39 as a permadeath teaser
public final class TotemHandler {

    private TotemHandler() {}

    /**
     * Returns true if the totem activation should be blocked.
     * Only applies during days 30-39 with a 1% failure chance,
     * keeping the mechanic rare but psychologically impactful.
     */
    public static boolean shouldDisableTotem() {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 30 || day >= 40) return false;

        return Random.create().nextInt(100) < 1;
    }
}
