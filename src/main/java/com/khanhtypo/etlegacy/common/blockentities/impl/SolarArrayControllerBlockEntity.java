package com.khanhtypo.etlegacy.common.blockentities.impl;

import com.khanhtypo.etlegacy.common.blockentities.BaseMultiblockControllerBlockEntity;
import com.khanhtypo.etlegacy.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SolarArrayControllerBlockEntity extends BaseMultiblockControllerBlockEntity {
    public SolarArrayControllerBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SOLAR_ARRAY_CONTROLLER, pos, blockState);
    }

    @Override
    protected void onMultiblockTick(Level level) {

    }
}
