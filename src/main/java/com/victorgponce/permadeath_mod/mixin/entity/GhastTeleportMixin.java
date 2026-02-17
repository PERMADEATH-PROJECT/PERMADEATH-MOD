package com.victorgponce.permadeath_mod.mixin.entity;

import com.victorgponce.permadeath_mod.core.modules.day30.GhastTeleportHandler;
import net.minecraft.entity.mob.GhastEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.entity.mob.GhastEntity$ShootFireballGoal")
public abstract class GhastTeleportMixin {
    @Shadow @Final private GhastEntity ghast;
    @Shadow private int cooldown;

    @Inject(method = "tick", at = @At("TAIL"))
    private void onAfterShoot(CallbackInfo ci) {
        GhastTeleportHandler.tryTeleportAfterShot(this.ghast, this.cooldown);
    }
}
