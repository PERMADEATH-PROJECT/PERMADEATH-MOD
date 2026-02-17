package com.victorgponce.permadeath_mod.mixin.entity;

import com.victorgponce.permadeath_mod.core.modules.day30.ShulkerHandler;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBulletEntity.class)
public class ShulkerBulletMixin {
    @Inject(method = "onCollision", at = @At("HEAD"))
    private void onProjectileHit(HitResult hitResult, CallbackInfo ci) {
        ShulkerHandler.onBulletHitPlayer(hitResult, (ShulkerBulletEntity) (Object) this);
    }
}
