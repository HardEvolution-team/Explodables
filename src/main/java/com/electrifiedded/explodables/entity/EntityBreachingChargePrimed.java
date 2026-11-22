package com.electrifiedded.explodables.entity;

import com.electrifiedded.archaiclib.legacy.world.LegacyExplosionUtil;
import com.electrifiedded.archaiclib.legacy.world.LegacyParticles;
import com.electrifiedded.explodables.Explodables;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.Set;

/** Destroys only doors, trapdoors and glass in its blast; forces obsidian doors open. */
public class EntityBreachingChargePrimed extends EntityDirectionalExplosive {
    private static final int[][] RANGES = {
            {4, 13, 6, 16, 4, 13}, {4, 13, 0, 10, 4, 13},
            {4, 13, 4, 13, 6, 16}, {4, 13, 4, 13, 0, 10},
            {6, 16, 4, 13, 4, 13}, {0, 10, 4, 13, 4, 13}};

    public EntityBreachingChargePrimed(World world) {
        super(world);
    }

    public EntityBreachingChargePrimed(World world, double x, double y, double z, int dir) {
        super(world, x, y, z, dir);
    }

    @Override
    protected void explode() {
        float power = Explodables.breachingChargePower;
        int[] r = RANGES[getDirection()];
        Set<BlockPos> hit = LegacyExplosionUtil.rayCast(world, this, posX, posY, posZ, power, r[0], r[1], r[2], r[3], r[4], r[5],
                s -> s.getBlock() == Explodables.blockObsidianDoor);
        LegacyExplosionUtil.damageEntitiesAround(world, this, DAMAGE, posX, posY, posZ, 2.0F);
        explosionSound();
        LegacyParticles.spawnBroadcast(world, "hugeexplosion", posX, posY, posZ, 0, 0, 0);
        Explosion fake = LegacyExplosionUtil.dummy(world, posX, posY, posZ);
        for (BlockPos p : hit) {
            LegacyExplosionUtil.debrisParticles(world, p, posX, posY, posZ, power);
            IBlockState s = world.getBlockState(p);
            Block b = s.getBlock();
            if (b == Explodables.blockObsidianDoor) {
                IBlockState lower = s.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER ? s : world.getBlockState(p.down());
                if (lower.getBlock() == b && !lower.getValue(BlockDoor.OPEN)) {
                    ((BlockDoor) b).toggleDoor(world, p, true);
                }
            } else if (b instanceof BlockDoor || b instanceof BlockTrapDoor || b == Blocks.GLASS || b == Blocks.GLASS_PANE) {
                LegacyExplosionUtil.blastBlock(world, p, 0.3F, fake);
            }
        }
    }
}
