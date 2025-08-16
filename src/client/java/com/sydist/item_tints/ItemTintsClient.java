package com.sydist.item_tints;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.item.tint.TintSourceTypes;
import net.minecraft.util.Identifier;

public class ItemTintsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        TintSourceTypes.ID_MAPPER.put(Identifier.of(ItemTints.MOD_ID, "biome"), BiomeTintSource.CODEC);
    }
}
