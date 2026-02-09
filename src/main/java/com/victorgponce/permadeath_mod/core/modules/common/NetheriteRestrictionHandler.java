package com.victorgponce.permadeath_mod.core.modules.common;

import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static com.victorgponce.permadeath_mod.data.NetheriteItemHelper.prohibitedItems;

// Centralizes netherite item restriction checks for pickup, grabbing, and block breaking
public final class NetheriteRestrictionHandler {

    private NetheriteRestrictionHandler() {}

    public static boolean shouldCancelPickup(PlayerEntity player, ItemStack stack) {
        if (player.isInCreativeMode()) return false;
        return prohibitedItems(stack.getItem());
    }

    public static void handlePickupBlocked(PlayerEntity player, ItemStack stack) {
        player.sendMessage(Text.literal("You cannot grab this item, the netherite is prohibited!")
                        .formatted(Formatting.BOLD)
                        .formatted(Formatting.RED),
                false);
        stack.decrement(stack.getCount());
    }

    public static boolean shouldCancelSlotClick(ServerPlayerEntity player, ClickSlotC2SPacket packet) {
        if (player.isInCreativeMode()) return false;
        if (packet.syncId() != player.currentScreenHandler.syncId) return false;

        int slotIndex = packet.slot();
        var handler = player.currentScreenHandler;
        if (slotIndex < 0 || slotIndex >= handler.slots.size()) return false;

        var slot = handler.getSlot(slotIndex);
        if (prohibitedItems(slot.getStack().getItem())) {
            player.sendMessage(Text.of("¡You cannot grab this!"), false);
            return true;
        }
        return false;
    }
}
