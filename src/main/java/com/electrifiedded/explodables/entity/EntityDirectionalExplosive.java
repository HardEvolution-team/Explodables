package com.electrifiedded.explodables.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

/** Primed directional charge; fuse and facing are synced with the data manager (replaces the 1.7 packets). */
public abstract class EntityDirectionalExplosive extends Entity {
    public static final DamageSource DAMAGE = DamageSource.causeExplosionDamage((EntityLivingBase) null);
    private static final DataParameter<Integer> FUSE = EntityDataManager.createKey(EntityDirectionalExplosive.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DIRECTION = EntityDataManager.createKey(EntityDirectionalExplosive.class, DataSerializers.VARINT);

    public EntityDirectionalExplosive(World world) {
        super(world);
        preventEntitySpawning = true;
        setSize(1.0F, 1.0F);
    }

    public EntityDirectionalExplosive(World world, double x, double y, double z, int direction) {
        this(world);
        setPosition(x, y, z);
        prevPosX = x;
        prevPosY = y;
        prevPosZ = z;
        setFuse(80);
        setDirection(direction);
    }

    @Override
    protected void entityInit() {
        dataManager.register(FUSE, 80);
        dataManager.register(DIRECTION, 1);
    }

    public int getFuse() {
        return dataManager.get(FUSE);
    }

    public void setFuse(int fuse) {
        dataManager.set(FUSE, fuse);
    }

    public int getDirection() {
        return dataManager.get(DIRECTION);
    }

    public void setDirection(int d) {
        dataManager.set(DIRECTION, Math.max(0, Math.min(5, d)));
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return !isDead;
    }

    @Override
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        int fuse = getFuse() - 1;
        if (!world.isRemote) setFuse(fuse);
        if (fuse <= 0) {
            if (!world.isRemote) {
                setDead();
                explode();
            }
        } else {
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX, posY + 0.5, posZ, 0, 0, 0);
        }
    }

    /** Server side only. */
    protected abstract void explode();

    protected void explosionSound() {
        com.electrifiedded.archaiclib.legacy.world.LegacyExplosionUtil.explodeSound(world, posX, posY, posZ);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag) {
        tag.setByte("Fuse", (byte) getFuse());
        tag.setByte("Meta", (byte) getDirection());
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag) {
        setFuse(tag.getByte("Fuse"));
        setDirection(tag.getByte("Meta"));
    }
}
