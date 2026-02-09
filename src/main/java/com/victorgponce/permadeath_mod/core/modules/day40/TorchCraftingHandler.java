package com.victorgponce.permadeath_mod.core.modules.day40;

import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

// Prevents crafting any torch variant after day 40 to force players into permanent darkness
public final class TorchCraftingHandler {

    private TorchCraftingHandler() {}

    /**
     * Checks if the crafted item is a torch variant and should be blocked.
     * Returns true if the crafting result should be cleared.
     */
    public static boolean shouldPreventTorchCrafting(ItemStack output, Object player) {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 40) return false;

        if (output.isOf(Items.TORCH) || output.isOf(Items.SOUL_TORCH) || output.isOf(Items.REDSTONE_TORCH)) {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.sendMessage(
                        Text.literal("¡You cannot craft torches after day 40!").styled(
                                style -> style.withColor(0xFF0000)
                        ),
                        false
                );
            }
            return true;
        }
        return false;
    }
}
