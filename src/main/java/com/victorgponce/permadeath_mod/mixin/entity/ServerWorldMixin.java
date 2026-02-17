package com.victorgponce.permadeath_mod.mixin.entity;

import com.victorgponce.permadeath_mod.core.modules.common.MobcapHandler;
import com.victorgponce.permadeath_mod.core.modules.common.SpiderEffectsHandler;
import com.victorgponce.permadeath_mod.core.modules.common.WeatherHandler;
import com.victorgponce.permadeath_mod.core.modules.day20.CustomSkeletonsHandler;
import com.victorgponce.permadeath_mod.core.modules.day25.DeathTrainEffectsHandler;
import com.victorgponce.permadeath_mod.core.modules.day25.GigaMobsHandler;
import com.victorgponce.permadeath_mod.core.modules.day25.RavagerEffectsHandler;
import com.victorgponce.permadeath_mod.core.modules.day30.EntityTransformationHandler;
import com.victorgponce.permadeath_mod.core.modules.day30.SkeletonDay30Handler;
import com.victorgponce.permadeath_mod.core.modules.day40.MobModifierHandler;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    @Unique
    private boolean previousThunderingState = false;

    @Inject(method = "addEntity", at = @At("HEAD"), cancellable = true)
    private void onEntitySpawn(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        MobcapHandler.onEntitySpawned(entity);
        SpiderEffectsHandler.handleSpiderSpawn(entity);
        DeathTrainEffectsHandler.applyDeathTrainEffects(entity);
        RavagerEffectsHandler.applyRavagerEffects(entity);

        // Apply per-entity ghast attributes since createGhastAttributes only runs once at startup
        if (entity instanceof net.minecraft.entity.mob.GhastEntity ghast) {
            GigaMobsHandler.applyDemonicGhastAttributes(ghast);
        }

        if (CustomSkeletonsHandler.handleEntitySpawn(entity)) { cir.setReturnValue(false); return; }
        if (SkeletonDay30Handler.handleEntitySpawn(entity)) { cir.setReturnValue(false); return; }
        if (EntityTransformationHandler.handleEntitySpawn(entity)) { cir.setReturnValue(false); return; }
        MobModifierHandler.applyMobEffects(entity);
        MobModifierHandler.handlePhantomRider(entity);
        if (MobModifierHandler.handleSpiderTransformation(entity)) { cir.setReturnValue(false); }
    }

    @Inject(method = "tickWeather", at = @At("TAIL"))
    private void onTickWeather(CallbackInfo ci) {
        ServerWorld self = (ServerWorld) (Object) this;
        if (previousThunderingState && !self.isThundering()) {
            if (WeatherHandler.isDeathTrainActive()) {
                WeatherHandler.onThunderEnd(self);
            }
        }
        previousThunderingState = self.isThundering();
    }
}
