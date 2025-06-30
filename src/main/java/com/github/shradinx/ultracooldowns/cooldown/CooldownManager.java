package com.github.shradinx.ultracooldowns.cooldown;

import lombok.Getter;
import com.github.shradinx.ultracooldowns.UltraCooldowns;
import org.bukkit.entity.Player;

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
    public static CooldownStatus register(Player player, int seconds, String reason, boolean showMessage) {
        if (seconds <= 0) return CooldownStatus.INVALID;
        Cooldown pCooldown = getCooldown(player, reason);
        CooldownStatus status = checkTimeLeft(pCooldown);
        if (status != CooldownStatus.NEW_COOLDOWN) return status;
        
        Random random = new Random();
        int id = random.nextInt(1, 1000);
        if (CooldownUtils.checkCooldownID(id)) return CooldownStatus.ON_COOLDOWN;
        Cooldown cd = new Cooldown(UltraCooldowns.getPlugin(), id, player.getUniqueId(), System.currentTimeMillis(), seconds, reason, showMessage);
        return CooldownStatus.NEW_COOLDOWN;
    }
    
    /**
     * @param cd Possibly-null Cooldown object
     * @return Status of cooldown based on the time left
     */
    private static CooldownStatus checkTimeLeft(@Nullable Cooldown cd) {
        if (cd == null) return CooldownStatus.NEW_COOLDOWN;
        
        long timeLeft = cd.getTimeLeft();
        if (timeLeft > 0) {
            cd.sendOnCooldownMessage();
            return CooldownStatus.ON_COOLDOWN;
        } else {
            cd.remove();
            cd.sendOffCooldownMessage();
            return CooldownStatus.OFF_COOLDOWN;
        }
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
    
    /**
     * @param player Player to remove all cooldowns for
     * @apiNote This will remove <b>ALL</b> cooldowns for the provided player
     */
    public static void removeAllCooldowns(Player player) {
        List<Cooldown> pCooldowns = getCooldowns(player);
        pCooldowns.forEach(Cooldown::remove);
    }
}
