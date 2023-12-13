package com.khanhtypo.etlegacy.common.blockentities;

import jakarta.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public abstract class BaseMultiblockControllerBlockEntity extends BlockEntity implements ILoadAndUnloadListener, IMBController, ITickableBlockEntity {

    private @Nullable Set<BlockPos> partPositions;
    private boolean hasDataChanged;

    public BaseMultiblockControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void onServerTick(Level level, BlockPos pos, BlockState state) {
        if (this.isAlreadyFormed()) {
            this.onMultiblockTick(level);
        }
        if (this.hasDataChanged()) {
            setChanged(level, pos, state);
        }
    }

    private boolean hasDataChanged() {
        if (this.hasDataChanged) {
            this.hasDataChanged = false;
            return true;
        }
        return false;
    }

    protected abstract void onMultiblockTick(Level level);

    protected void markChanged() {
        this.hasDataChanged = true;
    }

    @Override
    public void onFormed(Set<BlockPos> blockPos, Level level) {
        this.partPositions = blockPos;
        this.partPositions.stream().map(level::getBlockEntity)
                .map(be -> be instanceof IMBPart part ? part : null)
                .filter(Objects::nonNull)
                .forEach(part -> part.onFormed(this.getBlockPos(), level));
    }

    @Override
    public void onControllerDeformed(Level level) {
        if (this.partPositions != null) {
            this.partPositions.stream().map(level::getBlockEntity)
                    .map(be -> be instanceof IMBPart part ? part : null)
                    .filter(Objects::nonNull)
                    .forEach(part -> part.onPartDeformed(level, true));
            this.partPositions = null;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        if (this.partPositions != null) {
            tag.put("partPositions", new LongArrayTag(this.partPositions.stream().map(BlockPos::asLong).toList()));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag.contains("partPositions", Tag.TAG_LONG_ARRAY)) {
            this.partPositions = Util.make(new TreeSet<>(), set -> Arrays.stream(tag.getLongArray("partPositions")).mapToObj(BlockPos::of).forEach(set::add));
        }
    }

    @Override
    public void onUnloading() {
    }

    @Override
    public void onLoad(ServerLevel world) {

    }

    @Override
    public void onPartLoaded(IMBPart part, Level level) {
    }

    @Override
    public boolean isAlreadyFormed() {
        return this.partPositions != null && !this.partPositions.isEmpty();
    }

    @Override
    public Component[] debugMessages() {
        return null;
    }
}
