package com.electrifiedded.explodables.block;

import com.electrifiedded.archaiclib.legacy.sound.LegacySounds;
import com.electrifiedded.explodables.entity.EntityDirectionalExplosive;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.Random;

/** Placeable charge facing one of six directions; redstone or a nearby blast primes it into an entity. */
public class BlockDirectionalExplosive extends Block {
    public static final PropertyDirection FACING = BlockDirectional.FACING;

    public interface Factory {
        EntityDirectionalExplosive create(World world, double x, double y, double z, int direction);
    }

    private final Factory factory;

    public BlockDirectionalExplosive(Factory factory) {
        super(Material.TNT);
        this.factory = factory;
        setHardness(0.0F);
        setSoundType(SoundType.PLANT);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta & 7));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    protected EnumFacing placementFacing(BlockPos pos, EntityLivingBase placer) {
        return EnumFacing.getDirectionFromEntityLiving(pos, placer);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placementFacing(pos, placer));
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (world.isBlockPowered(pos)) {
            explode(world, pos, -1);
            world.setBlockToAir(pos);
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if (block != net.minecraft.init.Blocks.AIR && block.getDefaultState().canProvidePower() && world.isBlockPowered(pos)) {
            explode(world, pos, -1);
            world.setBlockToAir(pos);
        }
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public void onPlayerDestroy(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote && world.getGameRules().getBoolean("doTileDrops")) {
            spawnAsEntity(world, pos, new ItemStack(this, 1, 0));
        }
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
        if (!world.isRemote) {
            explode(world, pos, 10 + world.rand.nextInt(40));
            super.onBlockExploded(world, pos, explosion);
        }
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosion) {
        return false;
    }

    public void explode(World world, BlockPos pos, int overrideFuse) {
        if (world.isRemote) return;
        IBlockState state = world.getBlockState(pos);
        int direction = state.getBlock() == this ? state.getValue(FACING).getIndex() : 1;
        EntityDirectionalExplosive entity = factory.create(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, direction);
        if (overrideFuse >= 0) entity.setFuse(overrideFuse);
        world.spawnEntity(entity);
        if (entity.getFuse() > 40) {
            LegacySounds.playSoundEffect(world, entity.posX, entity.posY + 0.5, entity.posZ, "random.fuse", 1.0F, 1.0F);
        }
    }
}
