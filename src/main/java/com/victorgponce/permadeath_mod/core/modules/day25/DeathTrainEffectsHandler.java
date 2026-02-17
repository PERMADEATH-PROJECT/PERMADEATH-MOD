package com.victorgponce.permadeath_mod.core.modules.day25;

import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Monster;

import java.util.Arrays;
import java.util.List;

// Buffs all hostile mobs when the death train event is active, making them significantly harder
public final class DeathTrainEffectsHandler {

    private static final List<StatusEffectInstance> DEATH_TRAIN_EFFECTS = Arrays.asList(
            new StatusEffectInstance(StatusEffects.STRENGTH, 999999, 1),
            new StatusEffectInstance(StatusEffects.SPEED, 999999, 1),
            new StatusEffectInstance(StatusEffects.RESISTANCE, 999999, 1)
    );

    private DeathTrainEffectsHandler() {}

    /**
     * Applies death train buffs to hostile mobs on spawn.
     * Each effect is cloned to avoid shared references across entities.
     */
    public static void applyDeathTrainEffects(Entity entity) {
        int day = ConfigFileManager.readConfig().getDay();
        boolean deathTrainStatus = ConfigFileManager.readConfig().isDeathTrain();

        if (day < 25 || !deathTrainStatus) return;

        if (entity instanceof Monster && entity instanceof LivingEntity living) {
            for (StatusEffectInstance effect : DEATH_TRAIN_EFFECTS) {
                // Clone to prevent shared mutable state between entities
                StatusEffectInstance cloned = new StatusEffectInstance(
                        effect.getEffectType(),
                        effect.getDuration(),
                        effect.getAmplifier()
                );
                living.addStatusEffect(cloned);
            }
        }
    }
}
