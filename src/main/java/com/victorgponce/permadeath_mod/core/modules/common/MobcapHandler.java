package com.victorgponce.permadeath_mod.core.modules.common;

import com.victorgponce.permadeath_mod.util.mobcaps.MultiplayerHandler;
import com.victorgponce.permadeath_mod.util.mobcaps.SinglePlayerHandler;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

// Delegates mobcap counting on entity spawn/removal to the singleton handlers
public final class MobcapHandler {

    private static final ThreadLocal<Boolean> inCustomSpawn = ThreadLocal.withInitial(() -> false);

    private MobcapHandler() {}

    public static void onEntitySpawned(Entity entity) {
        if (inCustomSpawn.get()) return;
        try {
            inCustomSpawn.set(true);
            if (entity.getWorld() instanceof ServerWorld serverWorld) {
                MultiplayerHandler.getInstance().addEntity(serverWorld, entity.getType());
                SinglePlayerHandler.getInstance().addEntityType(entity.getType(), 1);
            }
        } finally {
            inCustomSpawn.set(false);
        }
    }

    public static void onEntityRemoved(Entity entity) {
        if (entity.getWorld() instanceof ServerWorld serverWorld) {
            MultiplayerHandler.getInstance().removeEntity(serverWorld, entity.getType());
            SinglePlayerHandler.getInstance().removeEntityType(entity.getType(), 1);
        }
    }
}
