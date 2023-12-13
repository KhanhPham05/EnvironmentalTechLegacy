package com.khanhtypo.etlegacy.common.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Set;

public interface IMBController extends IMBFormChecker {
    void onFormed(Set<BlockPos> blockPos, Level level);
    void onControllerDeformed(Level level);
    void onPartLoaded(IMBPart part, Level level);
}
