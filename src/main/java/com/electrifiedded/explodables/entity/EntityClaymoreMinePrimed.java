package com.electrifiedded.explodables.entity;

import com.electrifiedded.archaiclib.legacy.world.LegacyExplosionUtil;
import com.electrifiedded.archaiclib.legacy.world.LegacyParticles;
import com.electrifiedded.explodables.Explodables;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/** Anti-personnel: hurts living things in a cone-ish box in front, leaves blocks alone. */
public class EntityClaymoreMinePrimed extends EntityDirectionalExplosive {
    public EntityClaymoreMinePrimed(World world) {
        super(world);
    }

    public EntityClaymoreMinePrimed(World world, double x, double y, double z, int dir) {
        super(world, x, y, z, dir);
    }

    @Override
    protected void explode() {
        explosionSound();
        LegacyParticles.spawnBroadcast(world, "hugeexplosion", posX, posY, posZ, 0, 0, 0);
        int size = (int) Explodables.claymoreMinePower;
        int sx = -2, ex = 2, sy = -2, ey = 2, sz = -2, ez = 2;
        switch (getDirection()) {
            case 0: sy = 1; ey = size; break;
            case 1: sy = -size; ey = 1; break;
            case 2: sz = 1; ez = size; break;
            case 3: sz = -size; ez = 1; break;
            case 4: sx = 1; ex = size; break;
            default: sx = -size; ex = 1; break;
        }
        int bx = MathHelper.floor(posX - 0.5), by = MathHelper.floor(posY - 0.5), bz = MathHelper.floor(posZ - 0.5);
        for (int x = sx; x <= ex; x++)
            for (int y = sy; y <= ey; y++)
                for (int z = sz; z <= ez; z++)
                    LegacyExplosionUtil.debrisParticles(world, new BlockPos(bx + x, by + y, bz + z), posX, posY, posZ, Explodables.claymoreMinePower);
        AxisAlignedBB box = new AxisAlignedBB(posX + sx, posY + sy, posZ + sz, posX + ex, posY + ey, posZ + ez);
        LegacyExplosionUtil.damageEntities(world, world.getEntitiesWithinAABB(EntityLivingBase.class, box), DAMAGE,
                posX, posY, posZ, Explodables.claymoreMinePower);
    }
}
