package com.alcatrazescapee.randomchunks.mixin;

import com.alcatrazescapee.randomchunks.RandomChunks;
import net.minecraft.core.QuartPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkAccess.class)
public abstract class ChunkAccessMixin
{
    @Shadow public abstract ChunkPos getPos();
    @Shadow public abstract LevelHeightAccessor getHeightAccessorForGeneration();
    @Shadow public abstract LevelChunkSection getSection(int pIndex);

    @Inject(method = "fillBiomesFromNoise", at = @At("HEAD"), cancellable = true)
    void fillBiomesFromNoiseWithChunkOffset(BiomeResolver biomeResolver, Climate.Sampler sampler, CallbackInfo ci)
    {
        final ChunkPos chunkPos = RandomChunks.randomize(getPos());
        final int quartX = QuartPos.fromBlock(chunkPos.getMinBlockX());
        final int quartZ = QuartPos.fromBlock(chunkPos.getMinBlockZ());
        final LevelHeightAccessor level = getHeightAccessorForGeneration();

        for (int isection = level.getMinSection(); isection < level.getMaxSection(); ++isection)
        {
            getSection(((LevelHeightAccessor) this).getSectionIndexFromSectionY(isection))
                .fillBiomesFromNoise(biomeResolver, sampler, quartX, QuartPos.fromSection(isection), quartZ);
        }

        ci.cancel();
    }
}
