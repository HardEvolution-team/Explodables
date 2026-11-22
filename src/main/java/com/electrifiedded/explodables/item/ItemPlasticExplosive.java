package com.electrifiedded.explodables.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/** Plastic explosive burns in furnaces (the 1.7 IFuelHandler). */
public class ItemPlasticExplosive extends ItemBlock {
    public ItemPlasticExplosive(Block block) {
        super(block);
    }

    @Override
    public int getItemBurnTime(ItemStack stack) {
        return 800;
    }
}
