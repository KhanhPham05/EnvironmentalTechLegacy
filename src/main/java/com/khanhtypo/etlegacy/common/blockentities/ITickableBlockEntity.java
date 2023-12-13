package com.khanhtypo.etlegacy.common.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface ITickableBlockEntity {
    static void serverTick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (blockEntity instanceof ITickableBlockEntity tickableBlockEntity) {
            tickableBlockEntity.onServerTick(level, pos, state);
        }
    }

    default void onServerTick(Level level, BlockPos pos, BlockState state) {
    }
}
