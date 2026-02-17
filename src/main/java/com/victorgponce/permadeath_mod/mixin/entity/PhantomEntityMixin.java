package com.victorgponce.permadeath_mod.mixin.entity;

import com.victorgponce.permadeath_mod.core.modules.day20.PhantomHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PhantomEntity.class)
public class PhantomEntityMixin {
    @Inject(method = "initialize", at = @At("TAIL"))
    private void onInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CallbackInfoReturnable<EntityData> cir) {
        PhantomHandler.modifyPhantom((PhantomEntity) (Object) this);
    }

    // Stop skeleton rider's navigation each tick so it stays mounted on the phantom
    @Inject(method = "tick", at = @At("TAIL"))
    private void keepPassengerMounted(CallbackInfo ci) {
        PhantomHandler.handlePassengerAI((PhantomEntity) (Object) this);
    }
}
