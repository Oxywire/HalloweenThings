package com.oxywire.halloweenthings.effects;

import com.oxywire.halloweenthings.HalloweenThingsPlugin;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Map;

public interface Effect {

    Map<String, Effect> EFFECTS = Map.of(
            JumpScareEffect.ID, new JumpScareEffect(),
            WitchNamesEffect.ID, new WitchNamesEffect(HalloweenThingsPlugin.getInstance()),
            PumpkinPieEffect.ID, new PumpkinPieEffect(HalloweenThingsPlugin.getInstance()),
            PumpkinBombsEffect.ID, new PumpkinBombsEffect(HalloweenThingsPlugin.getInstance())
    );

    void enable();
    void disable();
    boolean isEnabled();
    Config getConfig();


    @ConfigSerializable
    interface Config {

        boolean isEnabled();
    }
}
