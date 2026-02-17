package com.victorgponce.permadeath_mod.mixin.entity;

import com.victorgponce.permadeath_mod.core.modules.day40.StructureDisablerHandler;
import net.minecraft.entity.Entity;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.gen.structure.Structure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TradeOffers.SellMapFactory.class)
public class MapTradeMixin {
    @Shadow @Final private TagKey<Structure> structure;

    @Inject(method = "create", at = @At("HEAD"), cancellable = true)
    private void onCreate(Entity entity, Random random, CallbackInfoReturnable<TradeOffer> cir) {
        String tagId = this.structure.id().toString();
        if (StructureDisablerHandler.shouldBlockMapTrade(tagId)) {
            cir.setReturnValue(StructureDisablerHandler.createDummyTradeOffer(1, 0));
        }
    }
}
