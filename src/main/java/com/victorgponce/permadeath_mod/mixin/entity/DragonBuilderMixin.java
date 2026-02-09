package com.victorgponce.permadeath_mod.mixin.entity;

import com.victorgponce.permadeath_mod.core.modules.day30.DragonBattleHandler;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(EnderDragonFight.class)
public class DragonBuilderMixin {
    @Shadow @Nullable private UUID dragonUuid;
    @Shadow @Final private ServerWorld world;
    @Unique private EnderDragonEntity dragon;
    @Unique private static int accumulatedTicks = 0;

    @Inject(method = "createDragon", at = @At("RETURN"))
    private void onDragonCreation(CallbackInfoReturnable<EnderDragonEntity> cir) {
        this.dragon = cir.getReturnValue();
        DragonBattleHandler.enhanceDragon(this.dragon);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onDragonTick(CallbackInfo ci) {
        if (dragon == null) {
            EnderDragonEntity found = (EnderDragonEntity) this.world.getEntity(dragonUuid);
            if (found != null) {
                this.dragon = found;
            }
        } else {
            accumulatedTicks++;
            if (accumulatedTicks >= 20 * 30) {
                accumulatedTicks = 0;
                DragonBattleHandler.tickDragonReinforcements(this.dragon);
            }
        }
    }
}
