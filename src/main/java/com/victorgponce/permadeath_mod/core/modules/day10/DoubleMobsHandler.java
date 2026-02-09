package com.victorgponce.permadeath_mod.core.modules.day10;

import com.victorgponce.permadeath_mod.mixin.accessor.ChunkAreaAccessor;
import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.SpawnGroup;

// Doubles the mob cap when day >= 10
public final class DoubleMobsHandler {

    private DoubleMobsHandler() {}

    public static boolean shouldOverrideMobcap(SpawnGroup group, int spawningChunkCount, Object2IntOpenHashMap<SpawnGroup> groupToCount) {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 10) return false;

        int baseCap = group.getCapacity() * spawningChunkCount / ChunkAreaAccessor.getChunkArea();
        return groupToCount.getInt(group) < (baseCap * 2);
    }
}
