package com.khanhtypo.etlegacy.multiblockchecker;

import com.khanhtypo.etlegacy.ETLegacy;
import jakarta.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BlockPlacerQueue implements Runnable {
    public static final BlockPlacerQueue INSTANCE = new BlockPlacerQueue();
    private static final List<Entry> blockPlaceQueue = new LinkedList<>();
    private static final Map<Entry, LastEntryMarker> lastCheckerEntryMap = new HashMap<>();
    private static @Nullable LastEntryMarker markerNextTick = null;

    private BlockPlacerQueue() {
    }

    @Override
    public void run() {
        this.processQueue(0);
        if (markerNextTick != null) {
            markerNextTick.checker().formStructure(
                    markerNextTick.user(),
                    markerNextTick.level(),
                    markerNextTick.controllerPos(),
                    markerNextTick.repeatCollisionCheck()
            );
            markerNextTick = null;
        }
    }

    private void processQueue(int startingIndex) {
        if (!blockPlaceQueue.isEmpty()) {
            int queueSize = blockPlaceQueue.size();
            for (int i = startingIndex; i < queueSize; i++) {
                //perform get entry
                Entry entry = blockPlaceQueue.get(i);

                Level currentLevel = entry.level();
                if (currentLevel.isClientSide() || !currentLevel.isLoaded(entry.blockPos()))
                    this.removeAndRerunAfter(i);

                //Overthinking moment, check if the level/dimension from player and server side is matching (or check if the dimension is present in server)
                //if (this.serverCurrentLevels.containsValue(((ServerLevel) currentLevel))) {
                //    this.removeAndRerunAfter(i);
                //}

                //For server, if the queue requested player leaves the game then don't do anything
                if (!currentLevel.players().contains(entry.player())) {
                    this.removeAndRerunAfter(i);
                }

                //perform place
                BlockPos position = entry.blockPos();
                if (!currentLevel.isLoaded(position)) continue;
                BlockState oldState = currentLevel.getBlockState(position);

                if (oldState.getBlock().defaultDestroyTime() < 0f) {
                    ETLegacy.LOGGER.warn("Can not replace a unbreakable block. Trying to replace block {} at {}", BuiltInRegistries.BLOCK.getKey(oldState.getBlock()), position.toShortString());
                    return;
                }

                //In case the queued block has already placed by another player then skip and remove the queue entry and rerun the method with the next entry slot
                BlockState toPlace = entry.state();
                if (oldState.is(toPlace.getBlock())) {
                    this.removeAndRerunAfter(i);
                }

                this.placeAt(currentLevel, position, toPlace, oldState);
                Player player = entry.player();
                if (!player.isCreative()) {
                    player.getInventory().removeItem(new ItemStack(toPlace.getBlock(), 1));
                }

                //remove queue after place at
                this.removeQueue(i);
                return;
            }
        }
    }

    public void onServerClose() {
        blockPlaceQueue.clear();
        lastCheckerEntryMap.clear();
    }

    private void placeAt(Level currentLevel, BlockPos blockPos, BlockState newBlock, BlockState oldBlock) {
        if (newBlock.isAir()) {
            currentLevel.destroyBlock(blockPos, true);

            ETLegacy.LOGGER.info("Destroyed block {} at {}", BuiltInRegistries.BLOCK.getKey(oldBlock.getBlock()), blockPos.toShortString());

        } else if (oldBlock.isAir()) {
            currentLevel.setBlockAndUpdate(blockPos, newBlock);
            currentLevel.playSound(null, blockPos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1, 1);
            ETLegacy.LOGGER.info("Placed block {} at {}", BuiltInRegistries.BLOCK.getKey(newBlock.getBlock()), blockPos.toShortString());
        } else {
            currentLevel.destroyBlock(blockPos, true, null);
            currentLevel.setBlockAndUpdate(blockPos, newBlock);

            ETLegacy.LOGGER.info("Replaced block {} with {} at {}",
                    BuiltInRegistries.BLOCK.getKey(oldBlock.getBlock()),
                    BuiltInRegistries.BLOCK.getKey(newBlock.getBlock()),
                    blockPos.toShortString());
        }
    }

    private void removeAndRerunAfter(int index) {
        if (this.removeQueue(index)) {
            this.processQueue(index);
        }
    }

    //return true if after remove, the sub list (from index to the end) is still not empty
    private boolean removeQueue(int index) {
        if (blockPlaceQueue.isEmpty()) {
            return true;
        }

        Entry removed = blockPlaceQueue.remove(index);

        ETLegacy.LOGGER.info("Removed queue entry : " + removed);

        //check if the removed entry object is marked as the last block to be placed in a multiblock structure then perform structure check
        if (lastCheckerEntryMap.containsKey(removed)) {
            markerNextTick = lastCheckerEntryMap.remove(removed);
            //LastEntryMarker lastEntryMarker = lastCheckerEntryMap.get(removed);

        }

        return !blockPlaceQueue.isEmpty();
    }

    void markQueueLast(MultiblockChecker checker, Entry lastEntry, Level level, BlockPos controllerPos, @Nullable Player user, boolean repeatCollisionCheck) {
        lastCheckerEntryMap.put(lastEntry, new LastEntryMarker(checker, level, controllerPos, user, repeatCollisionCheck));
    }

    Entry addPlaceQueue(Level level, BlockPos position, BlockState state, Player player) {
        Entry entry = new Entry(level, position, state, player);
        blockPlaceQueue.add(entry);
        ETLegacy.LOGGER.info("Place Queue added for block {} at {}", BuiltInRegistries.BLOCK.getKey(state.getBlock()), position.toShortString());
        return entry;
    }

    void addBreakQueue(Level level, BlockPos breakPos, Player player) {
        blockPlaceQueue.add(new Entry(level, breakPos, Blocks.AIR.defaultBlockState(), player));
    }

    record LastEntryMarker(MultiblockChecker checker, Level level, BlockPos controllerPos,
                           @Nullable Player user, boolean repeatCollisionCheck) {
    }

    record Entry(Level level, BlockPos blockPos, BlockState state, Player player) {
        @Override
        public String toString() {
            return BuiltInRegistries.BLOCK.getKey(state.getBlock()) + " at " + blockPos.toShortString();
        }
    }
}
