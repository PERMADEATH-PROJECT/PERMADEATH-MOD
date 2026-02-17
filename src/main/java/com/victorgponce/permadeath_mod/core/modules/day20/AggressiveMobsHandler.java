package com.victorgponce.permadeath_mod.core.modules.day20;

import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;

// Adds aggressive AI goals to passive mobs when day >= 20
public final class AggressiveMobsHandler {

    private AggressiveMobsHandler() {}

    public static boolean shouldBeAggressive() {
        return ConfigFileManager.readConfig().getDay() >= 20;
    }

    public static boolean shouldBeAggressiveSquid() {
        int day = ConfigFileManager.readConfig().getDay();
        return day >= 20 && day < 30;
    }

    public static void setupAngryPiglin(ZombifiedPiglinEntity piglin) {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 20) return;

        piglin.setAngerTime(Integer.MAX_VALUE);
    }

    public static void forceTargetPlayer(ZombifiedPiglinEntity piglin) {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 20) return;

        if (!piglin.getWorld().isClient() && piglin.getTarget() == null) {
            PlayerEntity nearestPlayer = piglin.getWorld().getClosestPlayer(piglin, 128);
            if (nearestPlayer != null) {
                piglin.setTarget(nearestPlayer);
            }
        }
    }
}
