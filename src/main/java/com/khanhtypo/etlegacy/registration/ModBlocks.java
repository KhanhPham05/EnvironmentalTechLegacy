package com.khanhtypo.etlegacy.registration;

import com.google.common.collect.Sets;
import com.khanhtypo.etlegacy.ETLegacy;
import com.khanhtypo.etlegacy.common.ItemGroupCollector;
import com.khanhtypo.etlegacy.common.block.DynamicShapeBlock;
import com.khanhtypo.etlegacy.common.block.multiblock.MultiblockPartBlock;
import com.khanhtypo.etlegacy.common.block.multiblock.StructureFrameBlock;
import com.khanhtypo.etlegacy.common.block.solargen.SolarCellBlock;
import com.khanhtypo.etlegacy.common.block.solargen.SolarGeneratorControllerBlock;
import com.khanhtypo.etlegacy.multiblockchecker.MultiblockInstances;
import com.khanhtypo.etlegacy.registering.Register;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.Set;
import java.util.stream.Stream;

import static com.khanhtypo.etlegacy.util.Tiers.*;

public class ModBlocks {
    public static final Set<Block> MULTIBLOCK_PARTS = Sets.newLinkedHashSet();
    public static final Register<Block> BLOCK_REGISTER = new Register<>(BuiltInRegistries.BLOCK);
    public static final ItemGroupCollector BLOCKS_COLLECTOR = new ItemGroupCollector();
    public static final Holder.Reference<CreativeModeTab> BLOCKS_TAB;
    public static final Block AETHIUM_BLOCK;
    public static final Block AETHIUM_BRICKS;
    public static final Block AETHIUM_PAVER;
    public static final Block AETHIUM_BRICK_SLAB;
    public static final Block AETHIUM_BRICK_STAIRS;
    public static final StructureFrameBlock STRUCTURE_FRAME_LITHERITE;
    public static final StructureFrameBlock STRUCTURE_FRAME_ERODIUM;
    public static final StructureFrameBlock STRUCTURE_FRAME_KYRONITE;
    public static final StructureFrameBlock STRUCTURE_FRAME_PLADIUM;
    public static final StructureFrameBlock STRUCTURE_FRAME_IONITE;
    public static final StructureFrameBlock STRUCTURE_FRAME_AETHIUM;
    public static final SolarCellBlock LITHERITE_SOLAR_CELL;
    public static final SolarCellBlock ERODIUM_SOLAR_CELL;
    public static final SolarCellBlock KYRONITE_SOLAR_CELL;
    public static final SolarCellBlock PLADIUM_SOLAR_CELL;
    public static final SolarCellBlock IONITE_SOLAR_CELL;
    public static final SolarCellBlock AETHIUM_SOLAR_CELL;
    public static final Block NULL_MODIFIER;
    //MB CONTROLLERS
    public static final Block NANOBOT_BEACON_CONTROLLER;
    public static final Block LIGHTNING_CONTROLLER;
    public static final SolarGeneratorControllerBlock SOLAR_ARRAY_CONTROLLER_LITHERITE;

    @SuppressWarnings("unchecked")
    public static <T extends Block> T register(String name, T block) {
        return (T) register(name, block, new Item.Properties());
    }

    private static Block register(String name, Block block, Item.Properties itemProperties) {
        Holder.Reference<Block> registeredObject = BLOCK_REGISTER.registerForHolder(name, block);
        BLOCKS_COLLECTOR.add(registeredObject);
        ModItems.register(name, new BlockItem(block, itemProperties));
        return block;
    }

    public static void staticInit() {
    }

    static {
        STRUCTURE_FRAME_LITHERITE = StructureFrameBlock.register(LITHERITE);
        STRUCTURE_FRAME_ERODIUM = StructureFrameBlock.register(ERODIUM);
        STRUCTURE_FRAME_KYRONITE = StructureFrameBlock.register(KYRONITE);
        STRUCTURE_FRAME_PLADIUM = StructureFrameBlock.register(PLADIUM);
        STRUCTURE_FRAME_IONITE = StructureFrameBlock.register(IONITE);
        STRUCTURE_FRAME_AETHIUM = StructureFrameBlock.register(AETHIUM);
        NULL_MODIFIER = register("null_modifier", new MultiblockPartBlock(BlockBehaviour.Properties.copy(STRUCTURE_FRAME_LITHERITE)));

        LITHERITE_SOLAR_CELL = register("litherite_solar_cell", new SolarCellBlock(LITHERITE, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).strength(2, 4).sound(SoundType.STONE)));
        ERODIUM_SOLAR_CELL = register("erodium_solar_cell", new SolarCellBlock(ERODIUM, BlockBehaviour.Properties.copy(LITHERITE_SOLAR_CELL)));
        KYRONITE_SOLAR_CELL = register("kyronite_solar_cell", new SolarCellBlock(KYRONITE, BlockBehaviour.Properties.copy(LITHERITE_SOLAR_CELL)));
        PLADIUM_SOLAR_CELL = register("pladium_solar_cell", new SolarCellBlock(PLADIUM, BlockBehaviour.Properties.copy(LITHERITE_SOLAR_CELL)));
        IONITE_SOLAR_CELL = register("ionite_solar_cell", new SolarCellBlock(IONITE, BlockBehaviour.Properties.copy(LITHERITE_SOLAR_CELL)));
        AETHIUM_SOLAR_CELL = register("aethium_solar_cell", new SolarCellBlock(AETHIUM, BlockBehaviour.Properties.copy(LITHERITE_SOLAR_CELL)));

        AETHIUM_BLOCK = register("aethium_block", new Block(BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(2, 4).requiresCorrectToolForDrops().mapColor(MapColor.COLOR_BLACK)));
        AETHIUM_BRICKS = register("aethium_bricks", new Block(BlockBehaviour.Properties.copy(AETHIUM_BLOCK)));
        AETHIUM_PAVER = register("aethium_paver", new Block(BlockBehaviour.Properties.copy(AETHIUM_BLOCK)));
        AETHIUM_BRICK_SLAB = register("aethium_brick_slab", new SlabBlock(BlockBehaviour.Properties.copy(AETHIUM_BLOCK)));
        AETHIUM_BRICK_STAIRS = register("aethium_brick_stairs", new SlabBlock(BlockBehaviour.Properties.copy(AETHIUM_BLOCK)));

        LIGHTNING_CONTROLLER = register("lightning_controller", new DynamicShapeBlock(
                BlockBehaviour.Properties.of().requiresCorrectToolForDrops().sound(SoundType.STONE).mapColor(MapColor.COLOR_BLACK).sound(SoundType.STONE).strength(2, 4),
                Stream.of(
                        Block.box(0, 12, 0, 16, 16, 16),
                        Block.box(0, 0, 0, 1, 12, 1),
                        Block.box(0, 0, 15, 1, 12, 16),
                        Block.box(15, 0, 0, 16, 12, 1),
                        Block.box(15, 0, 15, 16, 12, 16),
                        Block.box(1, 0, 0, 15, 1, 1),
                        Block.box(1, 0, 15, 15, 1, 16),
                        Block.box(0, 0, 1, 1, 1, 15),
                        Block.box(15, 0, 1, 16, 1, 15),
                        Block.box(2, 6, 2, 14, 12, 14),
                        Block.box(5, 1, 4, 11, 6, 5),
                        Block.box(5, 1, 11, 11, 6, 12),
                        Block.box(11, 1, 5, 12, 6, 11),
                        Block.box(4, 1, 5, 5, 6, 11),
                        Block.box(5, 0, 5, 11, 1, 11)
                )
        ), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
        NANOBOT_BEACON_CONTROLLER = register("nanobot_beacon_controller", new DynamicShapeBlock(
                BlockBehaviour.Properties.of().requiresCorrectToolForDrops().mapColor(MapColor.COLOR_BLACK).sound(SoundType.STONE).strength(2, 4),
                Stream.of(
                        Block.box(0, 4, 0, 1, 16, 1),
                        Block.box(0, 4, 15, 1, 16, 16),
                        Block.box(15, 4, 15, 16, 16, 16),
                        Block.box(15, 4, 0, 16, 16, 1),
                        Block.box(1, 15, 0, 15, 16, 1),
                        Block.box(1, 15, 15, 15, 16, 16),
                        Block.box(0, 15, 1, 1, 16, 15),
                        Block.box(15, 15, 1, 16, 16, 15),
                        Block.box(0, 0, 0, 16, 4, 16),
                        Block.box(2, 4, 2, 14, 7, 14),
                        Block.box(2, 11, 2, 14, 14, 14),
                        Block.box(3, 7, 3, 13, 11, 13)
                )
        ), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
        BLOCKS_TAB = Registry.registerForHolder(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                ModItems.id("all_blocks"),
                FabricItemGroup.builder()
                        .icon(() -> NANOBOT_BEACON_CONTROLLER.asItem().getDefaultInstance())
                        .title(Component.translatable("itemGroup.%s.all_blocks".formatted(ETLegacy.MOD_ID)))
                        .displayItems(BLOCKS_COLLECTOR)
                        .build()
        );
        SOLAR_ARRAY_CONTROLLER_LITHERITE = register("solar_array_controller_litherite", new SolarGeneratorControllerBlock(MultiblockInstances.SOLAR_ARRAY_LITHERITE));
    }
}
