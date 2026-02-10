package com.victorgponce.permadeath_mod.core.modules.day30;

import com.victorgponce.permadeath_mod.mixin.accessor.CreeperEntityAccessor;
import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

// Transforms passive and common mobs into dangerous variants to escalate late-game difficulty
public final class EntityTransformationHandler {

    private static final ThreadLocal<Boolean> inCustomSpawn = ThreadLocal.withInitial(() -> false);

    private static final List<StatusEffectInstance> SILVERFISH_EFFECTS = Arrays.asList(
            new StatusEffectInstance(StatusEffects.SPEED, 999999, 2),
            new StatusEffectInstance(StatusEffects.STRENGTH, 999999, 3),
            new StatusEffectInstance(StatusEffects.JUMP_BOOST, 999999, 4),
            new StatusEffectInstance(StatusEffects.GLOWING, 999999),
            new StatusEffectInstance(StatusEffects.REGENERATION, 999999, 3)
    );

    private EntityTransformationHandler() {}

    /**
     * Handles entity transformations on spawn. Returns true if the original entity should be cancelled.
     * Uses a ThreadLocal flag to prevent infinite recursion when spawning replacement entities.
     */
    public static boolean handleEntitySpawn(Entity entity) {
        if (inCustomSpawn.get()) return false;

        int day = ConfigFileManager.readConfig().getDay();
        if (day < 30) return false;

        if (!(entity.getWorld() instanceof ServerWorld serverWorld)) return false;

        try {
            inCustomSpawn.set(true);

            if (entity instanceof SquidEntity && getEntityCount(serverWorld, EntityType.GUARDIAN) < 20) {
                GuardianEntity guardian = new GuardianEntity(EntityType.GUARDIAN, serverWorld);
                guardian.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entity.getYaw(), entity.getPitch());
                guardian.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 999999, 2));
                guardian.setCustomName(Text.literal("Speed Guardian"));
                serverWorld.spawnEntity(guardian);
                return true;
            }

            // Block excess squids/blazes that would go over their caps
            if ((entity instanceof SquidEntity && getEntityCount(serverWorld, EntityType.GUARDIAN) >= 20)
                    || (entity instanceof BlazeEntity && getEntityCount(serverWorld, EntityType.BLAZE) >= 15)) {
                return true;
            }

            if (entity instanceof BatEntity && getEntityCount(serverWorld, EntityType.BLAZE) < 15) {
                BlazeEntity blaze = new BlazeEntity(EntityType.BLAZE, serverWorld);
                blaze.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entity.getYaw(), entity.getPitch());
                blaze.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 999999, 2));
                blaze.setCustomName(Text.literal("Resistance Blaze"));
                serverWorld.spawnEntity(blaze);
                return true;
            }

            if (entity instanceof CreeperEntity creeper) {
                CreeperEntityAccessor accessor = (CreeperEntityAccessor) creeper;
                creeper.getDataTracker().set(accessor.charged(), true);
            }

            if (entity instanceof PillagerEntity pillager) {
                World world = entity.getWorld();
                ItemStack crossBow = new ItemStack(Items.CROSSBOW);
                crossBow.addEnchantment(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.QUICK_CHARGE), 10);
                pillager.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 999999));
                pillager.equipStack(EquipmentSlot.MAINHAND, crossBow);
            }

            if (entity instanceof SkeletonEntity skeleton) {
                ItemStack tippedArrow = PotionContentsComponent.createStack(Items.TIPPED_ARROW, Potions.STRONG_HARMING);
                tippedArrow.setCount(64);
                skeleton.equipStack(EquipmentSlot.OFFHAND, tippedArrow);
                skeleton.setEquipmentDropChance(EquipmentSlot.OFFHAND, 0.0f);
            }

            if (entity instanceof ZombifiedPiglinEntity piglin) {
                piglin.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                piglin.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                piglin.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                piglin.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
            }

            if (entity instanceof IronGolemEntity ironGolem) {
                ironGolem.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 999999, 4));
            }

            if (entity instanceof EndermanEntity enderman) {
                enderman.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 999999, 2));
            }

            if (entity instanceof SilverfishEntity silverfish) {
                for (int i = 0; i < 5; i++) {
                    silverfish.addStatusEffect(SILVERFISH_EFFECTS.get(i));
                }
            }

        } finally {
            inCustomSpawn.set(false);
        }

        return false;
    }

    // Counts entities of a given type directly in the server world
    // so the cap reflects the actual state even mid-tick
    private static int getEntityCount(ServerWorld world, EntityType<?> type) {
        int count = 0;
        for (Entity e : world.iterateEntities()) {
            if (e.getType() == type) count++;
        }
        return count;
    }
}
