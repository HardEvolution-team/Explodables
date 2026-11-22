package com.electrifiedded.explodables.entity;

import com.electrifiedded.archaiclib.legacy.world.LegacyExplosionUtil;
import com.electrifiedded.archaiclib.legacy.world.LegacyParticles;
import com.electrifiedded.explodables.Explodables;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.Set;

/** Blast focused into the half-space the charge faces. */
public class EntityShapedChargePrimed extends EntityDirectionalExplosive {
    /** Ray-grid sub-cubes (0..16) per direction: sx, ex, sy, ey, sz, ez. */
    private static final int[][] RANGES = {
            {3, 13, 6, 16, 3, 13}, {3, 13, 0, 10, 3, 13},
            {4, 13, 4, 13, 6, 16}, {4, 13, 4, 13, 0, 10},
            {6, 16, 4, 13, 4, 13}, {0, 10, 4, 13, 4, 13}};

    public EntityShapedChargePrimed(World world) {
        super(world);
    }

    public EntityShapedChargePrimed(World world, double x, double y, double z, int dir) {
        super(world, x, y, z, dir);
    }

    @Override
    protected void explode() {
        int d = getDirection();
        double ey = posY + (d >= 2 ? 0.5 : 0.0);
        float power = Explodables.shapedChargePower;
        int[] r = RANGES[d];
        Set<BlockPos> hit = LegacyExplosionUtil.rayCast(world, null, posX, ey, posZ, power, r[0], r[1], r[2], r[3], r[4], r[5], null);
        LegacyExplosionUtil.damageEntitiesAround(world, null, DAMAGE, posX, ey, posZ, 6.0F);
        LegacyExplosionUtil.explodeSound(world, posX, ey, posZ);
        LegacyParticles.spawnBroadcast(world, "hugeexplosion", posX, ey, posZ, 0, 0, 0);
        Explosion fake = LegacyExplosionUtil.dummy(world, posX, ey, posZ);
        for (BlockPos p : hit) {
            LegacyExplosionUtil.debrisParticles(world, p, posX, ey, posZ, power);
            LegacyExplosionUtil.blastBlock(world, p, 0.3F, fake);
        }
    }
}
