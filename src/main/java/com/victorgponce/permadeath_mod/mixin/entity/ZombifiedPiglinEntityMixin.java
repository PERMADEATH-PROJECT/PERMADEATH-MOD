package com.victorgponce.permadeath_mod.mixin.entity;

import com.victorgponce.permadeath_mod.core.modules.day20.AggressiveMobsHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombifiedPiglinEntity.class)
public class ZombifiedPiglinEntityMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(EntityType<? extends ZombifiedPiglinEntity> type, World world, CallbackInfo ci) {
        AggressiveMobsHandler.setupAngryPiglin((ZombifiedPiglinEntity) (Object) this);
    }

    @Inject(method = "initCustomGoals", at = @At("TAIL"))
    private void onInitCustomGoals(CallbackInfo ci) {
        AggressiveMobsHandler.forceTargetPlayer((ZombifiedPiglinEntity) (Object) this);
    }
}
