package com.khanhtypo.etlegacy.common.block;

import jakarta.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.stream.Stream;

public class DynamicShapeBlock extends Block {
    private final VoxelShape shape;
    public DynamicShapeBlock(Properties properties, Stream<VoxelShape> shapeConstructor) {
        this(properties, constructShape(shapeConstructor));
    }

    public DynamicShapeBlock(Properties properties, VoxelShape shape) {
        super(properties.noOcclusion());
        this.shape = shape;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.shape;
    }

    @SuppressWarnings("DataFlowIssue")
    protected static VoxelShape constructShape(@Nonnull Stream<VoxelShape> shapeConstructor) {
        return shapeConstructor.reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).orElse(null);
    }
}
