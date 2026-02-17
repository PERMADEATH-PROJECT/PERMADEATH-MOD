package com.victorgponce.permadeath_mod.core.modules.common;

import com.victorgponce.permadeath_mod.effects.SpiderEffectsDay10;
import com.victorgponce.permadeath_mod.effects.SpiderEffectsDay20;
import com.victorgponce.permadeath_mod.effects.SpiderEffectsDay25;
import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SpiderEntity;

import java.util.Arrays;
import java.util.List;

// Applies random status effects to spiders based on the current day
public final class SpiderEffectsHandler {

    private static final List<StatusEffectInstance> AVAILABLE_EFFECTS = Arrays.asList(
            new StatusEffectInstance(StatusEffects.SPEED, 999999, 2),
            new StatusEffectInstance(StatusEffects.STRENGTH, 999999, 3),
            new StatusEffectInstance(StatusEffects.JUMP_BOOST, 999999, 4),
            new StatusEffectInstance(StatusEffects.GLOWING, 999999),
            new StatusEffectInstance(StatusEffects.REGENERATION, 999999, 3),
            new StatusEffectInstance(StatusEffects.INVISIBILITY, 999999),
            new StatusEffectInstance(StatusEffects.SLOW_FALLING, 999999),
            new StatusEffectInstance(StatusEffects.RESISTANCE, 999999)
    );

    private SpiderEffectsHandler() {}

    public static void handleSpiderSpawn(Entity entity) {
        if (!(entity instanceof SpiderEntity spider)) return;

        int day = ConfigFileManager.readConfig().getDay();
        if (day >= 10 && day < 20) {
            new SpiderEffectsDay10(AVAILABLE_EFFECTS).applyEffects(spider);
        } else if (day >= 20 && day < 25) {
            new SpiderEffectsDay20(AVAILABLE_EFFECTS).applyEffects(spider);
        } else if (day >= 25) {
            new SpiderEffectsDay25(AVAILABLE_EFFECTS).applyEffects(spider);
        }
    }
}
