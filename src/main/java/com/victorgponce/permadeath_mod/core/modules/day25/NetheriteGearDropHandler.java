package com.victorgponce.permadeath_mod.core.modules.day25;

import com.victorgponce.permadeath_mod.drops.netherite_gear.DropHandler;
import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Unit;
import net.minecraft.util.math.random.Random;

// Grants unbreakable netherite gear drops from specific mobs during the day 25-29 window
public final class NetheriteGearDropHandler {

    private static final ComponentType<Unit> UNBREAKABLE = DataComponentTypes.UNBREAKABLE;

    private NetheriteGearDropHandler() {}

    /**
     * Attempts to drop unbreakable netherite gear from killed entities.
     * Each mob type has a different gear piece and drop chance to spread
     * progression across multiple encounters.
     *
     * @return true if a drop was handled and the caller should cancel further processing
     */
    public static boolean tryDropNetheriteGear(LivingEntity entity) {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 25 || day >= 30) return false;

        if (entity instanceof SlimeEntity) {
            if (Random.create().nextInt(100) < 5) {
                dropUnbreakableGear(entity, new ItemStack(Items.NETHERITE_HELMET));
            }
            return true;
        } else if (entity instanceof MagmaCubeEntity) {
            if (Random.create().nextInt(100) < 3) {
                dropUnbreakableGear(entity, new ItemStack(Items.NETHERITE_CHESTPLATE));
            }
            return true;
        } else if (entity instanceof CaveSpiderEntity) {
            if (Random.create().nextInt(100) < 4) {
                dropUnbreakableGear(entity, new ItemStack(Items.NETHERITE_LEGGINGS));
            }
            return true;
        } else if (entity instanceof GhastEntity) {
            if (Random.create().nextInt(100) < 2) {
                dropUnbreakableGear(entity, new ItemStack(Items.NETHERITE_BOOTS));
            }
            return true;
        }

        return false;
    }

    private static void dropUnbreakableGear(LivingEntity entity, ItemStack gear) {
        gear.set(UNBREAKABLE, Unit.INSTANCE);
        new DropHandler().applyDrops(entity, gear);
    }
}
