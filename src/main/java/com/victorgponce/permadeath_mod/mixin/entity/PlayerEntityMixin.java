package com.victorgponce.permadeath_mod.mixin.entity;

import com.victorgponce.permadeath_mod.core.modules.common.ArmorBonusHandler;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Unique private boolean wasWearingFullNetherite = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        boolean wearingFull = ArmorBonusHandler.checkFullNetheriteArmor(self);
        if (wearingFull && !wasWearingFullNetherite) {
            ArmorBonusHandler.applyArmorBonus(self);
            wasWearingFullNetherite = true;
        } else if (!wearingFull && wasWearingFullNetherite) {
            ArmorBonusHandler.removeArmorBonus(self);
            wasWearingFullNetherite = false;
        }
    }
}
