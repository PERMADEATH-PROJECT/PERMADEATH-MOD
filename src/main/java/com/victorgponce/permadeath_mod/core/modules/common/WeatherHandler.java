package com.victorgponce.permadeath_mod.core.modules.common;

import com.victorgponce.permadeath_mod.config.Config;
import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

// Detects when a storm (Death Train) ends and broadcasts a message
public final class WeatherHandler {

    private WeatherHandler() {}

    public static void onThunderEnd(ServerWorld serverWorld) {
        serverWorld.getServer().getPlayerManager().broadcast(Text.literal("The Death Train has ended!")
                .formatted(Formatting.RED, Formatting.BOLD), false);
        Config cfg = ConfigFileManager.readConfig();
        cfg.setDeathTrain(false);
        ConfigFileManager.saveConfig(cfg);
    }

    public static boolean isDeathTrainActive() {
        return ConfigFileManager.readConfig().isDeathTrain();
    }
}
