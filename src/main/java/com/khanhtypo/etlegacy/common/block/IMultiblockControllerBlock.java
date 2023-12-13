package com.khanhtypo.etlegacy.common.block;

import com.khanhtypo.etlegacy.multiblockchecker.MultiblockChecker;
import com.khanhtypo.etlegacy.multiblockchecker.MultiblockInstances;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public interface IMultiblockControllerBlock {

    ResourceLocation getCheckerName();

    default MultiblockChecker getChecker() {
        return Objects.requireNonNull(
                MultiblockInstances.CHECKER_REGISTRY.get(this.getCheckerName()),
                "Multiblock Checker %s does not exist".formatted(this.getCheckerName())
        );
    }
}
