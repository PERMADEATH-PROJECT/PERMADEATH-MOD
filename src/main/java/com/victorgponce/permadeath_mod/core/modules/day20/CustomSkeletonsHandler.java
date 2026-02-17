package com.victorgponce.permadeath_mod.core.modules.day20;

import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;

// Creates custom-equipped skeleton riders for spiders and replaces skeleton spawns (day 20-29)
public final class CustomSkeletonsHandler {

    private static final ThreadLocal<Boolean> inCustomSpawn = ThreadLocal.withInitial(() -> false);

    private CustomSkeletonsHandler() {}

    // Returns true if the original entity should be cancelled
    public static boolean handleEntitySpawn(Entity entity) {
        if (inCustomSpawn.get()) return false;

        int day = ConfigFileManager.readConfig().getDay();
        if (day < 20 || day >= 30) return false;

        if (!(entity.getWorld() instanceof ServerWorld serverWorld)) return false;

        try {
            inCustomSpawn.set(true);

            if (entity instanceof SpiderEntity) {
                int skeletonType = Random.create().nextInt(5);
                SkeletonEntity skeleton = createCustomSkeleton(serverWorld, skeletonType);
                skeleton.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entity.getYaw(), entity.getPitch());
                serverWorld.spawnEntity(skeleton);
                skeleton.startRiding(entity);
                return false;
            }

            if (entity instanceof SkeletonEntity) {
                int skeletonType = Random.create().nextInt(5);
                SkeletonEntity custom = createCustomSkeleton(serverWorld, skeletonType);
                custom.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entity.getYaw(), entity.getPitch());
                serverWorld.spawnEntity(custom);
                return true;
            }
        } finally {
            inCustomSpawn.set(false);
        }
        return false;
    }

    public static SkeletonEntity createCustomSkeleton(ServerWorld world, int type) {
        SkeletonEntity skeleton = new SkeletonEntity(EntityType.SKELETON, world);
        switch (type) {
            case 0 -> {
                skeleton.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
                skeleton.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                skeleton.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                skeleton.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
                skeleton.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
                skeleton.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(20.0);
                skeleton.setHealth(20);
            }
            case 1 -> {
                ItemStack bow = new ItemStack(Items.BOW);
                bow.addEnchantment(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.PUNCH), 20);
                skeleton.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
                skeleton.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
                skeleton.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
                skeleton.equipStack(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
                skeleton.equipStack(EquipmentSlot.MAINHAND, bow);
                skeleton.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(40.0);
                skeleton.setHealth(40);
            }
            case 2 -> {
                ItemStack ironAxe = new ItemStack(Items.IRON_AXE);
                ironAxe.addEnchantment(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.FIRE_ASPECT), 2);
                skeleton.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
                skeleton.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
                skeleton.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                skeleton.equipStack(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
                skeleton.equipStack(EquipmentSlot.MAINHAND, ironAxe);
                skeleton.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(20.0);
                skeleton.setHealth(20);
            }
            case 3 -> {
                ItemStack crossbow = new ItemStack(Items.CROSSBOW);
                crossbow.addEnchantment(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.SHARPNESS), 20);
                skeleton.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
                skeleton.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
                skeleton.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
                skeleton.equipStack(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
                skeleton.equipStack(EquipmentSlot.MAINHAND, crossbow);
                skeleton.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(40.0);
                skeleton.setHealth(40);
            }
            case 4 -> {
                ItemStack bow = new ItemStack(Items.BOW);
                bow.addEnchantment(world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.POWER), 10);
                skeleton.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
                skeleton.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
                skeleton.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
                skeleton.equipStack(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
                skeleton.equipStack(EquipmentSlot.MAINHAND, bow);
                skeleton.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(40.0);
                skeleton.setHealth(40);
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
