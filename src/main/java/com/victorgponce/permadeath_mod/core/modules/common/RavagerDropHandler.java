package com.victorgponce.permadeath_mod.core.modules.common;

import com.victorgponce.permadeath_mod.drops.ravager.RavagerDropDay20;
import com.victorgponce.permadeath_mod.drops.ravager.RavagerDropDay25;
import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;

// Handles custom Ravager drops based on the current day
public final class RavagerDropHandler {

    private RavagerDropHandler() {}

    // Returns true if the default loot should be cancelled
    public static boolean handleRavagerDrop(LivingEntity entity, ServerWorld world) {
        if (!(entity instanceof RavagerEntity)) return false;

        int day = ConfigFileManager.readConfig().getDay();
        if (day >= 20 && day < 25) {
            new RavagerDropDay20().applyDrops(entity, Items.TOTEM_OF_UNDYING, world);
            return true;
        } else if (day >= 25) {
            new RavagerDropDay25().applyDrops(entity, Items.TOTEM_OF_UNDYING, world);
            return true;
        }
        return false;
    }
}
