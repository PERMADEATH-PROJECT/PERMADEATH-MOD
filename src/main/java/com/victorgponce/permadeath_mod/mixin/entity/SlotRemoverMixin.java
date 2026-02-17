package com.victorgponce.permadeath_mod.mixin.entity;

import com.victorgponce.permadeath_mod.core.modules.day40.InventoryRestrictionHandler;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PlayerInventory.class)
public abstract class SlotRemoverMixin {
    @ModifyConstant(method = "getHotbarSize", constant = @Constant(intValue = 9))
    private static int modifyHotbarSize(int original) {
        Integer modified = InventoryRestrictionHandler.getModifiedHotbarSize();
        return modified != null ? modified : original;
    }

    @ModifyConstant(method = "isValidHotbarIndex", constant = @Constant(intValue = 9))
    private static int modifyHotbarValidation(int original) {
        Integer modified = InventoryRestrictionHandler.getModifiedHotbarSize();
        return modified != null ? modified : original;
    }
}
