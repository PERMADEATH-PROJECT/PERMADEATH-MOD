package com.victorgponce.permadeath_mod.mixin.common;

import com.victorgponce.permadeath_mod.util.mobcaps.MultiplayerHandler;
import com.victorgponce.permadeath_mod.util.mobcaps.SinglePlayerHandler;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityRemovalMixin {

    @Inject(method = "remove", at = @At("HEAD"))
    private void onEntityRemoved(Entity.RemovalReason reason, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;

        if (entity.getWorld() instanceof ServerWorld serverWorld) {
            // Actualizar MultiplayerHandler
            MultiplayerHandler.getInstance().removeEntity(serverWorld, entity.getType());

            // Actualizar SinglePlayerHandler
            SinglePlayerHandler.getInstance().removeEntityType(entity.getType(), 1);
        }
    }
}