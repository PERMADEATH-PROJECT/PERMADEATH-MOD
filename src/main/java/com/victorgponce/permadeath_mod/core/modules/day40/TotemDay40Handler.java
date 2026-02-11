package com.victorgponce.permadeath_mod.core.modules.day40;

import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.random.Random;

import static com.victorgponce.permadeath_mod.Permadeath_mod.LOGGER;

// Implements harsh totem mechanics from day 40: low activation chance and requires spending extra totems
public final class TotemDay40Handler {

    private TotemDay40Handler() {}

    /**
     * Returns true if the totem activation should be blocked.
     * Day 40+ requires holding at least 2 totems (consuming one extra),
     * and even then only has a 3% chance of actually activating.
     */
    public static boolean shouldBlockTotem(Object self) {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 40) return false;

        if (!(self instanceof ServerPlayerEntity player)) return false;

        Inventory inventory = player.getInventory();

        int totemCount = 0;
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.getStack(i).isOf(Items.TOTEM_OF_UNDYING)) {
                totemCount++;
            }
        }

        // Must have at least 2 totems
        if (totemCount < 2) {
            LOGGER.info("Player {} has only {} totems. Canceling activation.", player.getName().getString(), totemCount);
            return true;
        }

        // Remove one additional totem (not the one currently in hand)
        removeExtraTotem(player);

        // 97% chance of the totem actually working
        if (Random.create().nextInt(100) <= 3) {
            LOGGER.info("Player {} failed the 97% totem activation roll.", player.getName().getString());
            return true;
        }

        return false;
    }

    private static void removeExtraTotem(ServerPlayerEntity player) {
        Inventory inventory = player.getInventory();

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isOf(Items.TOTEM_OF_UNDYING)) continue;

            // Skip the totem currently held in either hand
            boolean isInHand = false;
            for (Hand hand : Hand.values()) {
                if (player.getStackInHand(hand) == stack) {
                    isInHand = true;
                    break;
                }
            }

            if (!isInHand) {
                inventory.removeStack(i, 1);
                LOGGER.info("Removed 1 additional totem from player {}'s inventory.", player.getName().getString());
                return;
            }
        }

        // Fallback: remove from non-active hand if no other totem was found
        Hand nonActiveHand = (player.getActiveHand() == Hand.MAIN_HAND) ? Hand.OFF_HAND : Hand.MAIN_HAND;
        ItemStack stack = player.getStackInHand(nonActiveHand);
        if (stack.isOf(Items.TOTEM_OF_UNDYING)) {
            player.setStackInHand(nonActiveHand, ItemStack.EMPTY);
            LOGGER.info("Removed 1 totem from {} hand of player {}.", nonActiveHand, player.getName().getString());
        }
    }
}
