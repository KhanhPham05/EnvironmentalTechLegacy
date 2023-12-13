package com.khanhtypo.etlegacy.data;

import com.khanhtypo.etlegacy.multiblockchecker.MultiblockInstances;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.resources.ResourceLocation;

import static com.khanhtypo.etlegacy.registration.ModBlocks.*;
import static com.khanhtypo.etlegacy.registration.ModItems.*;

public class ModLanguageGenerator extends FabricLanguageProvider {
    public ModLanguageGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        this.itemTranslations(translationBuilder);
        this.blockTranslations(translationBuilder);
        TextConstants.runTranslation(translationBuilder);
        this.multiblockTranslation(translationBuilder);
    }

    private void add(TranslationBuilder builder, ResourceLocation checkerSupplier, String translation) {
        builder.add(checkerSupplier.toLanguageKey("multiblock"), translation);
    }

    private void multiblockTranslation(TranslationBuilder translationBuilder) {
        this.add(translationBuilder, MultiblockInstances.SOLAR_ARRAY_LITHERITE, "Litherite Solar Array");

    }

    private void itemTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(ITEMS_TAB.key(), "ET Legacy - Items");
        translationBuilder.add(AETHIUM_CRYSTAL, "Aethium Crystal");
        translationBuilder.add(CONNECTOR, "Connector");
        translationBuilder.add(DIODE, "Diode");
        translationBuilder.add(ERODIUM_CRYSTAL, "Erodium Crystal");
        translationBuilder.add(IONITE_CRYSTAL, "Ionite Crystal");
        translationBuilder.add(KYRONITE_CRYSTAL, "Kyronite Crystal");
        translationBuilder.add(LITHERITE_CRYSTAL, "Litherite Crystal");
        translationBuilder.add(LONSDALEITE_CRYSTAL, "Lonsdaleite Crystal");
        translationBuilder.add(MULTIBLOCK_ASSEMBLER, "Multiblock Assembler");
        translationBuilder.add(PHOTOVOLTAIC_CELL, "Photovoltaic Cell");
        translationBuilder.add(PLADIUM_CRYSTAL, "Pladium Assembler");
    }

    private void blockTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(BLOCKS_TAB.key(), "ET Legacy - Blocks");
        translationBuilder.add(AETHIUM_BLOCK, "Aethium");
        translationBuilder.add(AETHIUM_BRICKS, "Aethium Bricks");
        translationBuilder.add(AETHIUM_PAVER, "Aethium Paver");
        translationBuilder.add(AETHIUM_BRICK_SLAB, "Aethium Brick Slab");
        translationBuilder.add(AETHIUM_BRICK_STAIRS, "Aethium Brick Stairs");
        translationBuilder.add(STRUCTURE_FRAME_LITHERITE, "Structure Frame Tier 1");
        translationBuilder.add(STRUCTURE_FRAME_ERODIUM, "Structure Frame Tier 2");
        translationBuilder.add(STRUCTURE_FRAME_KYRONITE, "Structure Frame Tier 3");
        translationBuilder.add(STRUCTURE_FRAME_PLADIUM, "Structure Frame Tier 4");
        translationBuilder.add(STRUCTURE_FRAME_IONITE, "Structure Frame Tier 5");
        translationBuilder.add(STRUCTURE_FRAME_AETHIUM, "Structure Frame Tier 6");
        translationBuilder.add(LITHERITE_SOLAR_CELL, "Litherite Solar Cell");
        translationBuilder.add(ERODIUM_SOLAR_CELL, "Erodium Solar Cell");
        translationBuilder.add(KYRONITE_SOLAR_CELL, "Kyronite Solar Cell");
        translationBuilder.add(PLADIUM_SOLAR_CELL, "Pladium Solar Cell");
        translationBuilder.add(IONITE_SOLAR_CELL, "Ionite Solar Cell");
        translationBuilder.add(AETHIUM_SOLAR_CELL, "Aethium Solar Cell");
        translationBuilder.add(NULL_MODIFIER, "Null Modifier");
        translationBuilder.add(NANOBOT_BEACON_CONTROLLER, "Nanobot Beacon Controller");
        translationBuilder.add(LIGHTNING_CONTROLLER, "Lightning Rod Controller");
        translationBuilder.add(SOLAR_ARRAY_CONTROLLER_LITHERITE, "Solar Array Controller Tier 1");
    }
}
