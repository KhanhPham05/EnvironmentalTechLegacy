package com.khanhtypo.etlegacy.common.block.solargen;

import com.khanhtypo.etlegacy.common.block.multiblock.MBControllerBlock;
import com.khanhtypo.etlegacy.common.blockentities.ITickableBlockEntity;
import com.khanhtypo.etlegacy.common.blockentities.impl.SolarArrayControllerBlockEntity;
import com.khanhtypo.etlegacy.multiblockchecker.type.MultiblockType;
import com.khanhtypo.etlegacy.registration.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class SolarGeneratorControllerBlock extends MBControllerBlock {
    public SolarGeneratorControllerBlock(ResourceLocation checkerName) {
        super(Properties.copy(Blocks.COBBLESTONE).mapColor(MapColor.COLOR_BLACK),
                Stream.of(
                        box(0, 12, 0, 16, 16, 16),
                        box(0, 0, 0, 1, 12, 1),
                        box(0, 0, 15, 1, 12, 16),
                        box(15, 0, 0, 16, 12, 1),
                        box(15, 0, 15, 16, 12, 16),
                        box(1, 0, 1.77636e-15, 15, 1, 1),
                        box(1, 0, 15, 15, 1, 16),
                        box(0, 0, 1, 1, 1, 15),
                        box(15, 0, 1, 16, 1, 15),
                        box(2, 2, 2, 14, 12, 14)),
                checkerName, MultiblockType.SOLAR_ARRAY);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SolarArrayControllerBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected BlockEntityType<SolarArrayControllerBlockEntity> getBlockEntityType() {
        return ModBlockEntities.SOLAR_ARRAY_CONTROLLER;
    }
}