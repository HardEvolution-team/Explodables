package com.electrifiedded.explodables.item;

import com.electrifiedded.explodables.Explodables;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Places blasting gel like redstone dust. */
public class ItemBlastingGel extends Item {
    private final boolean highEx;

    public ItemBlastingGel(boolean highEx) {
        this.highEx = highEx;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Block gel = highEx ? Explodables.blockHighExBlastingGel : Explodables.blockBlastingGel;
        boolean replace = world.getBlockState(pos).getBlock().isReplaceable(world, pos);
        BlockPos p = replace ? pos : pos.offset(facing);
        ItemStack stack = player.getHeldItem(hand);
        if (player.canPlayerEdit(p, facing, stack) && world.mayPlace(gel, p, false, facing, null) && gel.canPlaceBlockAt(world, p)) {
            world.setBlockState(p, gel.getDefaultState());
            if (!player.capabilities.isCreativeMode) stack.shrink(1);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }
}
