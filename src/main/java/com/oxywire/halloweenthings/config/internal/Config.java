package com.oxywire.halloweenthings.config.internal;

import java.nio.file.WatchEvent;

public interface Config {

    default void onUpdate(WatchEvent<?> event) {
    }
}
