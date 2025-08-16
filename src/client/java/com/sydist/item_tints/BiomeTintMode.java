package com.sydist.item_tints;

import net.minecraft.util.StringIdentifiable;

public enum BiomeTintMode implements StringIdentifiable {
    GRASS("grass"),
    FOLIAGE("foliage"),
    DRY_FOLIAGE("dry_foliage"),
    BUCKET("bucket"),
    SUGAR_CANE("sugar_cane");

    private BiomeTintMode(String id) {
        this.id = id;
    }

    private final String id;

    @Override
    public String asString() {
        return this.id;
    }

}
