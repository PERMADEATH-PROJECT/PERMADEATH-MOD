package com.victorgponce.permadeath_mod.core.modules.day25;

import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.RavagerEntity;

// Buffs ravagers with speed and strength on spawn to make raid encounters deadlier
public final class RavagerEffectsHandler {

    private RavagerEffectsHandler() {}

    public static void applyRavagerEffects(Entity entity) {
        int day = ConfigFileManager.readConfig().getDay();

        if (entity instanceof RavagerEntity ravager && day >= 25) {
            ravager.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 999999, 1));
            ravager.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 999999, 2));
        }
    }
}
