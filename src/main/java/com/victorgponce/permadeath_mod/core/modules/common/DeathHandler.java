package com.victorgponce.permadeath_mod.core.modules.common;

import com.victorgponce.permadeath_mod.data.DataBaseHandler;
import com.victorgponce.permadeath_mod.util.BanManager;
import com.victorgponce.permadeath_mod.util.ConfigFileManager;
import com.victorgponce.permadeath_mod.util.DeathTrain;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Handles player death: DB logging, death train activation, ban checks
public final class DeathHandler {

    private DeathHandler() {}

    public static void onPlayerDeath(ServerPlayerEntity player, DamageSource damageSource) {
        String playerName = player.getName().getString();
        String cause = getSimpleDeathCause(damageSource);

        String url = ConfigFileManager.readConfig().getJdbc();
        String user = ConfigFileManager.readConfig().getUser();
        String password = ConfigFileManager.readConfig().getPassword();

        Pattern pattern = Pattern.compile("^jdbc:mysql://([\\w.-]+)(?::(\\d+))?/([\\w]+)$");
        Matcher matcher = pattern.matcher(url);
        if (!matcher.matches()) {
            throw new RuntimeException("Invalid URL in the configuration file.");
        }

        String escapedPlayerName = playerName.replace("'", "''");
        String escapedCause = cause.replace("'", "''");

        String updateSql = "UPDATE Players SET Lives = Lives - 1, DeathCount = DeathCount + 1, " +
                "Status = CASE WHEN (Lives - 1) <= 0 THEN 'inactive' ELSE Status END " +
                "WHERE Username = ?";
        DataBaseHandler.databaseConnectorStatement(url, user, password, updateSql, escapedPlayerName);

        String insertDeathSql = "INSERT INTO Deaths (PlayerID, Cause) " +
                "VALUES ((SELECT PlayerID FROM Players WHERE Username = ?), ?)";
        DataBaseHandler.databaseConnectorStatement(url, user, password, insertDeathSql, escapedPlayerName, escapedCause);

        DeathTrain.enableDeathTrain(damageSource);
        BanManager.checkAndBan(player);
    }

    private static String getSimpleDeathCause(DamageSource damageSource) {
        if (damageSource.isOf(DamageTypes.IN_FIRE)) return "fire";
        if (damageSource.isOf(DamageTypes.CAMPFIRE)) return "campfire";
        if (damageSource.isOf(DamageTypes.LIGHTNING_BOLT)) return "lightning";
        if (damageSource.isOf(DamageTypes.ON_FIRE)) return "burns";
        if (damageSource.isOf(DamageTypes.LAVA)) return "lava";
        if (damageSource.isOf(DamageTypes.HOT_FLOOR)) return "hot floor";
        if (damageSource.isOf(DamageTypes.IN_WALL)) return "suffocation";
        if (damageSource.isOf(DamageTypes.CRAMMING)) return "cramming";
        if (damageSource.isOf(DamageTypes.DROWN)) return "drowning";
        if (damageSource.isOf(DamageTypes.STARVE)) return "starvation";
        if (damageSource.isOf(DamageTypes.CACTUS)) return "cactus";
        if (damageSource.isOf(DamageTypes.FALL)) return "fall";
        if (damageSource.isOf(DamageTypes.ENDER_PEARL)) return "ender pearl";
        if (damageSource.isOf(DamageTypes.FLY_INTO_WALL)) return "flying";
        if (damageSource.isOf(DamageTypes.OUT_OF_WORLD)) return "void";
        if (damageSource.isOf(DamageTypes.MAGIC)) return "magic";
        if (damageSource.isOf(DamageTypes.WITHER)) return "wither";
        if (damageSource.isOf(DamageTypes.DRAGON_BREATH)) return "dragon breath";
        if (damageSource.isOf(DamageTypes.DRY_OUT)) return "dehydration";
        if (damageSource.isOf(DamageTypes.SWEET_BERRY_BUSH)) return "berry bush";
        if (damageSource.isOf(DamageTypes.FREEZE)) return "freezing";
        if (damageSource.isOf(DamageTypes.STALAGMITE)) return "stalagmite";
        if (damageSource.isOf(DamageTypes.FALLING_BLOCK)) return "falling block";
        if (damageSource.isOf(DamageTypes.FALLING_ANVIL)) return "anvil";
        if (damageSource.isOf(DamageTypes.FALLING_STALACTITE)) return "stalactite";
        if (damageSource.isOf(DamageTypes.STING)) return "sting";
        if (damageSource.isOf(DamageTypes.MOB_ATTACK) || damageSource.isOf(DamageTypes.MOB_ATTACK_NO_AGGRO)) return "mob attack";
        if (damageSource.isOf(DamageTypes.PLAYER_ATTACK)) return "PvP";
        if (damageSource.isOf(DamageTypes.ARROW)) return "arrow";
        if (damageSource.isOf(DamageTypes.TRIDENT)) return "trident";
        if (damageSource.isOf(DamageTypes.MOB_PROJECTILE) || damageSource.isOf(DamageTypes.SPIT)) return "projectile";
        if (damageSource.isOf(DamageTypes.FIREWORKS)) return "fireworks";
        if (damageSource.isOf(DamageTypes.FIREBALL)) return "fireball";
        if (damageSource.isOf(DamageTypes.WITHER_SKULL)) return "wither skull";
        if (damageSource.isOf(DamageTypes.THORNS)) return "thorns";
        if (damageSource.isOf(DamageTypes.EXPLOSION) || damageSource.isOf(DamageTypes.PLAYER_EXPLOSION)) return "explosion";
        if (damageSource.isOf(DamageTypes.SONIC_BOOM)) return "sonic boom";
        if (damageSource.isOf(DamageTypes.BAD_RESPAWN_POINT)) return "respawn point";
        if (damageSource.isOf(DamageTypes.OUTSIDE_BORDER)) return "world border";
        if (damageSource.isOf(DamageTypes.MACE_SMASH)) return "mace smash";
        return "other";
    }
}
