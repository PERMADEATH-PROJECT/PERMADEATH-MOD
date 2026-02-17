package com.victorgponce.permadeath_mod.mixin.entity;

import com.victorgponce.permadeath_mod.core.modules.day40.StructureDisablerHandler;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.structure.StructureSet;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkGenerator.class)
public class StructureMixin {
    @Inject(method = "trySetStructureStart", at = @At("HEAD"), cancellable = true)
    private void onTrySetStructureStart(
            StructureSet.WeightedEntry weightedEntry,
            StructureAccessor structureAccessor,
            DynamicRegistryManager dynamicRegistryManager,
            NoiseConfig noiseConfig,
            StructureTemplateManager structureManager,
            long seed,
            Chunk chunk,
            ChunkPos pos,
            ChunkSectionPos sectionPos,
            RegistryKey<World> dimension,
            CallbackInfoReturnable<Boolean> cir
    ) {
        String structureId = weightedEntry.structure().getIdAsString();
        if (StructureDisablerHandler.shouldBlockStructure(structureId)) {
            cir.setReturnValue(false);
        }
    }
}
