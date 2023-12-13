package com.khanhtypo.etlegacy.common.block.multiblock;

import com.khanhtypo.etlegacy.registration.ModBlocks;
import com.khanhtypo.etlegacy.registration.ModItems;
import com.khanhtypo.etlegacy.util.Tiers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class StructureFrameBlock extends MultiblockPartBlock {
    private StructureFrameBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(2, 3.5f).sound(SoundType.STONE));
    }

    public static StructureFrameBlock register(Tiers tiers) {
        ResourceLocation id = ModItems.id(tiers.toSimpleName() + "_structure_frame");
        return tiers.setStructureFrame(ModBlocks.register(id.getPath(), new StructureFrameBlock()), id);
    }
}
