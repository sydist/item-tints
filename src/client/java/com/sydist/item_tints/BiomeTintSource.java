package com.sydist.item_tints;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

public record BiomeTintSource(BiomeTintMode mode) implements TintSource {
    public static final MapCodec<BiomeTintSource> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    StringIdentifiable.createCodec(BiomeTintMode::values)
                            .fieldOf("mode")
                            .forGetter(BiomeTintSource::mode))
                    .apply(instance, BiomeTintSource::new));

    public BiomeTintSource(BiomeTintMode mode) {
        this.mode = mode;
    }

    @Override
    public int getTint(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user) {
        if (world == null) {
            return -1;
        }

        Entity owner = user != null ? user : stack.getHolder();
        if (owner == null) {
            return -1;
        }

        return this.getBiomeTint(mode, world, owner.getBlockPos()) | 0xFF000000;
    }

    @Override
    public MapCodec<BiomeTintSource> getCodec() {
        return CODEC;
    }

    private int getBiomeTint(BiomeTintMode mode, BlockRenderView world, BlockPos pos) {
        switch (mode) {
            case GRASS:
                return BiomeColors.getGrassColor(world, pos);
            case FOLIAGE:
                return BiomeColors.getFoliageColor(world, pos);
            case DRY_FOLIAGE:
                return BiomeColors.getDryFoliageColor(world, pos);
            case BUCKET:
                return BiomeColors.getWaterColor(world, pos);
            case SUGAR_CANE:
                final int SUGAR_CANE_BLOCK_COLOR = 0x7fc232;
                return overlayBlend(BiomeColors.getGrassColor(world, pos), SUGAR_CANE_BLOCK_COLOR);
            default:
                throw new IllegalArgumentException("Invalid biome tint mode: " + mode);
        }
    }

    private static int overlayBlend(int baseColor, int blendColor) {
        int baseR = (baseColor >> 16) & 0xFF;
        int baseG = (baseColor >> 8) & 0xFF;
        int baseB = baseColor & 0xFF;

        int blendR = (blendColor >> 16) & 0xFF;
        int blendG = (blendColor >> 8) & 0xFF;
        int blendB = blendColor & 0xFF;

        int r = overlayChannel(baseR, blendR);
        int g = overlayChannel(baseG, blendG);
        int b = overlayChannel(baseB, blendB);

        return (r << 16) | (g << 8) | b;
    }

    private static int overlayChannel(int base, int blend) {
        float b = base / 255f;
        float s = blend / 255f;
        float result = (b < 0.5f) ? (2 * b * s) : (1 - 2 * (1 - b) * (1 - s));
        return clamp(Math.round(result * 255));
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
