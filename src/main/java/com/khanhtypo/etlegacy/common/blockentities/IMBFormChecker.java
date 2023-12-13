package com.khanhtypo.etlegacy.common.blockentities;

import jakarta.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public interface IMBFormChecker {
    boolean isAlreadyFormed();

    default void onDeformed(Level level) {
        if (this instanceof IMBPart part) {
            part.onPartDeformed(level, false);
        } else if (this instanceof IMBController controller) {
            controller.onControllerDeformed(level);
        }
    }

    @Nullable
    Component[] debugMessages();
}
