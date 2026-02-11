package com.victorgponce.permadeath_mod.core.modules.day30;

import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

// Spawns stronger skeleton variants (riders and replacements) from day 30 with upgraded gear
public final class SkeletonDay30Handler {

    private static final ThreadLocal<Boolean> inCustomSpawn = ThreadLocal.withInitial(() -> false);

    private SkeletonDay30Handler() {}

    /**
     * Handles skeleton/spider spawn logic for day 30+.
     * Spiders get a custom skeleton rider; skeleton spawns are replaced with upgraded variants.
     * Returns true if the original entity should be cancelled.
     */
    public static boolean handleEntitySpawn(Entity entity) {
        if (inCustomSpawn.get()) return false;

        int day = ConfigFileManager.readConfig().getDay();
        if (day < 30) return false;

        if (!(entity.getWorld() instanceof ServerWorld serverWorld)) return false;

        try {
            inCustomSpawn.set(true);

            if (entity instanceof SpiderEntity) {
                int skeletonType = Random.create().nextInt(5);
                SkeletonEntity skeleton = createCustomSkeletonDay30(serverWorld, skeletonType);
                skeleton.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entity.getYaw(), entity.getPitch());
                serverWorld.spawnEntity(skeleton);
                skeleton.startRiding(entity);
                return false;
            }

            if (entity instanceof SkeletonEntity) {
                if (entity.getCommandTags().contains(com.victorgponce.permadeath_mod.core.modules.day40.MobModifierHandler.PHANTOM_RIDER_TAG)) return false;
                int skeletonType = Random.create().nextInt(5);
                SkeletonEntity custom = createCustomSkeletonDay30(serverWorld, skeletonType);
                custom.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entity.getYaw(), entity.getPitch());
                serverWorld.spawnEntity(custom);
                return true;
            }
        } finally {
            inCustomSpawn.set(false);
        }

        return false;
    }

    /**
     * Creates a skeleton variant with day-30 tier equipment.
     * Higher enchantment levels and named variants compared to the day-20 equivalents.
     */
    public static SkeletonEntity createCustomSkeletonDay30(ServerWorld world, int type) {
        SkeletonEntity skeleton = new SkeletonEntity(EntityType.SKELETON, world);

        switch (type) {
            case 0 -> {
                // Warrior: full diamond with Protection IV
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
                // Infernal: iron armor with Fire Aspect 10 axe
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
                // Assassin: golden armor with Sharpness 25 crossbow + Speed
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
                // Tactical: chainmail with Punch 30 + Power 25 bow
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
                // Nightmare: leather armor with Power 50 bow
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

        // Zero drop chance on all equipment to prevent overpowered loot
        skeleton.setEquipmentDropChance(EquipmentSlot.HEAD, 0.0F);
        skeleton.setEquipmentDropChance(EquipmentSlot.CHEST, 0.0F);
        skeleton.setEquipmentDropChance(EquipmentSlot.LEGS, 0.0F);
        skeleton.setEquipmentDropChance(EquipmentSlot.FEET, 0.0F);
        skeleton.setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.0F);

        return skeleton;
    }
}
