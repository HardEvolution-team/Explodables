package com.electrifiedded.explodables.block;

import net.minecraft.block.BlockButtonStone;
import net.minecraft.block.SoundType;
import net.minecraft.world.World;

/** Blast-proof button that stays pressed for 3 seconds. */
public class BlockButtonObsidian extends BlockButtonStone {
    public BlockButtonObsidian() {
        setHardness(10.0F);
        setResistance(2000.0F);
        setSoundType(SoundType.STONE);
    }

    @Override
    public int tickRate(World world) {
        return 60;
    }
}
