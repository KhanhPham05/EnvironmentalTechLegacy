package com.khanhtypo.etlegacy.common.block.solargen;

import com.khanhtypo.etlegacy.common.block.multiblock.MultiblockPartBlock;
import com.khanhtypo.etlegacy.util.Tiers;
import net.minecraft.world.level.block.Block;

public class SolarCellBlock extends MultiblockPartBlock {
    private final Tiers tiers;

    public SolarCellBlock(Tiers tiers, Properties properties) {
        super(properties, Block.box(0, 0, 0, 16, 13, 16));
        this.tiers = tiers;
    }

    public Tiers getTier() {
        return this.tiers;
    }

}
