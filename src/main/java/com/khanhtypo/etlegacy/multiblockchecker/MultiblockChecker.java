package com.khanhtypo.etlegacy.multiblockchecker;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.khanhtypo.etlegacy.ETLegacy;
import com.khanhtypo.etlegacy.common.block.IMultiblockControllerBlock;
import com.khanhtypo.etlegacy.common.blockentities.IMBController;
import com.khanhtypo.etlegacy.common.blockentities.impl.MultiblockPartBlockEntity;
import com.khanhtypo.etlegacy.data.TextConstants;
import com.mojang.datafixers.util.Pair;
import jakarta.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class MultiblockChecker {
    private int controllerLayerIndex;
    private final int size;
    private int height;
    private final List<Layer> multiblockLayers;
    private ResourceLocation checkerId;
    private final InventoryChecker inventoryChecker;

    MultiblockChecker(int size) {
        Preconditions.checkState(size % 2 == 1, "size %s must be an odd number.".formatted(size));
        this.size = size;
        this.multiblockLayers = new LinkedList<>();
        this.controllerLayerIndex = -1;
        this.height = -1;
        this.inventoryChecker = new InventoryChecker();
    }

    public MultiblockChecker addLayer(Consumer<Layer> layerConstructor) {
        Layer layer = new Layer(this.size);
        this.multiblockLayers.add(layer);
        layerConstructor.accept(layer);
        return this;
    }

    public void constructLayers(ResourceLocation checkerId) {
        this.checkerId = checkerId;
        this.height = this.multiblockLayers.size();
        for (int i = 0; i < this.height; i++) {
            if (this.multiblockLayers.get(i).construct(inventoryChecker)) {
                if (this.controllerLayerIndex == -1) {
                    this.controllerLayerIndex = i;
                } else {
                    throw new IllegalStateException("Multiblock checker already has a layer with controller at layer %s (checking at %s)".formatted(this.controllerLayerIndex, i));
                }
            }
        }
        Preconditions.checkState(this.controllerLayerIndex > -1, "Where is the controller layer ???");
        ETLegacy.LOGGER.info("");
    }

    public Component getName() {
        return Component.translatable(this.checkerId.toLanguageKey("multiblock"));
    }

    //The creative mode queue has already check for checkPartCollision
    public void formStructure(@Nullable Player user, Level level, BlockPos blockPos, boolean repeatCollisionCheck) {
        if (level.getBlockEntity(blockPos) instanceof IMBController controller) {
            if (this.checkStructure(user, level, blockPos, repeatCollisionCheck)) {
                controller.onFormed(collectPositionsWithOutAir(level, getTopTopLeftPos(blockPos), this.size, this.height), level);
            } else {
                controller.onControllerDeformed(level);
            }
        }
    }

    //if the collision check is done prior to this check then we don't have to re-check it, therefore a bit of the game performance
    //may be saved.
    private boolean checkStructure(@Nullable Player user, Level level, BlockPos controllerBlockPos, boolean doCollisionCheck) {
        if (doCollisionCheck && !this.checkPartCollision(user, level, controllerBlockPos)) {
            return false;
        }

        for (Map.Entry<Layer, BlockPos> layerBlockPosEntry : this.getLayerPosMap(controllerBlockPos).entrySet()) {
            @Nullable Component errorMessage = layerBlockPosEntry.getKey().checkLayer(level, layerBlockPosEntry.getValue());
            if (errorMessage != null) {
                this.sendUnsuccessfulMessage(user, errorMessage);
                return false;
            }
        }

        if (user != null)
            user.displayClientMessage(TextConstants.copyWithArgs(TextConstants.MULTIBLOCK_FORMED, Component.translatable(this.checkerId.toLanguageKey("multiblock"))), false);
        return true;
    }

    //call during : BEFORE structureCheck and BEFORE auto-place.
    //check if the upcoming block position to be auto-placed belongs to a pre-formed multiblock
    //this method exists to prevent a multiblock part is used by >= 2 multiblock.
    public boolean checkPartCollision(@Nullable Player user, Level level, BlockPos controllerPos) {
        Iterator<BlockPos> positions = this.collectStrictPositions(controllerPos);
        while (positions.hasNext()) {
            BlockPos pos = positions.next();
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof MultiblockPartBlockEntity partBlockEntity) {
                if (partBlockEntity.isAlreadyFormed()) {
                    this.sendUnsuccessfulMessage(user, TextConstants.copyWithArgs(TextConstants.PART_COLLISION, pos.toShortString(), partBlockEntity.getBlockState().getBlock().getName()));
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkInventory(Player user, Level level, BlockPos controllerBlockPos) {
        Map<Block, Integer> excludeMap = null;
        BlockPos topTopPos = this.getTopTopLeftPos(controllerBlockPos);
        for (Layer multiblockLayer : this.multiblockLayers) {
            Map<Block, Integer> excludeMapLayer = multiblockLayer.getAlreadyPlacedBlocks(level, topTopPos);
            if (excludeMapLayer != null && !excludeMapLayer.isEmpty()) {
                if (excludeMap != null) {
                    final Map<Block, Integer> finalExcludeMap = excludeMap;
                    excludeMapLayer.forEach((block, excludeAmount) -> finalExcludeMap.putAll(excludeMapLayer));
                } else excludeMap = excludeMapLayer;
            }
            topTopPos = topTopPos.below();
        }

        Optional<List<Component>> optionalComponents = this.inventoryChecker.checkInventory(user.getInventory(), excludeMap);

        if (optionalComponents.isPresent()) {
            List<Component> components = optionalComponents.get();
            this.sendUnsuccessfulMessage(user, components.get(0));
            for (int i = 1; i < components.size(); i++) {
                user.displayClientMessage(components.get(i), false);
            }
            return false;
        }

        return true;
    }

    private BlockPos getTopTopLeftPos(BlockPos controllerPos) {
        int shift = (this.size - 1) / 2;
        return controllerPos.relative(Direction.NORTH, shift).relative(Direction.WEST, shift).relative(Direction.UP, this.controllerLayerIndex);
    }

    private void sendUnsuccessfulMessage(@Nullable Player user, Component reason) {
        if (user == null) return;
        user.displayClientMessage(TextConstants.copyWithArgs(TextConstants.FAILED_TO_FORM_MULTIBLOCK, this.getComponent()), false);
        user.displayClientMessage(reason, false);
    }

    private Component getComponent() {
        return Component.translatable(this.checkerId.toLanguageKey("multiblock"));
    }

    //Collect all block positions that is not air
    //Similar to collectStrictPositions but this should be called when the structureCheck returns true.
    public static Set<BlockPos> collectPositionsWithOutAir(LevelReader level, BlockPos cornerPos, int size, int height) {
        return Util.make(new TreeSet<>(), set -> {
            for (BlockPos blockPos : collectPos(cornerPos, size, height)) {
                if (level.getBlockState(blockPos).isAir()) continue;
                set.add(new BlockPos(blockPos));
            }
        });
    }

    //Collect all block positions within the box if the multiblock
    private static Iterable<BlockPos> collectPos(BlockPos cornerPos, int size, int height) {
        BlockPos endPos = cornerPos.relative(Direction.EAST, size - 1).relative(Direction.SOUTH, size - 1);
        if (height > 1) {
            endPos = endPos.relative(Direction.DOWN, height - 1);
        }
        return BlockPos.betweenClosed(cornerPos, endPos);
    }

    //Collect all block positions of all layers that should be checked
    //This should be called during structure check.
    private Iterator<BlockPos> collectStrictPositions(BlockPos controllerPos) {
        return Util.make(Stream.<BlockPos>builder(), builder ->
                this.forEachLayer(controllerPos, (layer, pos) ->
                        layer.forEachPos(pos, (blockPos, b) ->
                                builder.add(blockPos)))
        ).build().iterator();
    }

    private void forEachLayer(BlockPos controllerPos, BiConsumer<Layer, BlockPos> layerAndCornerPosAction) {
        BlockPos topTopPos = this.getTopTopLeftPos(controllerPos);
        for (Layer multiblockLayer : this.multiblockLayers) {
            layerAndCornerPosAction.accept(multiblockLayer, topTopPos);
            topTopPos = topTopPos.below();
        }
    }

    private Map<Layer, BlockPos> getLayerPosMap(BlockPos controllerPos) {
        ImmutableMap.Builder<Layer, BlockPos> mapBuilder = new ImmutableMap.Builder<>();
        BlockPos topTopPos = this.getTopTopLeftPos(controllerPos);
        for (Layer multiblockLayer : this.multiblockLayers) {
            mapBuilder.put(multiblockLayer, topTopPos);
            topTopPos = topTopPos.below();
        }

        return mapBuilder.build();
    }

    public void addAllToQueue(Level level, BlockPos controllerPos, Player user) {
        if (this.checkPartCollision(user, level, controllerPos)) {
            AtomicReference<BlockPlacerQueue.Entry> entries = new AtomicReference<>();
            this.forEachLayer(controllerPos, (layer, pos) -> entries.set(layer.addLayerToQueue(level, pos, user)));
            BlockPlacerQueue.INSTANCE.markQueueLast(this, entries.get(), level, controllerPos, user, false);
        }
    }


    public final static class Layer {
        private boolean locked;
        private final int size;
        private final Set<List<String>> charLayers = new HashSet<>();
        private final Map<Character, Block> blockMapping = new HashMap<>();
        private final NonNullList<Block> finalLayerMap;

        private Layer(int size) {
            this.size = size;
            this.locked = false;
            this.finalLayerMap = NonNullList.withSize((int) Math.pow(this.size, 2), Blocks.AIR);
        }

        public int getSize() {
            return size;
        }

        public Layer addLayer(String... layerChars) {
            if (this.locked) return this;
            Preconditions.checkState(layerChars.length == size, "Layers must has the same size as multiblock (%s expected, found %s)".formatted(size, layerChars.length));
            for (int i = 0; i < layerChars.length; i++) {
                Preconditions.checkState(layerChars[i].length() == size, "Layer %s must have the same size as multiblock's size (%s expected, %s found)".formatted(i, size, layerChars[i].length()));
            }
            this.charLayers.add(List.of(layerChars));
            return this;
        }

        //return null if layer is okay (no error message)
        @Nullable
        private Component checkLayer(Level level, BlockPos cornerPos) {
            @Nullable Component errorMessage = this.computeEachPos(cornerPos, (blockPos, block) -> {
                if (!level.getBlockState(blockPos).is(block)) {
                    return TextConstants.copyWithArgs(TextConstants.BLOCK_INVALID, blockPos.toShortString(), block.getName());
                }
                return null;
            });
            return errorMessage;
        }

        //return the last queue slot of the layer
        private BlockPlacerQueue.Entry addLayerToQueue(Level level, BlockPos cornerPos, Player user) {
            final Iterator<Pair<BlockState, BlockPos>> entrySet = this.getPositionPerBlock(cornerPos).iterator();
            BlockPlacerQueue.Entry placeEntry = null;
            while (entrySet.hasNext()) {
                var entry = entrySet.next();
                placeEntry = BlockPlacerQueue.INSTANCE.addPlaceQueue(level, entry.getSecond(), entry.getFirst(), user);
            }
            return placeEntry;
        }

        public Layer map(char c, Block block) {
            if (!this.locked)
                blockMapping.put(c, block);
            return this;
        }

        private int controllerIndex = -1;

        //return true if constructed layer has controller init
        public boolean construct(InventoryChecker inventoryChecker) {
            this.locked = true;
            int k = 0;
            for (Iterable<String> charLayer : this.charLayers) {
                for (String chars : charLayer) {
                    int charsLength = chars.length();
                    for (int i = 0; i < charsLength; i++) {
                        char c = chars.charAt(i);
                        if (c != ' ') {
                            Block block = this.blockMapping.get(c);
                            Preconditions.checkState(block != null, "Can not find block mapping for '%s'".formatted(c));
                            this.finalLayerMap.set(k, block);
                            if (block instanceof IMultiblockControllerBlock) {
                                if (this.controllerIndex > -1)
                                    throw new IllegalStateException("Layer already has a controller");
                                else this.controllerIndex = k;
                            } else {
                                inventoryChecker.addRequirement(block);
                            }
                        }
                        k++;
                    }
                }
            }
            return this.controllerIndex > -1;
        }

        @Nullable
        public Map<Block, Integer> getAlreadyPlacedBlocks(Level level, BlockPos cornerPos) {
            Map<Block, Integer> layerExcludeMap = null;
            for (Map.Entry<BlockPos, Block> blockPosBlockEntry : this.collectEachPos(cornerPos).entrySet()) {
                BlockPos blockPos = blockPosBlockEntry.getKey();
                Block block /*toCheck*/ = blockPosBlockEntry.getValue();
                if (level.getBlockState(blockPos).is(block)) {
                    if (layerExcludeMap != null) {
                        layerExcludeMap.compute(block, (b, excludeAmount) -> excludeAmount == null ? 1 : excludeAmount + 1);
                    } else {
                        layerExcludeMap = Util.make(new HashMap<>(), map -> map.put(block, 1));
                    }
                }
            }
            return layerExcludeMap;
        }

        private Set<Pair<BlockState, BlockPos>> getPositionPerBlock(BlockPos layerCornerPos) {
            return Util.make(new HashSet<>(), set -> {
                this.forEachPos(layerCornerPos, (pos, block) -> {
                    if (block instanceof AirBlock || block instanceof IMultiblockControllerBlock) return;
                    set.add(Pair.of(block.defaultBlockState(), pos));
                });
            });
        }

        //BiConsumer takes COLLECTED blockPos and Block from finalLayerMap
        private void forEachPos(BlockPos layerCornerPos, BiConsumer<BlockPos, Block> action) {
            this.computeEachPos(layerCornerPos, (blockPos, block) -> {
                action.accept(blockPos, block);
                return null;
            });
        }

        /**
         * @return null if throughout the whole iteration, action returns null, otherwise, return the non-null value and stop the iteration process immediately.
         */
        @Nullable
        private <T> T computeEachPos(BlockPos layerCornerPos, BiFunction<BlockPos, Block, T> action) {
            int i = 0;
            for (BlockPos blockPosition : collectPos(layerCornerPos, this.size, 0)) {
                Block block = this.finalLayerMap.get(i);
                i++;
                if (block instanceof AirBlock) continue;
                @Nullable T applied = action.apply(new BlockPos(blockPosition), block);
                if (applied != null) {
                    return applied;
                }
            }
            return null;
        }

        private Map<BlockPos, Block> collectEachPos(BlockPos layerCornerPos) {
            return Util.make(new ImmutableMap.Builder<BlockPos, Block>(), map -> {
                this.forEachPos(layerCornerPos, map::put);
            }).build();
        }
    }
}