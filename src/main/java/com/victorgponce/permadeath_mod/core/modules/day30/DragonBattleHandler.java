package com.victorgponce.permadeath_mod.core.modules.day30;

import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

// Powers up the Ender Dragon with massive health, armor, effects and periodic ghast reinforcements
public final class DragonBattleHandler {

    private static final List<StatusEffectInstance> DRAGON_EFFECTS = Arrays.asList(
            new StatusEffectInstance(StatusEffects.SPEED, 999999, 4),
            new StatusEffectInstance(StatusEffects.STRENGTH, 999999, 3),
            new StatusEffectInstance(StatusEffects.RESISTANCE, 999999, 3),
            new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 999999)
    );

    // Shared across ticks to track ghast spawn interval
    private static int accumulatedTicks = 0;

    private DragonBattleHandler() {}

    /**
     * Applies boss-tier stats to the dragon after creation.
     * Called from the dragon fight's createDragon return injection.
     */
    public static void enhanceDragon(EnderDragonEntity dragon) {
        int day = ConfigFileManager.readConfig().getDay();
        if (day <= 30 || dragon == null) return;

        dragon.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(600);
        dragon.setHealth(600);
        dragon.getAttributeInstance(EntityAttributes.ARMOR).setBaseValue(10);
        dragon.getAttributeInstance(EntityAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0);

        for (StatusEffectInstance effect : DRAGON_EFFECTS) {
            dragon.addStatusEffect(effect);
        }
    }

    /**
     * Spawns 2 ghasts near the dragon every 30 seconds (600 ticks) during the fight.
     * The tick counter resets after each wave to maintain a steady reinforcement cadence.
     */
    public static void tickDragonReinforcements(EnderDragonEntity dragon) {
        int day = ConfigFileManager.readConfig().getDay();
        if (day <= 30 || dragon == null) return;

        accumulatedTicks++;
        if (accumulatedTicks >= 20 * 30) {
            accumulatedTicks = 0;
            Vec3d pos = dragon.getPos();
            for (int i = 0; i < 2; i++) {
                GhastEntity ghast = new GhastEntity(EntityType.GHAST, dragon.getWorld());
                ghast.setPos(pos.x, pos.y, pos.z);
                dragon.getWorld().spawnEntity(ghast);
            }
        }
    }

    /**
     * Resolves the dragon entity from UUID when the direct reference is null.
     */
    public static EnderDragonEntity resolveDragon(ServerWorld world, UUID dragonUuid) {
        if (dragonUuid == null) return null;
        return (EnderDragonEntity) world.getEntity(dragonUuid);
    }

    public static void resetTickCounter() {
        accumulatedTicks = 0;
    }
}
