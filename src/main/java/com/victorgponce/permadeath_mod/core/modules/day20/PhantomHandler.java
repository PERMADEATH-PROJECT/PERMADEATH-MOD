package com.victorgponce.permadeath_mod.core.modules.day20;

import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PhantomEntity;

// Modifies phantom size and health for day 20-39
public final class PhantomHandler {

    private PhantomHandler() {}

    public static void modifyPhantom(PhantomEntity phantom) {
        int day = ConfigFileManager.readConfig().getDay();
        if (day < 20) return;

        phantom.setPhantomSize(9);
        phantom.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(40.0);
        phantom.setHealth(40);
    }
}
