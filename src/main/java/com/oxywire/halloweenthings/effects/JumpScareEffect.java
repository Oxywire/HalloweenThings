package com.oxywire.halloweenthings.effects;

import com.oxywire.halloweenthings.config.HalloweenThingsConfig;
import com.oxywire.halloweenthings.message.Message;
import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class JumpScareEffect implements Effect {

    public static final String ID = "jumpscare";
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private @Nullable ScheduledFuture<?> future = null;

    @Override
    public void enable() {
        this.future = executorService.scheduleAtFixedRate(
                () -> getConfig().getMessage().send(Bukkit.getServer()),
                0,
                getConfig().getDuration().getSeconds(),
                TimeUnit.SECONDS
        );
    }

    @Override
    public void disable() {
        if (future != null) {
            future.cancel(true);
            this.future = null;
        }
    }

    @Override
    public boolean isEnabled() {
        return future != null;
    }

    @Override
    public Config getConfig() {
        return ((Config) HalloweenThingsConfig.get().getEffects().get(ID));
    }

    @ConfigSerializable
    public static class Config implements Effect.Config {

        @Setting
        private boolean enabled = false;
        @Setting
        private Duration duration = Duration.ofMinutes(5);
        @Setting
        private Message message = new Message()
                .setTitle(
                        new Message.Title()
                                .setTitle("Jump Scare!")
                                .setSubTitle("You have been jump scared!")
                                .setFadeIn(Duration.ofSeconds(5))
                                .setStay(Duration.ofSeconds(5))
                                .setFadeOut(Duration.ofSeconds(5))
                )
                .setSound(new Message.Sound());

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        public Duration getDuration() {
            return duration;
        }

        public Message getMessage() {
            return message;
        }
    }
}
