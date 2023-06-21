package com.alcatrazescapee.randomchunks.mixin;

import com.alcatrazescapee.randomchunks.RandomChunks;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoiseChunk.class)
public abstract class NoiseChunkMixin
{
    @Inject(method = "forChunk", at = @At("HEAD"), cancellable = true)
    private static void replaceForChunk(ChunkAccess chunk, RandomState state, DensityFunctions.BeardifierOrMarker beardifierOrMarker, NoiseGeneratorSettings noiseGeneratorSettings, Aquifer.FluidPicker fluidPicker, Blender blender, CallbackInfoReturnable<NoiseChunk> cir)
    {
        final NoiseSettings noiseSettings = noiseGeneratorSettings.noiseSettings().clampToHeightAccessor(chunk);
        final int cellCountXZ = 16 / noiseSettings.getCellWidth();
        final ChunkPos origin = chunk.getPos();
        final ChunkPos pos = RandomChunks.randomize(origin);

        final NoiseChunk noiseChunk = new NoiseChunk(cellCountXZ, state, pos.getMinBlockX(), pos.getMinBlockZ(), noiseSettings, beardifierOrMarker, noiseGeneratorSettings, fluidPicker, blender);

        ((NoiseChunkAccessor) noiseChunk).setAquifer(new RandomChunks.OffsetAquifer(noiseChunk.aquifer(), pos.getMinBlockX() - origin.getMinBlockX(), pos.getMinBlockZ() - origin.getMinBlockZ()));

        cir.setReturnValue(noiseChunk);
    }
}
