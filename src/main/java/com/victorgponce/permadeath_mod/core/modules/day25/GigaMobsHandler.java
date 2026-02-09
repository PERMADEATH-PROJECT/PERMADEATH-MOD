package com.victorgponce.permadeath_mod.core.modules.day25;

import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;

import java.util.Random;

// Handles oversized mob variants that appear from day 25 onwards to increase challenge
public final class GigaMobsHandler {

    private static final Random RANDOM = new Random();

    private GigaMobsHandler() {}

    /**
     * Overrides ghast attributes with boosted health (40-60) and extended follow range.
     * Returns null if day < 25 (caller should skip override).
     */
    public static DefaultAttributeContainer.Builder createDemonicGhastAttributes() {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 25) return null;

        double health = 40 + RANDOM.nextInt(21);
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, health)
                .add(EntityAttributes.FOLLOW_RANGE, 100.0);
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
