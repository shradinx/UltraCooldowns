package me.shradinx.ultracooldowns;

import lombok.Getter;
import me.shradinx.ultracooldowns.cooldown.Cooldown;
import me.shradinx.ultracooldowns.cooldown.CooldownManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public final class UltraCooldowns extends JavaPlugin {
    
    /**
     * Main plugin instance
     */
    @Getter
    private static UltraCooldowns plugin;
    
    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        
        ConfigurationSection cooldowns = getConfig().getConfigurationSection("cooldowns");
        if (cooldowns != null) {
            for (String header : cooldowns.getKeys(false)) {
                Cooldown cd = (Cooldown) cooldowns.get(header, null);
                if (cd == null) continue;
                getLogger().info(String.format("Cooldown Started : %1$s | %2$s", cd.getPlayer(), cd.getReason()));
                cooldowns.set(header, null);
            }
            getConfig().set("cooldowns", cooldowns);
            saveConfig();
        }
        
        getLogger().info(String.format("%1$s Enabled!", getPluginMeta().getDisplayName()));
    }
    
    @Override
    public void onDisable() {
        ConfigurationSection cooldowns = getConfig().getConfigurationSection("cooldowns");
        if (cooldowns != null) {
            for (Cooldown cd : CooldownManager.getCooldowns()) {
                int id = cd.getId();
                String header = String.format("cooldown_%s", id);
                cooldowns.set(header, cd);
                cd.cancel();
            }
            getConfig().set("cooldowns", cooldowns);
            saveConfig();
        }
        
        getLogger().info(String.format("%1$s Disabled!", getPluginMeta().getDisplayName()));
    }
}
