package com.khanhtypo.etlegacy.common.blockentities.impl;

import com.khanhtypo.etlegacy.common.blockentities.ILoadAndUnloadListener;
import com.khanhtypo.etlegacy.common.blockentities.IMBController;
import com.khanhtypo.etlegacy.common.blockentities.IMBPart;
import com.khanhtypo.etlegacy.registration.ModBlockEntities;
import jakarta.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MultiblockPartBlockEntity extends BlockEntity implements ILoadAndUnloadListener, IMBPart {
    //position of the controller is written if it is formed or loaded
    //and would be null again if it is deformed caused by player block break.
    private @Nullable BlockPos controlerBlockPos;

    public MultiblockPartBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.MULTIBLOCK_PARTS, pos, blockState);
    }

    public void onFormed(BlockPos controllerPos, Level level) {
        this.controlerBlockPos = controllerPos instanceof BlockPos.MutableBlockPos ? new BlockPos(controllerPos) : controllerPos;
    }

    @Override
    public void onPartDeformed(Level level, boolean isCausedByController) {
        //to prevent stack overflow, the following lines will only be called if a part in the structure is deformed.
        //Otherwise, if the controller caused the mb deform then just set the controller pos to null and do nothing else
        if (!isCausedByController)
            if (this.controlerBlockPos != null && level.getBlockEntity(this.controlerBlockPos) instanceof IMBController controller) {
                controller.onControllerDeformed(level);
            }
        this.controlerBlockPos = null;
    }

    @Override
    public Component[] debugMessages() {
        return new Component[]{
                Component.literal("controller pos : " + this.controlerBlockPos)
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        if (this.controlerBlockPos != null) {
            tag.putLong("controllerPos", this.controlerBlockPos.asLong());
        }
    }

    @Override
    public void load(CompoundTag tag) {
        this.controlerBlockPos = tag.contains("controllerPos", Tag.TAG_LONG) ? BlockPos.of(tag.getLong("controllerPos")) : null;
    }

    @Override
    public void onUnloading() {

    }

    @Override
    public void onLoad(ServerLevel world) {
    }

    public boolean isAlreadyFormed() {
        return this.controlerBlockPos != null;
    }
}
