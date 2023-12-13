package com.khanhtypo.etlegacy.common.blockentities;

import net.minecraft.server.level.ServerLevel;

public interface ILoadAndUnloadListener {
    void onUnloading();

    void onLoad(ServerLevel world);
}
