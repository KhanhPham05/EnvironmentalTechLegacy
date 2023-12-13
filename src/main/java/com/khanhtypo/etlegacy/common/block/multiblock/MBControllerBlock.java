package com.khanhtypo.etlegacy.common.block.multiblock;

import com.khanhtypo.etlegacy.common.block.DynamicShapeBlock;
import com.khanhtypo.etlegacy.common.block.IMultiblockControllerBlock;
import com.khanhtypo.etlegacy.common.blockentities.ITickableBlockEntity;
import com.khanhtypo.etlegacy.multiblockchecker.type.MultiblockType;
import jakarta.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public abstract class MBControllerBlock extends DynamicShapeBlock implements IMultiblockControllerBlock, EntityBlock {
    private final ResourceLocation checkerName;

    public MBControllerBlock(Properties properties, Stream<VoxelShape> shapeConstructor, ResourceLocation checkerName, MultiblockType multiblockType) {
        this(properties, constructShape(shapeConstructor), checkerName, multiblockType);
    }

    public MBControllerBlock(Properties properties, VoxelShape shape, ResourceLocation checkerName, MultiblockType multiblockType) {
        super(properties, shape);
        this.checkerName = checkerName;
        multiblockType.addBlock(this);
    }

    @Nonnull
    @Override
    public ResourceLocation getCheckerName() {
        return this.checkerName;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : BaseEntityBlock.createTickerHelper(blockEntityType, this.getBlockEntityType(), ITickableBlockEntity::serverTick);
    }

    protected abstract <T extends BlockEntity & ITickableBlockEntity> BlockEntityType<T> getBlockEntityType();
}
