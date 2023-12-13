package com.khanhtypo.etlegacy.registration;

import com.khanhtypo.etlegacy.common.blockentities.impl.MultiblockPartBlockEntity;
import com.khanhtypo.etlegacy.common.blockentities.impl.SolarArrayControllerBlockEntity;
import com.khanhtypo.etlegacy.multiblockchecker.type.MultiblockType;
import com.khanhtypo.etlegacy.registering.Register;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final Register<BlockEntityType<?>> REGISTER = new Register<>(BuiltInRegistries.BLOCK_ENTITY_TYPE);
    public static final BlockEntityType<MultiblockPartBlockEntity> MULTIBLOCK_PARTS = REGISTER.register("multiblock_part", BlockEntityType.Builder.of(MultiblockPartBlockEntity::new, ModBlocks.MULTIBLOCK_PARTS.toArray(Block[]::new)).build(null));
    public static final BlockEntityType<SolarArrayControllerBlockEntity> SOLAR_ARRAY_CONTROLLER = REGISTER.register("solar_array_controller", new BlockEntityType<>(SolarArrayControllerBlockEntity::new, MultiblockType.SOLAR_ARRAY.getBlocksForType(), null));

    public static void staticInit() {
    }

}
