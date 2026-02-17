package com.victorgponce.permadeath_mod.core.modules.day20;

import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import com.victorgponce.permadeath_mod.util.DropHelper;
import net.minecraft.entity.LivingEntity;

// Cancels drops for specific mobs on day 20+
public final class MobDropsHandler {

    private MobDropsHandler() {}

    public static boolean shouldCancelDrop(LivingEntity entity) {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 20) return false;
        return DropHelper.shouldCancelDrop(entity);
    }
}
