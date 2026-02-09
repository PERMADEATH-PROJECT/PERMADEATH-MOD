package com.victorgponce.permadeath_mod.core.modules.day40;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

// Handles world generation tweaks: damaged elytra in end ships and mycelium mob spawning
public final class WorldGenHandler {

    private WorldGenHandler() {}

    /**
     * Creates a pre-damaged elytra (damage 432) to make the item less valuable
     * and force players to use mending or find multiple copies.
     */
    public static ItemStack createDamagedElytra() {
        ItemStack elytra = new ItemStack(Items.ELYTRA);
        elytra.setDamage(432);
        return elytra;
    }

    /**
     * Checks if a mob should be allowed to spawn on mycelium by treating it
     * like grass. Returns true if spawning is allowed, false to fall through to vanilla logic.
     */
    public static boolean canSpawnOnMycelium(EntityType<? extends MobEntity> type, WorldAccess world, BlockPos pos) {
        BlockPos downPos = pos.down();
        BlockState blockState = world.getBlockState(downPos);

        if (blockState.isOf(Blocks.MYCELIUM)) {
            BlockState grassState = Blocks.GRASS_BLOCK.getDefaultState();
            return grassState.allowsSpawning(world, downPos, type);
        }
        return false;
    }
}
