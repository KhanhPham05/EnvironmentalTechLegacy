package com.khanhtypo.etlegacy.multiblockchecker;

import com.khanhtypo.etlegacy.registration.ModBlocks;
import com.khanhtypo.etlegacy.registration.ModItems;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

public class MultiblockInstances {
    private static final Map<ResourceLocation, Supplier<MultiblockChecker>> deferredMap = new TreeMap<>();
    public static final ResourceKey<Registry<MultiblockChecker>> CHECKER_REGISTRY_KEY = ResourceKey.createRegistryKey(ModItems.id("multiblock_checkers"));
    public static final MappedRegistry<MultiblockChecker> CHECKER_REGISTRY = FabricRegistryBuilder.createSimple(CHECKER_REGISTRY_KEY).buildAndRegister();
    public static final ResourceLocation SOLAR_ARRAY_LITHERITE = register("solar_array_litherite",
            () -> new MultiblockChecker(5)
                    .addLayer(layer -> createSolarArrayTopLayer(layer, ModBlocks.STRUCTURE_FRAME_LITHERITE, ModBlocks.LITHERITE_SOLAR_CELL))
                    .addLayer(layer -> layer.addLayer("     ", " S S ", "  C  ", " S S ", "     ")
                            //TODO Modifiers
                            .map('S', ModBlocks.NULL_MODIFIER).map('C', ModBlocks.SOLAR_ARRAY_CONTROLLER_LITHERITE))
    );

    private static void createSolarArrayTopLayer(MultiblockChecker.Layer layer, Block structureBlock, Block solarCellBlock) {
        int interiorSize = layer.getSize() - 2;
        String topLine = "A".repeat(layer.getSize());
        String interiorLine = "A" + "B".repeat(interiorSize) + "A";
        String[] layerString = new String[layer.getSize()];
        for (int i = 0; i < layer.getSize(); i++) {
            layerString[i] = (i == 0 || i == layer.getSize() - 1 ? topLine : interiorLine);
        }
        layer.addLayer(layerString).map('A', structureBlock).map('B', solarCellBlock);
    }

    private MultiblockInstances() {
    }

    private static ResourceLocation register(String name, Supplier<MultiblockChecker> checker) {
        ResourceLocation id = ModItems.id(name);
        deferredMap.put(id, () -> Registry.register(CHECKER_REGISTRY, id, checker.get()));
        return id;
    }

    public static void constructCheckers() {
        deferredMap.forEach((id, checker) -> checker.get().constructLayers(id));
        CHECKER_REGISTRY.freeze();
    }
}
