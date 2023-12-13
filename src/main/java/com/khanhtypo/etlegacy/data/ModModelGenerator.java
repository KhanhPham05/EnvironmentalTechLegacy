package com.khanhtypo.etlegacy.data;

import com.khanhtypo.etlegacy.common.block.solargen.SolarCellBlock;
import com.khanhtypo.etlegacy.common.block.solargen.SolarGeneratorControllerBlock;
import com.khanhtypo.etlegacy.registration.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

import static com.khanhtypo.etlegacy.registration.ModBlocks.*;
import static com.khanhtypo.etlegacy.registration.ModItems.*;

public class ModModelGenerator extends FabricModelProvider {
    private static final TextureSlot FRAME_SLOT = TextureSlot.create("frame");
    private static final ModelTemplate SOLAR_CELL_TEMPLATE = new ModelTemplate(Optional.of(ModItems.id("block/templates/solar_cell")), Optional.empty(), TextureSlot.BOTTOM, TextureSlot.TOP);
    private static final ModelTemplate SOLAR_ARRAY_TEMPLATE = new ModelTemplate(Optional.of(ModItems.id("block/templates/controller_solar")), Optional.empty(), FRAME_SLOT);
    private ItemModelGenerators itemModelGenerator;
    private BlockModelGenerators blockModelGenerators;

    public ModModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        this.blockModelGenerators = blockStateModelGenerator;
        this.createSimpleBlockWithItem(STRUCTURE_FRAME_LITHERITE);
        this.createSimpleBlockWithItem(STRUCTURE_FRAME_ERODIUM);
        this.createSimpleBlockWithItem(STRUCTURE_FRAME_KYRONITE);
        this.createSimpleBlockWithItem(STRUCTURE_FRAME_PLADIUM);
        this.createSimpleBlockWithItem(STRUCTURE_FRAME_IONITE);
        this.createSimpleBlockWithItem(STRUCTURE_FRAME_AETHIUM);
        this.createSimpleBlockWithItem(NULL_MODIFIER);
        this.createSolarCellModel(LITHERITE_SOLAR_CELL);
        this.createSolarCellModel(ERODIUM_SOLAR_CELL);
        this.createSolarCellModel(PLADIUM_SOLAR_CELL);
        this.createSolarCellModel(KYRONITE_SOLAR_CELL);
        this.createSolarCellModel(IONITE_SOLAR_CELL);
        this.createSolarCellModel(AETHIUM_SOLAR_CELL);
        this.createSolarArrayControllerModel(SOLAR_ARRAY_CONTROLLER_LITHERITE);
    }

    private void createSolarArrayControllerModel(SolarGeneratorControllerBlock block) {
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
        this.outputState(block,
                SOLAR_ARRAY_TEMPLATE.create(
                        block,
                        new TextureMapping().put(FRAME_SLOT, ModItems.id("block/solar_frames/" + blockId.getPath().replace("solar_array_controller_", ""))),
                        this.blockModelGenerators.modelOutput)
        );

    }

    private void createSolarCellModel(SolarCellBlock solarCellBlock) {
        ResourceLocation blockId = this.getBlockId(solarCellBlock);
        ResourceLocation modelLoc = blockId.withPrefix("block/");
        this.outputState(solarCellBlock, SOLAR_CELL_TEMPLATE.create(solarCellBlock,
                new TextureMapping()
                        .put(TextureSlot.BOTTOM, solarCellBlock.getTier().getFrameName("block/"))
                        .put(TextureSlot.TOP, modelLoc.withSuffix("_top"))
                , this.blockModelGenerators.modelOutput));
    }

    private void outputState(Block block, ResourceLocation modeLocation) {
        this.blockModelGenerators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, modeLocation)));
    }

    private int getTier(String tierName) {
        return switch (tierName) {
            case "litherite" -> 1;
            case "erodium" -> 2;
            case "kyronite" -> 3;
            case "pladium" -> 4;
            case "ionite" -> 5;
            case "aethium" -> 6;
            default -> throw new IllegalStateException("Tier not valid " + tierName);
        };
    }

    private ResourceLocation getBlockId(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    private void createSimpleBlockWithItem(Block block) {
        this.blockModelGenerators.createTrivialCube(block);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        this.itemModelGenerator = itemModelGenerator;
        this.itemModelGenerator.generateFlatItem(MULTIBLOCK_ASSEMBLER, ModelTemplates.FLAT_HANDHELD_ITEM);
        this.simpleItem(AETHIUM_CRYSTAL);
        this.simpleItem(DIODE);
        this.simpleItem(CONNECTOR);
        this.simpleItem(IONITE_CRYSTAL);
        this.simpleItem(ERODIUM_CRYSTAL);
        this.simpleItem(KYRONITE_CRYSTAL);
        this.simpleItem(LONSDALEITE_CRYSTAL);
        this.simpleItem(LITHERITE_CRYSTAL);
        this.simpleItem(PHOTOVOLTAIC_CELL);
        this.simpleItem(PLADIUM_CRYSTAL);
    }

    private void simpleItem(Item item) {
        this.itemModelGenerator.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
    }
}
