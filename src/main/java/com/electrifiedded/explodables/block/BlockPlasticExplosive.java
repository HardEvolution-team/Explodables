package com.electrifiedded.explodables.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockPlasticExplosive extends Block {
    public BlockPlasticExplosive() {
        super(Material.WOOD);
        setHardness(0.0F);
        setSoundType(SoundType.PLANT);
    }
}
