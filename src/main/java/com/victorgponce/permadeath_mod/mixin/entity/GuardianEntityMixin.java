package com.victorgponce.permadeath_mod.mixin.entity;

import com.victorgponce.permadeath_mod.core.modules.day40.MobModifierHandler;
import net.minecraft.entity.mob.GuardianEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuardianEntity.class)
public class GuardianEntityMixin {
    @Inject(method = "getWarmupTime", at = @At("HEAD"), cancellable = true)
    private static void onGetWarmupTime(CallbackInfoReturnable<Integer> cir) {
        Integer warmup = MobModifierHandler.getGuardianWarmupTime();
        if (warmup != null) {
            cir.setReturnValue(warmup);
        }
    }
}
