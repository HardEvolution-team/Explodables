package com.electrifiedded.explodables.block;

import com.electrifiedded.explodables.Explodables;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/** Redstone-operated blast-proof door; breaching charges force it open. */
public class BlockObsidianDoor extends BlockDoor {
    public BlockObsidianDoor() {
        super(Material.IRON);
        setHardness(10.0F);
        setResistance(2000.0F);
        setSoundType(SoundType.STONE);
        disableStats();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(HALF) == EnumDoorHalf.UPPER ? Items.AIR : Explodables.itemObsidianDoor;
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(Explodables.itemObsidianDoor);
    }
}
