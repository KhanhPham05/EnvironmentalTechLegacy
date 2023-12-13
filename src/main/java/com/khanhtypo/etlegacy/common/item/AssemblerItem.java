package com.khanhtypo.etlegacy.common.item;

import com.khanhtypo.etlegacy.common.block.IMultiblockControllerBlock;
import com.khanhtypo.etlegacy.common.blockentities.IMBFormChecker;
import com.khanhtypo.etlegacy.data.TextConstants;
import com.khanhtypo.etlegacy.multiblockchecker.MultiblockChecker;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AssemblerItem extends Item {
    public AssemblerItem(Properties properties) {
        super(properties);
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) return InteractionResult.PASS;
        Player user = context.getPlayer();
        BlockEntity blockEntity = level.getBlockEntity(context.getClickedPos());
        if (!(blockEntity instanceof IMBFormChecker part)) return InteractionResult.PASS;
        if (user != null) {
            if (user.isShiftKeyDown()) {
                Block block = level.getBlockState(context.getClickedPos()).getBlock();
                if (block instanceof IMultiblockControllerBlock controllerBlock) {
                    MultiblockChecker checker = controllerBlock.getChecker();
                    if (part.isAlreadyFormed()) {
                        user.displayClientMessage(TextConstants.copyWithArgs(TextConstants.ALREADY_FORMED, checker.getName()), true);
                    } else {
                        if (user.isCreative()) {
                            checker.addAllToQueue(level, context.getClickedPos(), user);
                        } else {

                        }
                    }
                }
            } else if (user.isCreative()) {
                @Nullable Component[] components = part.debugMessages();
                if (components != null)
                    TextConstants.boxInBorder(components).forEach(component -> user.displayClientMessage(component, false));
            }
        }
        return InteractionResult.PASS;
    }
}
