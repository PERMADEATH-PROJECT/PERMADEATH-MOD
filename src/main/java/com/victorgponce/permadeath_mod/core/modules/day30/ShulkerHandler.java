package com.victorgponce.permadeath_mod.core.modules.day30;

import com.victorgponce.permadeath_mod.data.BinaryServerDataHandler;
import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

// Turns shulkers into explosive hazards: TNT on death, TNT on bullet hit, and reduced drop rates
public final class ShulkerHandler {

    private ShulkerHandler() {}

    /**
     * Spawns TNT at the shulker's position when killed.
     * Uses a 40-tick fuse (2 seconds) to give players a brief escape window.
     */
    public static void onShulkerDeath(Entity entity, Entity.RemovalReason reason) {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 30) return;

        if (entity instanceof ShulkerEntity shulker && reason == Entity.RemovalReason.KILLED) {
            World world = shulker.getWorld();
            BlockPos pos = shulker.getBlockPos();

            TntEntity tnt = new TntEntity(EntityType.TNT, world);
            tnt.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            tnt.setFuse(40);
            world.spawnEntity(tnt);
        }
    }

    /**
     * Reduces shulker shell drop rate to 20% (or 40% if the double flag is active).
     * Returns true if the drop should be cancelled.
     */
    public static boolean shouldCancelShulkerDrop(LivingEntity entity) {
        if (entity.getType() != EntityType.SHULKER) return false;

        BinaryServerDataHandler config = BinaryServerDataHandler.getInstance();

        if (config.getDoubleShulkerShell()) {
            // With double flag, 40% chance to keep the drop
            if (Random.create().nextInt(100) > 40) return true;
        }

        // Base 20% chance to keep the drop
        return Random.create().nextInt(100) > 20;
    }

    /**
     * Spawns TNT at the impact point when a shulker bullet hits a player.
     * Uses a 20-tick fuse (1 second) for more immediate danger than the death variant.
     */
    public static void onBulletHitPlayer(HitResult hitResult, Entity projectile) {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 30) return;

        if (hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHit = (EntityHitResult) hitResult;
            if (entityHit.getEntity() instanceof PlayerEntity) {
                World world = projectile.getWorld();
                Vec3d pos = hitResult.getPos();

                TntEntity tnt = new TntEntity(EntityType.TNT, world);
                tnt.setPosition(pos.x, pos.y, pos.z);
                tnt.setFuse(20);
                world.spawnEntity(tnt);
            }
        }
    }
}
