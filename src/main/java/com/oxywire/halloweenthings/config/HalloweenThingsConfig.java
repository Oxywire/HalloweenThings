package com.oxywire.halloweenthings.config;

import com.oxywire.halloweenthings.config.internal.Config;
import com.oxywire.halloweenthings.config.internal.ConfigManager;
import com.oxywire.halloweenthings.effects.Effect;
import com.oxywire.halloweenthings.effects.JumpScareEffect;
import com.oxywire.halloweenthings.effects.PumpkinBombsEffect;
import com.oxywire.halloweenthings.effects.PumpkinPieEffect;
import com.oxywire.halloweenthings.effects.WitchNamesEffect;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.Map;

@ConfigSerializable
public class HalloweenThingsConfig implements Config {

    @Setting
    private Map<String, Effect.Config> effects = Map.of(
            JumpScareEffect.ID, new JumpScareEffect.Config(),
            WitchNamesEffect.ID, new WitchNamesEffect.Config(),
            PumpkinPieEffect.ID, new PumpkinPieEffect.Config(),
            PumpkinBombsEffect.ID, new PumpkinBombsEffect.Config()
    );

    @Override
    public void onUpdate() {
        Effect.EFFECTS.forEach((id, effect) -> {
            if (effects.get(id).isEnabled() && !effect.isEnabled()) {
                effect.enable();
            } else if (!effects.get(id).isEnabled() && effect.isEnabled()) {
                effect.disable();
            }
        });
    }

    public Map<String, Effect.Config> getEffects() {
        return effects;
    }

    public static HalloweenThingsConfig get() {
        return ConfigManager.getConfig(HalloweenThingsConfig.class);
    }
}
