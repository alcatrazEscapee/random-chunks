package com.alcatrazescapee.randomchunks;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public final class RandomChunks
{
    public static final String MOD_ID = "randomchunks";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final int U = 203;
    private static final int V = 204;
    private static final int G = 229;
    private static final int P = U * V - 1;

    public static ChunkPos randomize(final ChunkPos pos)
    {
        final int gridX = Math.floorDiv(pos.x, U);
        final int gridZ = Math.floorDiv(pos.z, V);

        final int baseX = gridX * U;
        final int baseZ = gridZ * V;

        final int localX = pos.x - baseX;
        final int localZ = pos.z - baseZ;

        final int q = localX + U * localZ;
        if (q == P) return pos;

        final int qPrime = fastPower(G, q, P);

        final int localXPrime = qPrime % U;
        final int localZPrime = qPrime / U;

        return new ChunkPos(baseX + localXPrime, baseZ + localZPrime);
    }

    public static void init()
    {
        LOGGER.info("Let p = u*v - 1 be prime, for u, v ∈ Z, F*p the finite field mod p, with primitive root g");
        LOGGER.info("Define f : Z^2 → Z^2 by extension of f' : Fp* → Fp*, where (x, y) ⟷ xu + y, plus the single point (u, v) ⟷ (u, v). Then f' is given by f'(q) = g^q mod p");
        LOGGER.info("That is how random chunks, do.");
    }

    /**
     * Ref. "An Introduction to Mathematical Cryptography 2nd Ed, 1.3.2 The Fast Powering Algorithm, p25-26"
     */
    private static int fastPower(int base, int exp, final int modulo)
    {
        int value = 1;
        while (exp > 0)
        {
            if ((exp & 1) == 1)
            {
                value = (value * base) % modulo;
            }
            base = (base * base) % modulo;
            exp >>= 1;
        }
        return value;
    }

    public record OffsetAquifer(Aquifer aquifer, int offsetX, int offsetZ) implements Aquifer
    {
        @Nullable
        @Override
        public BlockState computeSubstance(final DensityFunction.FunctionContext context, final double value)
        {
            return aquifer.computeSubstance(new DensityFunction.SinglePointContext(context.blockX() + offsetX, context.blockY(), context.blockZ() + offsetZ), value);
        }

        @Override
        public boolean shouldScheduleFluidUpdate()
        {
            return aquifer.shouldScheduleFluidUpdate();
        }
    }
}
