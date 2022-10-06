package com.oxywire.halloweenthings;

import com.oxywire.halloweenthings.config.HalloweenThingsConfig;
import com.oxywire.halloweenthings.config.internal.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HalloweenThingsPlugin extends JavaPlugin {

    private ConfigManager configManager;
    private static HalloweenThingsPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        this.configManager = new ConfigManager(getDataFolder().toPath())
                .withConfigs(HalloweenThingsConfig.class);
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
