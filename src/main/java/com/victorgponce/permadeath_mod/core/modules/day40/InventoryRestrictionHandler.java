package com.victorgponce.permadeath_mod.core.modules.day40;

import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.util.Set;

// Restricts inventory slots to reduce available storage and hotbar size as a late-game penalty
public final class InventoryRestrictionHandler {

    // Slots that become blocked: last column of each inventory row + offhand
    private static final Set<Integer> BLOCKED_SLOTS = Set.of(8, 17, 26, 35, 40);

    private InventoryRestrictionHandler() {}

    /**
     * Checks if a slot should block item insertion.
     * Targets the rightmost column of each inventory row and the offhand slot
     * to progressively shrink usable inventory space.
     */
    public static boolean shouldBlockSlotInsertion(int slotIndex) {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 40) return false;
        return BLOCKED_SLOTS.contains(slotIndex);
    }

    /**
     * Returns the first empty slot index, skipping blocked slots.
     * Returns -1 if no valid empty slot exists (same as vanilla behavior).
     */
    public static int getEmptySlot(DefaultedList<ItemStack> main) {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 40) return -2; // sentinel: caller should use default logic

        for (int i = 0; i < main.size(); i++) {
            if (BLOCKED_SLOTS.contains(i)) continue;
            if (main.get(i).isEmpty()) return i;
        }
        return -1;
    }

    /**
     * Returns the reduced hotbar size (8 instead of 9), or null if not active.
     */
    public static Integer getModifiedHotbarSize() {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 40) return null;
        return 8;
    }
}
