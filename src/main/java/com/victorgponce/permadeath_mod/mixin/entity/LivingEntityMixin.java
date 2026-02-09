package com.victorgponce.permadeath_mod.mixin.entity;

import com.victorgponce.permadeath_mod.core.modules.common.RavagerDropHandler;
import com.victorgponce.permadeath_mod.core.modules.day20.MobDropsHandler;
import com.victorgponce.permadeath_mod.core.modules.day25.NetheriteGearDropHandler;
import com.victorgponce.permadeath_mod.core.modules.day30.TotemHandler;
import com.victorgponce.permadeath_mod.core.modules.day40.TotemDay40Handler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "dropLoot", at = @At("HEAD"), cancellable = true)
    private void onDropLoot(ServerWorld world, DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (RavagerDropHandler.handleRavagerDrop(self, world)) { ci.cancel(); return; }
        if (MobDropsHandler.shouldCancelDrop(self)) { ci.cancel(); }
    }

    @Inject(method = "tryUseDeathProtector", at = @At("HEAD"), cancellable = true)
    private void onTotemUse(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (TotemHandler.shouldDisableTotem()) {
            cir.setReturnValue(false);
            return;
        }
        if (TotemDay40Handler.shouldBlockTotem(self)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "onDeath", at = @At("TAIL"))
    private void onEntityDeath(DamageSource source, CallbackInfo ci) {
        NetheriteGearDropHandler.tryDropNetheriteGear((LivingEntity) (Object) this);
    }
}
