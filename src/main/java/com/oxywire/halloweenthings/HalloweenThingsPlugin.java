package com.oxywire.halloweenthings;

import com.oxywire.halloweenthings.config.HalloweenThingsConfig;
import com.oxywire.halloweenthings.config.internal.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HalloweenThingsPlugin extends JavaPlugin {

    private final ConfigManager configManager = new ConfigManager(getDataFolder().toPath())
            .withConfigs(HalloweenThingsConfig.class);
    private static HalloweenThingsPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
        try {
            configManager.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HalloweenThingsPlugin getInstance() {
        return instance;
    }
}
