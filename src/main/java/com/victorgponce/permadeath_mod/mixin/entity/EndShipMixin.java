package com.victorgponce.permadeath_mod.mixin.entity;

import com.victorgponce.permadeath_mod.core.modules.day40.WorldGenHandler;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.structure.EndCityGenerator;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndCityGenerator.Piece.class)
public class EndShipMixin {
    @Inject(method = "handleMetadata", at = @At("HEAD"), cancellable = true)
    private void onHandleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox, CallbackInfo ci) {
        if (metadata.startsWith("Elytra") && boundingBox.contains(pos) && World.isValid(pos)) {
            // Recreate the item frame with a pre-damaged elytra instead of the vanilla pristine one
            ItemFrameEntity itemFrameEntity = new ItemFrameEntity(
                world.toServerWorld(),
                pos,
                ((EndCityGenerator.Piece)(Object)this).getPlacementData().getRotation().rotate(Direction.SOUTH)
            );

            ItemStack customElytra = new ItemStack(Items.ELYTRA);
            customElytra.setDamage(432);
            itemFrameEntity.setHeldItemStack(customElytra, false);
            world.spawnEntity(itemFrameEntity);

            ci.cancel();
        }
    }
}
