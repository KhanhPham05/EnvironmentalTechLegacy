package com.khanhtypo.etlegacy.common.block.multiblock;

import com.khanhtypo.etlegacy.common.block.DynamicShapeBlock;
import com.khanhtypo.etlegacy.common.blockentities.impl.MultiblockPartBlockEntity;
import com.khanhtypo.etlegacy.registration.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class MultiblockPartBlock extends DynamicShapeBlock implements EntityBlock {
    private boolean isFormed;

    public MultiblockPartBlock(Properties properties) {
        this(properties, Shapes.block());
    }

    public MultiblockPartBlock(Properties properties, Stream<VoxelShape> shapeConstructor) {
        this(properties, constructShape(shapeConstructor));
    }

    public MultiblockPartBlock(Properties properties, VoxelShape shape) {
        super(properties, shape);
        ModBlocks.MULTIBLOCK_PARTS.add(this);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MultiblockPartBlockEntity(pos, state);
    }
}
