package com.victorgponce.permadeath_mod.mixin.entity;

import com.victorgponce.permadeath_mod.core.modules.day25.GigaMobsHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MagmaCubeEntity.class)
public class MagmaCubeEntityMixin extends SlimeEntity {
    public MagmaCubeEntityMixin(EntityType<? extends SlimeEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "setSize", at = @At("HEAD"))
    private void onSetSize(int size, boolean heal, CallbackInfo ci) {
        if (GigaMobsHandler.shouldEnlargeGigaMagmacube()) {
            // super calls SlimeEntity.setSize directly, avoiding recursive mixin hook on MagmaCubeEntity
            super.setSize(16, false);
        }
    }
}
