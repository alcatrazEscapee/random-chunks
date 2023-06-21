package com.alcatrazescapee.randomchunks;

import net.fabricmc.api.ModInitializer;

public class FabricRandomChunks implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        RandomChunks.init();
    }
}
