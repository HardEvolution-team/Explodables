package com.electrifiedded.explodables.entity;

import com.electrifiedded.archaiclib.legacy.world.LegacyExplosionUtil;
import com.electrifiedded.archaiclib.legacy.world.LegacyParticles;
import com.electrifiedded.explodables.Explodables;
import com.electrifiedded.explodables.block.BlockReinforcedIron;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

/** Bores a 3x3 tunnel and lines its walls with reinforced iron. */
public class EntitySpokeBombPrimed extends EntityDirectionalExplosive {
    public EntitySpokeBombPrimed(World world) {
        super(world);
    }

    public EntitySpokeBombPrimed(World world, double x, double y, double z, int dir) {
        super(world, x, y, z, dir);
    }

    @Override
    protected void explode() {
        explosionSound();
        int size = Explodables.spokeBombPower - 1;
        int direction = getDirection();
        int sx = -1, ex = 1, sy = -1, ey = 1, sz = -1, ez = 1;
        switch (direction) {
            case 0: sy = 0; ey = size; break;
            case 1: sy = -size; ey = 0; break;
            case 2: sz = 0; ez = size; break;
            case 3: sz = -size; ez = 0; break;
            case 4: sx = 0; ex = size; break;
            default: sx = -size; ex = 0; break;
        }
        int bx = MathHelper.floor(posX - 0.5), by = MathHelper.floor(posY), bz = MathHelper.floor(posZ - 0.5);
        Explosion fake = LegacyExplosionUtil.dummy(world, posX, posY, posZ);
        for (int x = sx; x <= ex; x++) {
            for (int y = sy; y <= ey; y++) {
                for (int z = sz; z <= ez; z++) {
                    BlockPos p = new BlockPos(bx + x, by + y, bz + z);
                    LegacyParticles.spawnBroadcast(world, "explode", p.getX(), p.getY(), p.getZ(), 0, 0, 0);
                    LegacyParticles.spawnBroadcast(world, "smoke", p.getX(), p.getY(), p.getZ(), 0, 0, 0);
                    IBlockState s = world.getBlockState(p);
                    if (s.getMaterial() == Material.AIR || s.getBlock().getExplosionResistance(world, p, null, fake) >= 150.0F) continue;
                    LegacyExplosionUtil.blastBlock(world, p, 0.5F, fake);
                    int type = -1;
                    if ((direction == 2 || direction == 3) && (x == sx || x == ex || y == sy || y == ey)) {
                        if (x == sx && y == sy) type = 6;
                        else if (x == sx && y == ey) type = 7;
                        else if (x == ex && y == sy) type = 8;
                        else if (x == ex && y == ey) type = 9;
                        else if (y == sy) type = 0;
                        else if (y == ey) type = 1;
                        else if (x == sx) type = 2;
                        else type = 3;
                    } else if ((direction == 4 || direction == 5) && (y == sy || y == ey || z == sz || z == ez)) {
                        if (y == sy && z == sz) type = 10;
                        else if (y == sy && z == ez) type = 11;
                        else if (y == ey && z == sz) type = 12;
                        else if (y == ey && z == ez) type = 13;
                        else if (y == sy) type = 0;
                        else if (y == ey) type = 1;
                        else if (z == sz) type = 4;
                        else type = 5;
                    }
                    if (type >= 0) {
                        world.setBlockState(p, Explodables.blockReinforcedIron.getDefaultState().withProperty(BlockReinforcedIron.TYPE, type), 3);
                    }
                }
            }
        }
        LegacyExplosionUtil.damageEntitiesAround(world, null, DAMAGE, posX, posY, posZ, 2.0F);
    }
}
