package com.victorgponce.permadeath_mod.core.modules.day25;

import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.SlimeEntity;

import java.util.Random;

// Handles oversized mob variants that appear from day 25 onwards to increase challenge
public final class GigaMobsHandler {

    private static final Random RANDOM = new Random();

    private GigaMobsHandler() {}

    /**
     * Applies boosted health (40-60) and follow range to a ghast on spawn.
     * Modifies the entity directly because createGhastAttributes is a static
     * registration method that only runs once at startup.
     */
    public static void applyDemonicGhastAttributes(GhastEntity ghast) {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 25) return;

        double health = 40 + RANDOM.nextInt(21);
        ghast.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(health);
        ghast.setHealth((float) health);
        ghast.getAttributeInstance(EntityAttributes.FOLLOW_RANGE).setBaseValue(100.0);
    }

    /**
     * Returns boosted fireball strength (3-5) for ghasts, or null if day < 25.
     */
    public static Integer getDemonicFireballStrength() {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 25) return null;

        return 3 + RANDOM.nextInt(3);
    }

    /**
     * Forces magma cubes to size 16 on spawn when day >= 25.
     */
    public static boolean shouldEnlargeGigaMagmacube() {
        return ConfigFileManager.readConfig().getDay() >= 25;
    }

    /**
     * Enlarges slimes to size 15 with 32 health on initialization when day >= 25.
     */
    public static void applyGigaSlime(SlimeEntity slime) {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 25) return;

        slime.setSize(15, false);
        slime.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(32);
        slime.setHealth(32);
    }
}
