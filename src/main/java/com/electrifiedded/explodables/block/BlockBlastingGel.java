package com.electrifiedded.explodables.block;

import com.electrifiedded.archaiclib.legacy.world.LegacyExplosionUtil;
import com.electrifiedded.archaiclib.legacy.world.LegacyParticles;
import com.electrifiedded.explodables.Explodables;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Redstone-wire-like trail. Once it carries any power it detonates after (40 - 2*power) ticks,
 * removing a column of {@code explosionSize} blocks beneath it.
 * The 1.7 global tick-handler map is replaced by scheduled block ticks.
 */
public class BlockBlastingGel extends BlockRedstoneWire {
    public static final int COLOUR_NORMAL = 8847510;
    public static final int COLOUR_HIGHEX = 852126;

    private final int explosionSize;
    private final int colour;
    private final boolean highEx;

    public BlockBlastingGel(int colour, int power, boolean highEx) {
        this.colour = colour;
        this.explosionSize = power;
        this.highEx = highEx;
        setHardness(0.0F);
        setSoundType(SoundType.SAND);
        disableStats();
    }

    public int getColour() {
        return colour;
    }

    /** Colour multiplier the 1.7 renderer used: base colour scaled by 0.6 + power/15. */
    public int tint(int power) {
        float b = 0.6F + power / 15.0F;
        int r = Math.min(255, (int) (b * (colour >> 16 & 0xFF)));
        int g = Math.min(255, (int) (b * (colour >> 8 & 0xFF)));
        int bl = Math.min(255, (int) (b * (colour & 0xFF)));
        return r << 16 | g << 8 | bl;
    }

    private Item item() {
        return highEx ? Explodables.itemHighExBlastingGel : Explodables.itemBlastingGel;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return item();
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(item());
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        checkExplode(world, pos);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        super.neighborChanged(state, world, pos, block, fromPos);
        checkExplode(world, pos);
    }

    private void checkExplode(World world, BlockPos pos) {
        if (world.isRemote) return;
        IBlockState s = world.getBlockState(pos);
        if (s.getBlock() != this) return;
        int power = s.getValue(POWER);
        if (power > 0 && !world.isUpdateScheduled(pos, this)) {
            world.scheduleUpdate(pos, this, Math.max(1, 40 - power * 2));
        }
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (world.isRemote || state.getBlock() != this) return;
        world.setBlockToAir(pos);
        doExplosion(world, pos);
    }

    public void doExplosion(World world, BlockPos pos) {
        LegacyExplosionUtil.explodeSound(world, pos.getX(), pos.getY(), pos.getZ());
        Explosion fake = LegacyExplosionUtil.dummy(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        for (int l = 1; l <= explosionSize; l++) {
            BlockPos p = pos.down(l);
            LegacyParticles.spawnBroadcast(world, "explode", p.getX(), p.getY(), p.getZ(), 0, 0, 0);
            LegacyParticles.spawnBroadcast(world, "smoke", p.getX(), p.getY(), p.getZ(), 0, 0, 0);
            IBlockState s = world.getBlockState(p);
            if (s.getMaterial() != Material.AIR && s.getBlock().getExplosionResistance(world, p, null, fake) < 100.0F) {
                LegacyExplosionUtil.blastBlock(world, p, 0.5F, fake);
            }
        }
    }
}
