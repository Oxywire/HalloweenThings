package com.oxywire.halloweenthings.effects;

import com.oxywire.halloweenthings.config.HalloweenThingsConfig;
import com.oxywire.halloweenthings.message.Message;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.Plugin;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class WitchNamesEffect implements Effect, Listener {

    public static final String ID = "witchnames";
    private final Plugin plugin;
    private boolean enabled = false;

    public WitchNamesEffect(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if (event.getEntityType() == EntityType.WITCH) {
            List<String> names = getConfig().getWitchNames();
            event.getEntity().customName(Message.MINI_MESSAGE.deserialize(names.get(ThreadLocalRandom.current().nextInt(names.size()))));
            event.getEntity().setCustomNameVisible(true);
        }
    }

    @Override
    public void enable() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.enabled = true;
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);
        this.enabled = false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Config getConfig() {
        return ((Config) HalloweenThingsConfig.get().getEffects().get(ID));
    }

    @ConfigSerializable
    public static class Config implements Effect.Config {

        @Setting
        private boolean enabled = true;
        @Setting
        private List<String> witchNames = List.of("ugly witch1", "ugly witch2", "ugly witch3");

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        public List<String> getWitchNames() {
            return witchNames;
        }
    }
}
