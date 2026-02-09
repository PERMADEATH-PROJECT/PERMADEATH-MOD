package com.victorgponce.permadeath_mod.mixin.entity;

import com.victorgponce.permadeath_mod.core.modules.common.NetheriteRestrictionHandler;
import com.victorgponce.permadeath_mod.core.modules.day40.InventoryRestrictionHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
    @Shadow @Final public PlayerEntity player;
    @Shadow private DefaultedList<ItemStack> main;

    @Inject(method = "insertStack(ILnet/minecraft/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void onItemPickup(int slot, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (NetheriteRestrictionHandler.shouldCancelPickup(player, stack)) {
            NetheriteRestrictionHandler.handlePickupBlocked(player, stack);
            cir.cancel();
        }
    }

    @Inject(method = "getEmptySlot", at = @At("HEAD"), cancellable = true)
    private void onGetEmptySlot(CallbackInfoReturnable<Integer> cir) {
        int result = InventoryRestrictionHandler.getEmptySlot(this.main);
        if (result != -2) {
            cir.setReturnValue(result);
        }
    }
}
