package me.shradinx.ultracooldowns.cooldown;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CooldownManager {
    @Getter
    private static final List<Cooldown> cooldowns = new ArrayList<>();
    /**
     * @param player Player to apply cooldown to
     * @param seconds Duration of cooldown
     * @param reason Reason for cooldown
     * @param showMessage Whether to show cooldown message as actionbar
     * @return Status of new or existing cooldown
     */
    public static CooldownStatus handleCooldown(JavaPlugin plugin, Player player, int seconds, String reason, boolean showMessage) {
        if (seconds <= 0) return CooldownStatus.INVALID;
        Cooldown pCooldown = getCooldown(player, reason);
        if (pCooldown != null) {
            long timeLeft = pCooldown.getTimeLeft();
            if (timeLeft > 0) {
                pCooldown.sendOnCooldownMessage();
                return CooldownStatus.ON_COOLDOWN;
            } else {
                pCooldown.remove();
                pCooldown.sendOffCooldownMessage();
                return CooldownStatus.OFF_COOLDOWN;
            }
        }
        
        Random random = new Random();
        int id = random.nextInt(1, 1000);
        if (!cooldowns.stream().filter(c -> c.getId() == id).toList().isEmpty()) return CooldownStatus.ON_COOLDOWN;
        Cooldown cd = new Cooldown(plugin, id, player.getUniqueId(), System.currentTimeMillis(), seconds, reason, showMessage);
        return CooldownStatus.NEW_COOLDOWN;
    }
    
    /**
     * @param player Player to get all cooldowns for
     * @return Possibly-empty list of active cooldowns associated with the player
     */
    public static List<Cooldown> getCooldowns(Player player) {
        List<Cooldown> playerCooldowns = new ArrayList<>();
        for (Cooldown cd : cooldowns) {
            if (cd.getPlayer() != player.getUniqueId()) continue;
            playerCooldowns.add(cd);
        }
        return playerCooldowns;
    }
    
    /**
     * @param player Player to get a cooldown for
     * @param reason Reason for cooldown
     * @return Possibly-null active cooldown associated with player
     */
    public static @Nullable Cooldown getCooldown(Player player, String reason) {
        List<Cooldown> pCooldowns = getCooldowns(player);
        for (Cooldown cd : pCooldowns) {
            if (!cd.getReason().equals(reason)) continue;
            return cd;
        }
        return null;
    }
    
    public static void removeAllCooldowns(Player player) {
        List<Cooldown> pCooldowns = getCooldowns(player);
        pCooldowns.forEach(Cooldown::remove);
    }
}
