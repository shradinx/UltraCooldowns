package me.shradinx.ultracooldowns.cooldown;

import org.bukkit.entity.Player;

import java.util.List;

public class CooldownUtils {
    /**
     * @param player Player to check for cooldown
     * @return True if player is on cooldown, otherwise false
     * @apiNote This method will return if the player is on cooldown for <strong>ANY</strong> reason!
     * To check for a specific reason, use {@link CooldownUtils#isOnCooldownForReason(Player, String)}
     */
    public static boolean isOnCooldown(Player player) {
        List<Cooldown> cooldowns = CooldownManager.getCooldowns();
        for (Cooldown cd : cooldowns) {
            if (!cd.getPlayer().equals(player.getUniqueId())) continue;
            return true;
        }
        return false;
    }
    
    /**
     * @param player Player to check for cooldown
     * @param reason Reason for cooldown
     * @return True if player is on cooldown for specified reason, otherwise false
     * @apiNote This method will return if the player is on cooldown for a <strong>specific</strong> reason!
     * To check for any reason, use {@link CooldownUtils#isOnCooldown(Player)}
     */
    public static boolean isOnCooldownForReason(Player player, String reason) {
        if (!isOnCooldown(player)) return false;
        for (Cooldown cd : CooldownManager.getCooldowns()) {
            if (!cd.getPlayer().equals(player.getUniqueId())) continue;
            if (!cd.getReason().equals(reason)) continue;
            return true;
        }
        return false;
    }
    
    public static String formatTimeLeft(int timeLeft) {
        int hours = timeLeft / 3600;
        int minutes = (timeLeft % 3600) / 60;
        int seconds = timeLeft % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
