package com.khanhtypo.etlegacy.util;

import com.google.common.base.Preconditions;
import com.khanhtypo.etlegacy.common.block.multiblock.StructureFrameBlock;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;

public enum Tiers implements ISimplifiedName {
    LITHERITE,
    ERODIUM,
    KYRONITE,
    PLADIUM,
    IONITE,
    AETHIUM;

    private Pair<StructureFrameBlock, ResourceLocation> structureFrame;

    public StructureFrameBlock setStructureFrame(StructureFrameBlock structureFrame, ResourceLocation id) {
        Preconditions.checkState(this.structureFrame == null, "Frame block has already been registered for tier %s".formatted(this.toString()));
        this.structureFrame = Pair.of(structureFrame, id);
        return structureFrame;
    }

    public ResourceLocation getFrameName() {
        return this.structureFrame.getSecond();
    }

    public ResourceLocation getFrameName(String prefix) {
        return this.getFrameName().withPrefix(prefix);
    }
}
