package com.khanhtypo.etlegacy.data;

import com.khanhtypo.etlegacy.ETLegacy;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public final class TextConstants {
    private static final String FORMAT = "%s." + ETLegacy.MOD_ID + ".%s";
    static final Map<String, String> keyMap = new TreeMap<>();
    private static final Map<Component, String> componentMap = new HashMap<>();
    public static final MutableComponent ALREADY_FORMED = create("message", "already_formed", "Multiblock %s has already formed.");
    public static final MutableComponent PART_COLLISION = create("message", "part_collision", "Part collision at %s : Block %s has already belong to another multiblock.");
    public static final MutableComponent NOT_A_MB_PART = create("message", "not_a_mb_part", "Block at %s is not a MB Part ?!");
    public static final MutableComponent FAILED_TO_FORM_MULTIBLOCK = create("message", "multiblock_form_fail", "Failed to check Multiblock (%s).");
    public static final MutableComponent BLOCK_INVALID = create("message", "multiblock_part_invalid", "Invalid block at %s, it should be %s.");
    public static final MutableComponent BLOCK_MISSING = create("message", "missing_block", "Build Stopped, not enough %s to build.");
    public static final MutableComponent MULTIBLOCK_FORMED = create("message", "multiblock_success", "Multiblock %s is formed.");
    public static final MutableComponent INVENTORY_NOT_ENOUGH = create("message", "insufficient_items", "Insufficient items in inventory.");
    public static final MutableComponent NEED_X_BLOCKS = create("message", "needs_x_blocks", "• Needs %s more %s.");
    private static final Component MINUS_BORDERLINE = Component.literal("-".repeat(15));

    private static MutableComponent create(String prefix, String suffix, String translation) {
        String key = String.format(FORMAT, prefix, suffix);
        keyMap.put(key, translation);
        MutableComponent component = Component.translatable(key, translation);
        componentMap.put(component, key);
        return component;
    }

    public static MutableComponent copyWithArgs(MutableComponent component, Object... args) {
        if (componentMap.containsKey(component)) {
            return Component.translatable(componentMap.get(component), args);
        }

        ETLegacy.LOGGER.warn("Can not send component {}", component);
        return Component.empty();
    }

    static void runTranslation(FabricLanguageProvider.TranslationBuilder builder) {
        keyMap.forEach(builder::add);
    }

    public static void staticInit() {
    }

    public static Stream<Component> boxInBorder(Component... content) {
        return Util.make(
                Stream.<Component>builder().add(MINUS_BORDERLINE),
                builder -> Arrays.stream(content).forEach(builder::add)
        ).add(MINUS_BORDERLINE).build();
    }
}
