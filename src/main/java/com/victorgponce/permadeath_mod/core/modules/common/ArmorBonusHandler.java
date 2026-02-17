package com.victorgponce.permadeath_mod.core.modules.common;

import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

// Grants +8 max health when wearing full netherite armor
public final class ArmorBonusHandler {

    private ArmorBonusHandler() {}

    public static void handleArmorTick(PlayerEntity player, boolean wasWearingFullNetherite) {
        // This is called from the mixin which tracks the previous state
    }

    public static boolean checkFullNetheriteArmor(PlayerEntity player) {
        return isNetheritePiece(player.getEquippedStack(EquipmentSlot.HEAD), Items.NETHERITE_HELMET) &&
                isNetheritePiece(player.getEquippedStack(EquipmentSlot.CHEST), Items.NETHERITE_CHESTPLATE) &&
                isNetheritePiece(player.getEquippedStack(EquipmentSlot.LEGS), Items.NETHERITE_LEGGINGS) &&
                isNetheritePiece(player.getEquippedStack(EquipmentSlot.FEET), Items.NETHERITE_BOOTS);
    }

    public static void applyArmorBonus(PlayerEntity player) {
        int day = ConfigFileManager.readConfig().getDay();
        EntityAttributeInstance maxHealthAttr = player.getAttributeInstance(EntityAttributes.MAX_HEALTH);
        if (day < 40) {
            maxHealthAttr.setBaseValue(maxHealthAttr.getBaseValue() + 8);
        } else {
            maxHealthAttr.setBaseValue(20);
        }
    }

    public static void removeArmorBonus(PlayerEntity player) {
        EntityAttributeInstance maxHealthAttr = player.getAttributeInstance(EntityAttributes.MAX_HEALTH);
        maxHealthAttr.setBaseValue(maxHealthAttr.getBaseValue() - 8);
        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
    }

    private static boolean isNetheritePiece(ItemStack stack, Item item) {
        return !stack.isEmpty() && stack.getItem() == item;
    }
}
