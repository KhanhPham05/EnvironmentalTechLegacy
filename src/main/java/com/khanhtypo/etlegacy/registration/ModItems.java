package com.khanhtypo.etlegacy.registration;

import com.khanhtypo.etlegacy.ETLegacy;
import com.khanhtypo.etlegacy.common.ItemGroupCollector;
import com.khanhtypo.etlegacy.common.item.AssemblerItem;
import com.khanhtypo.etlegacy.registering.Register;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public final class ModItems {
    private static final Register<Item> ITEM_REGISTER = new Register<>(BuiltInRegistries.ITEM);
    private static final ItemGroupCollector ITEMS_TAB_ITEMS = new ItemGroupCollector();
    public static final Holder.Reference<CreativeModeTab> ITEMS_TAB;
    public static final Item MULTIBLOCK_ASSEMBLER;
    public static final Item AETHIUM_CRYSTAL = register("aethium_crystal");
    public static final Item DIODE = register("diode");
    public static final Item CONNECTOR = register("connector");
    public static final Item ERODIUM_CRYSTAL = register("erodium_crystal");
    public static final Item IONITE_CRYSTAL = register("ionite_crystal");
    public static final Item KYRONITE_CRYSTAL = register("kyronite_crystal");
    public static final Item LONSDALEITE_CRYSTAL = register("lonsdaleite_crystal");
    public static final Item LITHERITE_CRYSTAL = register("litherite_crystal");
    public static final Item PHOTOVOLTAIC_CELL = register("photovoltaic_cell");
    public static final Item PLADIUM_CRYSTAL = register("pladium_crystal");

    private ModItems() {
    }

    private static Item register(String name) {
        return register(name, new Item(new Item.Properties()));
    }

    static Item register(String name, Item item) {
        Holder.Reference<Item> itemR = ITEM_REGISTER.registerForHolder(name, item);
        if (!(item instanceof BlockItem)) ITEMS_TAB_ITEMS.add(itemR);
        return item;
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(ETLegacy.MOD_ID, path);
    }

    public static void staticInit() {
    }

    static {
        MULTIBLOCK_ASSEMBLER = register("multiblock_assembler", new AssemblerItem(new Item.Properties().stacksTo(1)));
        ITEMS_TAB = Registry.registerForHolder(BuiltInRegistries.CREATIVE_MODE_TAB,
                id("all_items"),
                FabricItemGroup.builder()
                        .icon(MULTIBLOCK_ASSEMBLER::getDefaultInstance)
                        .displayItems(ITEMS_TAB_ITEMS)
                        .title(Component.translatable("itemGroup.%s.tab".formatted(ETLegacy.MOD_ID)))
                        .build()
        );
    }
}
