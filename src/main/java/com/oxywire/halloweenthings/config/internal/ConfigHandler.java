package com.oxywire.halloweenthings.config.internal;

import com.oxywire.halloweenthings.config.internal.serializer.DurationSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.reference.ConfigurationReference;
import org.spongepowered.configurate.reference.ValueReference;
import org.spongepowered.configurate.reference.WatchServiceListener;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;

public class ConfigHandler<T> implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger("HalloweenThings/ConfigHandler");
    private final WatchServiceListener listener;
    private final ConfigurationReference<CommentedConfigurationNode> base;
    private final ValueReference<T, CommentedConfigurationNode> config;

    public ConfigHandler(Class<T> clazz, Path dataDirectory) throws IOException {
        Path configPath = dataDirectory.resolve(clazz.getSimpleName() + ".yml");
        this.listener = WatchServiceListener.create();
        this.base = listener.listenToConfiguration(
                file -> YamlConfigurationLoader.builder()
                        .defaultOptions(options -> options
                                .shouldCopyDefaults(true)
                                .serializers(s -> s.register(DurationSerializer.INSTANCE))
                        )
                        .nodeStyle(NodeStyle.BLOCK)
                        .indent(2)
                        .path(configPath)
                        .build(),
                configPath
        );
        this.config = base.referenceTo(clazz);
        base.save();

        listener.listenToFile(configPath, event -> {
            LOGGER.info("Updated {}.yml", clazz.getSimpleName());
            if (getConfig() instanceof Config conf) {
                conf.onUpdate();
            }
        });
    }

    public T getConfig() {
        return config.get();
    }

    @Override
    public void close() throws Exception {
        listener.close();
        base.close();
    }
}
