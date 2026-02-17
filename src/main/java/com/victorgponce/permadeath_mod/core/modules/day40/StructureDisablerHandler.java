package com.victorgponce.permadeath_mod.core.modules.day40;

import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.minecraft.world.gen.structure.Structure;

import java.util.Optional;
import java.util.Set;

// Blocks specific structure generation, locate commands, and map trades after day 40
public final class StructureDisablerHandler {

    // Structures blocked from world generation and /locate
    private static final Set<String> BLOCKED_STRUCTURES = Set.of(
            "minecraft:ancient_city",
            "minecraft:mansion",
            "minecraft:fortress",
            "minecraft:bastion_remnant",
            "minecraft:trial_chambers",
            "minecraft:monument",
            "minecraft:desert_pyramid",
            "minecraft:jungle_pyramid"
    );

    // Villager trade tags for structure maps that should be disabled
    private static final Set<String> BLOCKED_TRADE_TAGS = Set.of(
            "minecraft:on_trial_chambers_maps",
            "minecraft:on_treasure_maps",
            "minecraft:on_woodland_explorer_maps",
            "minecraft:shipwreck",
            "minecraft:on_ocean_explorer_maps",
            "minecraft:on_jungle_explorer_maps"
    );

    private StructureDisablerHandler() {}

    /**
     * Returns true if the structure should be prevented from generating.
     */
    public static boolean shouldBlockStructure(String structureId) {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 40) return false;
        return BLOCKED_STRUCTURES.contains(structureId);
    }

    /**
     * Extracts the structure ID string from a locate command predicate.
     * Handles both direct key-based and tag-based lookups.
     */
    public static String extractStructureId(Optional<RegistryKey<Structure>> keyOpt, Optional<TagKey<Structure>> tagOpt) {
        if (keyOpt.isPresent()) {
            return keyOpt.get().getValue().toString();
        }

        if (tagOpt.isPresent()) {
            Identifier tagId = tagOpt.get().id();
            String namespace = tagId.getNamespace();
            String[] parts = tagId.getPath().split("/");
            String name = parts[parts.length - 1];
            return namespace + ":" + name;
        }

        return null;
    }

    /**
     * Returns true if the locate command should be blocked for this structure.
     */
    public static boolean shouldBlockLocate(String structureId) {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 40) return false;
        return structureId != null && BLOCKED_STRUCTURES.contains(structureId);
    }

    /**
     * Returns true if a villager map trade should be replaced with a dummy offer.
     */
    public static boolean shouldBlockMapTrade(String tradeTagId) {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 40) return false;
        return BLOCKED_TRADE_TAGS.contains(tradeTagId);
    }

    /**
     * Creates a dummy trade offer that replaces blocked map trades.
     * Uses a renamed structure void item so players know the trade is disabled.
     */
    public static TradeOffer createDummyTradeOffer(int maxUses, int experience) {
        ItemStack structureVoidRenamed = new ItemStack(Items.STRUCTURE_VOID);
        structureVoidRenamed.set(DataComponentTypes.CUSTOM_NAME, Text.literal("Structure Maps Trade Disabled"));

        return new TradeOffer(
                new TradedItem(Items.STRUCTURE_VOID, 1),
                structureVoidRenamed,
                maxUses,
                experience,
                0.2F
        );
    }
}
