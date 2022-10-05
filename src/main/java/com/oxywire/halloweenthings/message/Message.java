package com.oxywire.halloweenthings.message;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.time.Duration;

@ConfigSerializable
public class Message {

    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    @Setting
    private String message = null;
    @Setting
    private Title title = null;
    @Setting
    private Sound sound = null;
    @Setting
    private Particle particle = null;

    public void send(Audience audience, TagResolver... placeholders) {
        if (message != null) {
            audience.sendMessage(MINI_MESSAGE.deserialize(message, placeholders));
        }
        if (title != null) {
            audience.showTitle(title.asTitle(placeholders));
        }
        if (sound != null) {
            audience.playSound(sound.asSound());
        }

        if (sound != null && audience instanceof Player player) {
            player.spawnParticle(particle.getParticle(), player.getLocation(), particle.getAmount(), particle.getOffsetX(), particle.getOffsetY(), particle.getOffsetZ());
        }
    }

    public Message setMessage(String message) {
        this.message = message;
        return this;
    }

    public Message setTitle(Title title) {
        this.title = title;
        return this;
    }

    public Message setSound(Sound sound) {
        this.sound = sound;
        return this;
    }

    @ConfigSerializable
    public static class Title {

        @Setting
        private String title;
        @Setting
        private String subTitle;
        @Setting
        private Duration fadeIn;
        @Setting
        private Duration stay;
        @Setting
        private Duration fadeOut;

        public net.kyori.adventure.title.Title asTitle(TagResolver... placeholders) {
            Component title = this.title == null ? Component.empty() : MINI_MESSAGE.deserialize(this.title, placeholders);
            Component subTitle = this.subTitle == null ? Component.empty() : MINI_MESSAGE.deserialize(this.subTitle, placeholders);

            if (fadeIn == null || stay == null || fadeOut == null) {
                return net.kyori.adventure.title.Title.title(title, subTitle);
            } else {
                return net.kyori.adventure.title.Title.title(title, subTitle, net.kyori.adventure.title.Title.Times.times(fadeIn, stay, fadeOut));
            }
        }

        public Title setTitle(String title) {
            this.title = title;
            return this;
        }

        public Title setSubTitle(String subTitle) {
            this.subTitle = subTitle;
            return this;
        }

        public Title setFadeIn(Duration fadeIn) {
            this.fadeIn = fadeIn;
            return this;
        }

        public Title setStay(Duration stay) {
            this.stay = stay;
            return this;
        }

        public Title setFadeOut(Duration fadeOut) {
            this.fadeOut = fadeOut;
            return this;
        }
    }

    @ConfigSerializable
    public static class Sound {

        @Setting
        private String key = "block.bell.use";
        @Setting
        private net.kyori.adventure.sound.Sound.Source source = net.kyori.adventure.sound.Sound.Source.MASTER;
        @Setting
        private float volume = 1.0f;
        @Setting
        private float pitch = 1.0f;

        public net.kyori.adventure.sound.Sound asSound() {
            return net.kyori.adventure.sound.Sound.sound(
                    Key.key(key),
                    source,
                    volume,
                    pitch
            );
        }

        public Sound setKey(String key) {
            this.key = key;
            return this;
        }

        public Sound setSource(net.kyori.adventure.sound.Sound.Source source) {
            this.source = source;
            return this;
        }

        public Sound setVolume(float volume) {
            this.volume = volume;
            return this;
        }

        public Sound setPitch(float pitch) {
            this.pitch = pitch;
            return this;
        }
    }

    @ConfigSerializable
    public static class Particle {

        @Setting
        private org.bukkit.Particle particle = org.bukkit.Particle.SPIT;
        @Setting
        private int amount = 1;
        @Setting
        private double offsetX = 0;
        @Setting
        private double offsetY = 0;
        @Setting
        private double offsetZ = 0;

        public org.bukkit.Particle getParticle() {
            return particle;
        }

        public int getAmount() {
            return amount;
        }

        public double getOffsetX() {
            return offsetX;
        }

        public double getOffsetY() {
            return offsetY;
        }

        public double getOffsetZ() {
            return offsetZ;
        }
    }
}
