package com.electrifiedded.explodables.block;

import com.electrifiedded.explodables.Explodables;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Thin iron lining left on tunnel walls by spoke bombs. Type 0-5 = single plate
 * (floor, ceiling, west, east, north, south), 6-13 = edge pieces made of two plates.
 */
public class BlockReinforcedIron extends Block {
    public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 13);
    private static final AxisAlignedBB[] BOUNDS = {
            new AxisAlignedBB(0, 0, 0, 1, 0.125, 1),
            new AxisAlignedBB(0, 0.875, 0, 1, 1, 1),
            new AxisAlignedBB(0, 0, 0, 0.125, 1, 1),
            new AxisAlignedBB(0.875, 0, 0, 1, 1, 1),
            new AxisAlignedBB(0, 0, 0, 1, 1, 0.125),
            new AxisAlignedBB(0, 0, 0.875, 1, 1, 1),
    };

    public BlockReinforcedIron() {
        super(Material.IRON);
        setHardness(0.5F);
        setSoundType(SoundType.METAL);
        setDefaultState(blockState.getBaseState().withProperty(TYPE, 0));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(TYPE, Math.min(meta, 13));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        int t = state.getValue(TYPE);
        return t < BOUNDS.length ? BOUNDS[t] : FULL_BLOCK_AABB;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isPassable(IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public int quantityDropped(Random random) {
        return random.nextFloat() <= 0.1F ? 1 : 0;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Explodables.itemScrapIron;
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(Explodables.itemScrapIron);
    }

    @Override
    public EnumPushReaction getPushReaction(IBlockState state) {
        return EnumPushReaction.DESTROY;
    }
}
