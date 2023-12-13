package com.khanhtypo.etlegacy.common.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;


public interface IMBPart extends IMBFormChecker {
    void onFormed(BlockPos controllerBlockPos, Level level);
    BlockPos getBlockPos();
    void onPartDeformed(Level level, boolean isCausedByController);
}
