package com.victorgponce.permadeath_mod.mixin.entity;

import com.victorgponce.permadeath_mod.core.modules.day25.GigaMobsHandler;
import net.minecraft.entity.mob.GhastEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GhastEntity.class)
public class GhastEntityMixin {
    @Inject(method = "getFireballStrength", at = @At("HEAD"), cancellable = true)
    private void onGetFireballStrength(CallbackInfoReturnable<Integer> cir) {
        Integer strength = GigaMobsHandler.getDemonicFireballStrength();
        if (strength != null) {
            cir.setReturnValue(strength);
        }
    }
}
