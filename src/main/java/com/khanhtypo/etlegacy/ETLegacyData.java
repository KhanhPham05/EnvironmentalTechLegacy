package com.khanhtypo.etlegacy;

import com.khanhtypo.etlegacy.data.ModModelGenerator;
import com.khanhtypo.etlegacy.data.ModLanguageGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;

public class ETLegacyData implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ModModelGenerator::new);
		pack.addProvider(ModLanguageGenerator::new);
	}
}
