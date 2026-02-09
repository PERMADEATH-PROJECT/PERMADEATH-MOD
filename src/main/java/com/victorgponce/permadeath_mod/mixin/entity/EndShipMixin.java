package com.victorgponce.permadeath_mod.mixin.entity;

import com.victorgponce.permadeath_mod.core.modules.day40.WorldGenHandler;
import net.minecraft.structure.EndCityGenerator;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndCityGenerator.Piece.class)
public class EndShipMixin {
    @Inject(method = "handleMetadata", at = @At("HEAD"), cancellable = true)
    private void onHandleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox, CallbackInfo ci) {
        if ("Elytra".equals(metadata)) {
            net.minecraft.item.ItemStack damagedElytra = WorldGenHandler.createDamagedElytra();
            net.minecraft.block.entity.BlockEntity blockEntity = world.getBlockEntity(pos.down());
            if (blockEntity instanceof net.minecraft.block.entity.ChestBlockEntity chest) {
                chest.setStack(0, damagedElytra);
            }
        }
    }
}
