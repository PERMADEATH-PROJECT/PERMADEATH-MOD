package com.victorgponce.permadeath_mod.mixin.entity;

import com.victorgponce.permadeath_mod.core.modules.day40.StructureDisablerHandler;
import com.mojang.datafixers.util.Either;
import net.minecraft.command.argument.RegistryPredicateArgumentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.command.LocateCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.gen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocateCommand.class)
public class LocateCommandMixin {
    @Inject(method = "executeLocateStructure", at = @At("HEAD"), cancellable = true)
    private static void onExecuteLocateStructure(
            ServerCommandSource source,
            RegistryPredicateArgumentType.RegistryPredicate<Structure> predicate,
            CallbackInfoReturnable<Integer> cir
    ) {
        Either<RegistryKey<Structure>, TagKey<Structure>> either = predicate.getKey();
        String structureId = either.map(
                key -> key.getValue().toString(),
                tag -> tag.id().toString()
        );
        if (StructureDisablerHandler.shouldBlockLocate(structureId)) {
            source.sendError(net.minecraft.text.Text.literal("This structure has been disabled!"));
            cir.setReturnValue(0);
        }
    }
}
