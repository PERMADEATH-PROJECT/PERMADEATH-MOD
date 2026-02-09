package com.victorgponce.permadeath_mod.mixin.entity;

import com.victorgponce.permadeath_mod.core.modules.day10.DoubleMobsHandler;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnHelper.Info.class)
public class SpawnHelperMixin {
    @Shadow @Final private int spawningChunkCount;
    @Shadow @Final private Object2IntOpenHashMap<SpawnGroup> groupToCount;

    @Inject(method = "isBelowCap", at = @At("HEAD"), cancellable = true)
    private void onIsBelowCap(SpawnGroup group, CallbackInfoReturnable<Boolean> cir) {
        if (DoubleMobsHandler.shouldOverrideMobcap(group, this.spawningChunkCount, this.groupToCount)) {
            cir.setReturnValue(true);
        }
    }
}
