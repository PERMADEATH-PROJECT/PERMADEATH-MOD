package com.victorgponce.permadeath_mod.core.modules.day30;

import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

// Teleports ghasts near their target after shooting in the End dimension for relentless pressure
public final class GhastTeleportHandler {

    private GhastTeleportHandler() {}

    /**
     * Attempts to teleport a ghast near its target after firing a fireball.
     * Only activates in the End dimension to complement the dragon fight.
     * Tries 5 random positions with safety checks for height and clearance.
     */
    public static void tryTeleportAfterShot(GhastEntity ghast, int cooldown) {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 30) return;

        // The ghast mixin fires at cooldown == -40 after the shot
        if (cooldown != -40 || ghast.isDead()) return;

        LivingEntity target = ghast.getTarget();
        if (target == null) return;

        ServerWorld world = (ServerWorld) ghast.getEntityWorld();
        if (!world.getRegistryKey().equals(World.END)) return;

        for (int i = 0; i < 5; i++) {
            double dx = (target.getX() + 5) + (world.random.nextDouble() * 20 + 10);
            double dz = target.getZ() + (world.random.nextDouble() * 20 + 10);

            int topY = world.getTopY(Heightmap.Type.MOTION_BLOCKING, (int) Math.floor(dx), (int) Math.floor(dz));
            double dy = Math.max(target.getY() + 10, topY + 15.0);
            // Prevent teleporting below world floor
            dy = Math.max(dy, world.getBottomY() + 15);

            if (isAreaClear(world, ghast, dx, dy, dz)) {
                boolean success = ghast.teleport(dx, dy, dz, true);
                if (success) return;
            }
        }
    }

    private static boolean isAreaClear(ServerWorld world, GhastEntity ghast, double x, double y, double z) {
        return world.isSpaceEmpty(ghast.getBoundingBox()
                .expand(1.5)
                .offset(x - ghast.getX(), y - ghast.getY(), z - ghast.getZ()));
    }
}
