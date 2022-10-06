package com.oxywire.halloweenthings.effects;

import com.oxywire.halloweenthings.config.HalloweenThingsConfig;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PumpkinPieEffect implements Effect, Listener {

    public static final String ID = "pumpkinpie";
    private final Plugin plugin;
    private boolean enabled = false;

    public PumpkinPieEffect(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.PUMPKIN_PIE) {
            List<Config.ConfigurablePotionEffect> potionEffects = getConfig().getPotionEffects();
            event.getPlayer().addPotionEffect(potionEffects.get(ThreadLocalRandom.current().nextInt(potionEffects.size())).asPotionEffect());
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
        private List<ConfigurablePotionEffect> potionEffects = List.of(new ConfigurablePotionEffect());

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        public List<ConfigurablePotionEffect> getPotionEffects() {
            return potionEffects;
        }

        @ConfigSerializable
        public static class ConfigurablePotionEffect {

            @Setting
            private String key = "minecraft:levitation";
            @Setting
            private Duration duration = Duration.ofSeconds(10);
            @Setting
            private int amplifier = 256;
            @Setting
            private boolean ambient = true;
            @Setting
            private boolean particles = true;
            @Setting
            private boolean icon = true;

            public PotionEffect asPotionEffect() {
                return new PotionEffect(PotionEffectType.getByKey(NamespacedKey.fromString(key)), (int) (duration.toSeconds() * 20), amplifier, ambient, particles, icon);
            }
        }
    }
}
