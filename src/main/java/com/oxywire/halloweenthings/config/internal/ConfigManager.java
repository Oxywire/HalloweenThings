package com.oxywire.halloweenthings.config.internal;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager implements AutoCloseable {

    private static final Map<Class<?>, ConfigHandler<?>> CONFIGS = new HashMap<>();
    private final Path dataDirectory;

    public ConfigManager(Path dataDirectory) {
        this.dataDirectory = dataDirectory;

        if (!dataDirectory.toFile().exists()) {
            dataDirectory.toFile().mkdirs();
        }
    }

    public ConfigManager withConfigs(Class<?>... clazz) {
        for (Class<?> c : clazz) {
            try {
                CONFIGS.put(c, new ConfigHandler<>(c, dataDirectory));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return this;
    }

    public static <T> T getConfig(Class<T> config) {
        return (T) CONFIGS.get(config);
    }


    @Override
    public void close() throws Exception {
        for (ConfigHandler<?> config : CONFIGS.values()) {
            config.close();
        }
    }
}
