package com.oxywire.halloweenthings.effects;

import com.oxywire.halloweenthings.config.HalloweenThingsConfig;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class PumpkinBombsEffect implements Effect, Listener {

    public static final String ID = "pumpkinbombs";
    private static final Set<Material> EMPTY_SET = EnumSet.noneOf(Material.class);
    private final Plugin plugin;
    private boolean enabled = false;

    public PumpkinBombsEffect(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getKiller() == null) {
            return;
        }

        Set<Material> toDrop = getConfig().getDrops().getOrDefault(event.getEntityType(), EMPTY_SET);
        for (Material material : toDrop) {
            ItemStack item = new ItemStack(material);
            item.editMeta(meta -> meta.displayName(Component.text((char) ThreadLocalRandom.current().nextInt())));

            Item droppedItem = entity.getWorld().dropItemNaturally(entity.getLocation(), item);
            droppedItem.setCanMobPickup(false);
            droppedItem.setCanPlayerPickup(false);
            droppedItem.setCustomNameVisible(false);
            droppedItem.setTicksLived(5_800); // 10 seconds to 5 minutes
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
        private Map<EntityType, Set<Material>> drops = Map.of(
                EntityType.ZOMBIE, EnumSet.of(Material.CARVED_PUMPKIN, Material.ROTTEN_FLESH, Material.BONE, Material.REDSTONE),
                EntityType.SKELETON, EnumSet.of(Material.CARVED_PUMPKIN, Material.BONE, Material.ARROW, Material.REDSTONE)
        );

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        public Map<EntityType, Set<Material>> getDrops() {
            return drops;
        }
    }
}
