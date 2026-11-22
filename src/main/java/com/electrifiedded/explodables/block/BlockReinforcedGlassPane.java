package com.electrifiedded.explodables.block;

import net.minecraft.block.BlockPane;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockReinforcedGlassPane extends BlockPane {
    public BlockReinforcedGlassPane() {
        super(Material.GLASS, false);
        setHardness(3.0F);
        setResistance(2000.0F);
        setSoundType(SoundType.GLASS);
    }
}
