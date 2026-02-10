package com.victorgponce.permadeath_mod.core.modules.day40;

import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import com.victorgponce.permadeath_mod.util.mobcaps.SinglePlayerHandler;
import com.victorgponce.permadeath_mod.util.tickcounter.TaskManager;
import com.victorgponce.permadeath_mod.util.tickcounter.TickCounter;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

// Consolidates all day-40 mob modifications: aggression, effects, transformations, and phantom riders
public final class MobModifierHandler {

    private static final ThreadLocal<Boolean> inCustomSpawn = ThreadLocal.withInitial(() -> false);

    private static final int MAX_CAVE_SPIDERS = 30;

    // Uses real-time entity counts instead of static counters
    private static int getCaveSpiderCount() {
        return SinglePlayerHandler.getInstance().getEntityTypeMap().getOrDefault(EntityType.CAVE_SPIDER, 0);
    }

    private static final List<StatusEffectInstance> CREEPER_EFFECTS = List.of(
            new StatusEffectInstance(StatusEffects.SPEED, Integer.MAX_VALUE, 2),
            new StatusEffectInstance(StatusEffects.RESISTANCE, Integer.MAX_VALUE, 2)
    );

    private static final List<StatusEffectInstance> GUARDIAN_EFFECTS = List.of(
            new StatusEffectInstance(StatusEffects.RESISTANCE, Integer.MAX_VALUE, 2),
            new StatusEffectInstance(StatusEffects.SPEED, Integer.MAX_VALUE, 2)
    );

    private MobModifierHandler() {}

    /**
     * Returns the overridden guardian warmup time, or null if day < 40.
     */
    public static Integer getGuardianWarmupTime() {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 40) return null;
        return 40;
    }

    /**
     * Applies status effects to creepers, golems, and guardians on spawn.
     */
    public static void applyMobEffects(Entity entity) {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 40) return;

        if (entity instanceof CreeperEntity creeper) {
            for (StatusEffectInstance effect : CREEPER_EFFECTS) {
                creeper.addStatusEffect(effect);
            }
        }

        if (entity instanceof GolemEntity golem) {
            golem.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, Integer.MAX_VALUE, 1));
        }

        if (entity instanceof GuardianEntity guardian) {
            for (StatusEffectInstance effect : GUARDIAN_EFFECTS) {
                guardian.addStatusEffect(effect);
            }
        }
    }

    /**
     * Determines if an enderman should be made aggressive (60% chance).
     */
    public static boolean shouldMakeEndermanAggressive(World world) {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 40) return false;
        return world.random.nextFloat() < 0.6f;
    }

    /**
     * Handles phantom rider spawning. Returns true if no further action is needed from the mixin.
     * Skeleton riders mount phantoms using the same day-30 skeleton variants.
     */
    public static void handlePhantomRider(Entity entity) {
        if (inCustomSpawn.get()) return;

        int day = ConfigFileManager.readConfig().getDay();
        if (day < 40) return;

        if (!(entity.getWorld() instanceof ServerWorld serverWorld)) return;
        if (!(entity instanceof PhantomEntity phantom)) return;

        try {
            inCustomSpawn.set(true);

            int skeletonType = Random.create().nextInt(5);
            SkeletonEntity skeleton = createCustomSkeleton(serverWorld, skeletonType);
            skeleton.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entity.getYaw(), entity.getPitch());
            serverWorld.spawnEntity(skeleton);
            // Delay mounting by 1 tick so both entities are fully registered in the world
            TaskManager.addTask(new TickCounter(1, () -> skeleton.startRiding(phantom, true)));
        } finally {
            inCustomSpawn.set(false);
        }
    }

    /**
     * Transforms normal spiders into cave spiders up to the cap.
     * Returns true if the original spider entity should be cancelled.
     */
    public static boolean handleSpiderTransformation(Entity entity) {
        if (inCustomSpawn.get()) return false;

        int day = ConfigFileManager.readConfig().getDay();
        if (day < 40) return false;

        if (!(entity instanceof SpiderEntity)) return false;
        if (!(entity.getWorld() instanceof ServerWorld serverWorld)) return false;

        try {
            inCustomSpawn.set(true);

            if (getCaveSpiderCount() < MAX_CAVE_SPIDERS) {
                CaveSpiderEntity caveSpider = new CaveSpiderEntity(EntityType.CAVE_SPIDER, serverWorld);
                caveSpider.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entity.getYaw(), entity.getPitch());
                serverWorld.spawnEntity(caveSpider);
            }
            // Block all normal spider spawns after day 40
            return true;
        } finally {
            inCustomSpawn.set(false);
        }
    }

    /**
     * Creates a custom skeleton with day-30 tier equipment for phantom riders.
     * Reuses the same variant system to keep skeleton types consistent across modules.
     */
    private static SkeletonEntity createCustomSkeleton(ServerWorld world, int type) {
        SkeletonEntity skeleton = new SkeletonEntity(EntityType.SKELETON, world);

        switch (type) {
            case 0 -> {
                ItemStack normalBow = new ItemStack(Items.BOW);
                ItemStack diamondHelmet = new ItemStack(Items.DIAMOND_HELMET);
                ItemStack diamondChestplate = new ItemStack(Items.DIAMOND_CHESTPLATE);
                ItemStack diamondLeggings = new ItemStack(Items.DIAMOND_LEGGINGS);
                ItemStack diamondBoots = new ItemStack(Items.DIAMOND_BOOTS);
                diamondHelmet.addEnchantment(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.PROTECTION), 4);
                diamondChestplate.addEnchantment(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.PROTECTION), 4);
                diamondLeggings.addEnchantment(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.PROTECTION), 4);
                diamondBoots.addEnchantment(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.PROTECTION), 4);

                skeleton.equipStack(EquipmentSlot.HEAD, diamondHelmet);
                skeleton.equipStack(EquipmentSlot.CHEST, diamondChestplate);
                skeleton.equipStack(EquipmentSlot.LEGS, diamondLeggings);
                skeleton.equipStack(EquipmentSlot.FEET, diamondBoots);
                skeleton.equipStack(EquipmentSlot.MAINHAND, normalBow);
                skeleton.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(20.0);
                skeleton.setHealth(20);
                skeleton.setCustomName(Text.of("Esqueleto Guerrero"));
                skeleton.setCustomNameVisible(true);
            }
            case 1 -> {
                ItemStack diamondAxe = new ItemStack(Items.DIAMOND_AXE);
                diamondAxe.addEnchantment(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.FIRE_ASPECT), 10);

                skeleton.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
                skeleton.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
                skeleton.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                skeleton.equipStack(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
                skeleton.equipStack(EquipmentSlot.MAINHAND, diamondAxe);
                skeleton.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(20.0);
                skeleton.setHealth(20);
                skeleton.setCustomName(Text.of("Esqueleto Infernal"));
                skeleton.setCustomNameVisible(true);
            }
            case 2 -> {
                ItemStack crossBow = new ItemStack(Items.CROSSBOW);
                crossBow.addEnchantment(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.SHARPNESS), 25);

                skeleton.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
                skeleton.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
                skeleton.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
                skeleton.equipStack(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
                skeleton.equipStack(EquipmentSlot.MAINHAND, crossBow);
                skeleton.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(20.0);
                skeleton.setHealth(20);
                skeleton.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 999999, 2));
                skeleton.setCustomName(Text.of("Esqueleto Asesino"));
                skeleton.setCustomNameVisible(true);
            }
            case 3 -> {
                ItemStack bow = new ItemStack(Items.BOW);
                bow.addEnchantment(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.PUNCH), 30);
                bow.addEnchantment(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.POWER), 25);

                skeleton.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
                skeleton.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
                skeleton.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
                skeleton.equipStack(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
                skeleton.equipStack(EquipmentSlot.MAINHAND, bow);
                skeleton.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(20.0);
                skeleton.setHealth(20);
                skeleton.setCustomName(Text.of("Esqueleto Táctico"));
                skeleton.setCustomNameVisible(true);
            }
            case 4 -> {
                ItemStack bowX = new ItemStack(Items.BOW);
                bowX.addEnchantment(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.POWER), 50);

                skeleton.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                skeleton.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
                skeleton.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
                skeleton.equipStack(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
                skeleton.equipStack(EquipmentSlot.MAINHAND, bowX);
                skeleton.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(20.0);
                skeleton.setHealth(20);
                skeleton.setCustomName(Text.of("Esqueleto Pesadilla"));
                skeleton.setCustomNameVisible(true);
            }
        }

        skeleton.setEquipmentDropChance(EquipmentSlot.HEAD, 0.0F);
        skeleton.setEquipmentDropChance(EquipmentSlot.CHEST, 0.0F);
        skeleton.setEquipmentDropChance(EquipmentSlot.LEGS, 0.0F);
        skeleton.setEquipmentDropChance(EquipmentSlot.FEET, 0.0F);
        skeleton.setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.0F);

        return skeleton;
    }
}
