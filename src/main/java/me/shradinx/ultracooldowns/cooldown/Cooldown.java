package me.shradinx.ultracooldowns.cooldown;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.shradinx.ultracooldowns.UltraCooldowns;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@SerializableAs("Cooldown")
public class Cooldown extends BukkitRunnable implements ConfigurationSerializable {
    @Setter(AccessLevel.PACKAGE)
    private long timeLeft;
    
    private final int id;
    
    private final long initialTime;
    private final int duration;
    private final String reason;
    private final boolean showMessage;
    private final UUID player;
    
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.PRIVATE)
    private long timeRan = 0L;
    
    @Getter(AccessLevel.NONE)
    private final JavaPlugin plugin;
    
    public Cooldown(JavaPlugin plugin, int id, UUID player, long initialTime, int duration, String reason, boolean showMessage) {
        this.player = player;
        this.initialTime = initialTime;
        this.duration = duration;
        this.timeLeft = duration;
        this.reason = reason;
        this.showMessage = showMessage;
        this.plugin = plugin;
        this.id = id;
        
        start();
    }
    
    public void start() {
        this.runTaskTimer(plugin, 0L, 20L);
        CooldownManager.getCooldowns().add(this);
    }
    
    @Override
    public void run() {
        if (timeRan >= duration) {
            sendOffCooldownMessage();
            remove();
            return;
        }
        timeRan++;
        timeLeft = (long) ((((double) initialTime / 1000) + duration)
            - ((double) System.currentTimeMillis() / 1000));
    }
    
    /**
     * @param message Message to send to player
     */
    public void sendActionBar(Component message) {
        if (!showMessage) {
            return;
        }
        Player p = plugin.getServer().getPlayer(player);
        if (p == null) return;
        p.sendActionBar(message);
    }
    
    /**
     * Send off cooldown message to player
     */
    public void sendOffCooldownMessage() {
        Component message = Component.text("You are no longer on cooldown!", NamedTextColor.GREEN);
        
        sendActionBar(message);
    }
    
    /**
     * Send on cooldown message to player
     */
    public void sendOnCooldownMessage() {
        Component message = Component.text("Time Left on " + reason +
            ": " + CooldownUtils.formatTimeLeft((int) timeLeft), NamedTextColor.RED);
        sendActionBar(message);
    }
    
    public void remove() {
        CooldownManager.getCooldowns().remove(this);
        this.cancel();
    }
    
    @Override
    public @NotNull Map<String, Object> serialize() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        
        result.put("id", id);
        result.put("reason", reason);
        result.put("player", player);
        result.put("initialTime", initialTime);
        result.put("duration", duration);
        result.put("timeRan", timeRan);
        result.put("showMessage", showMessage);
        
        return result;
    }
    
    public static Cooldown deserialize(Map<String, Object> args) {
        int id = -1;
        String reason = "";
        UUID player = null;
        long initialTime = 0;
        int duration = 0;
        long timeRan = 0;
        boolean showMessage = false;
        for (String key : args.keySet()) {
            switch (key) {
                case "id" -> id = Integer.parseInt(args.get(key).toString());
                case "reason" -> reason = args.get(key).toString();
                case "player" -> player = UUID.fromString(args.get(key).toString());
                case "initialTime" -> initialTime = Long.parseLong(args.get(key).toString());
                case "duration" -> duration = Integer.parseInt(args.get(key).toString());
                case "timeRan" -> timeRan = Long.parseLong((String) args.get(key));
                case "showMessage" -> showMessage = Boolean.parseBoolean((String) args.get(key));
            }
        }
        if (id == -1) return null;
        Cooldown cd = new Cooldown(UltraCooldowns.getPlugin(), id, player, initialTime, duration, reason, showMessage);
        cd.setTimeRan(timeRan);
        return cd;
    }
}
