package com.khanhtypo.etlegacy;

import com.khanhtypo.etlegacy.common.blockentities.ILoadAndUnloadListener;
import com.khanhtypo.etlegacy.common.blockentities.IMBFormChecker;
import com.khanhtypo.etlegacy.common.blockentities.IMBPart;
import com.khanhtypo.etlegacy.data.TextConstants;
import com.khanhtypo.etlegacy.multiblockchecker.BlockPlacerQueue;
import com.khanhtypo.etlegacy.multiblockchecker.MultiblockInstances;
import com.khanhtypo.etlegacy.registration.ModBlockEntities;
import com.khanhtypo.etlegacy.registration.ModBlocks;
import com.khanhtypo.etlegacy.registration.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Objects;

public class ETLegacy implements ModInitializer {
    public static final String MOD_ID = "environmentaltechlegacy";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        TextConstants.staticInit();
        ModItems.staticInit();
        ModBlocks.staticInit();
        MultiblockInstances.constructCheckers();
        ModBlockEntities.staticInit();
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            ETLegacy.LOGGER.info("Server {} started !!!", server.isDedicatedServer() ? "Dedicated" : "Integrated Server");
            server.addTickable(BlockPlacerQueue.INSTANCE);
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            ETLegacy.LOGGER.info("Server {} stopping !!!", server.isDedicatedServer() ? "Dedicated" : "Integrated Server");
            BlockPlacerQueue.INSTANCE.onServerClose();
        });
        ServerBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register((blockEntity, world) -> {
            if (blockEntity instanceof ILoadAndUnloadListener listener) {
                listener.onUnloading();
            }
        });
        ServerChunkEvents.CHUNK_LOAD.register((world, chunk) -> {
            Collection<BlockEntity> blockEntitiesInChunk = chunk.getBlockEntities().values();
            if (!blockEntitiesInChunk.isEmpty()) {
                blockEntitiesInChunk.stream()
                        .map(blockEntity -> blockEntity instanceof ILoadAndUnloadListener part ? part : null)
                        .filter(Objects::nonNull)
                        .forEach(part -> part.onLoad(world));
            }
        });
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (blockEntity instanceof IMBFormChecker part) {
                if (part.isAlreadyFormed()) {
                    part.onDeformed(world);
                }
            }
            return true;
        });
    }
}