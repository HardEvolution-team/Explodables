package com.electrifiedded.explodables.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

/** 9-slot chest inventory (dispenser container/GUI) with chest-style lid animation. */
public class TileEntityExplosivesChest extends TileEntityDispenser implements ITickable {
    public int numPlayersUsing;
    public float lidAngle, prevLidAngle;

    @Override
    public String getName() {
        return hasCustomName() ? super.getName() : "container.explodables.explosivesChest";
    }

    @Override
    public void update() {
        prevLidAngle = lidAngle;
        if (numPlayersUsing > 0 && lidAngle == 0.0F) {
            playSound(true);
        }
        if (numPlayersUsing == 0 && lidAngle > 0.0F || numPlayersUsing > 0 && lidAngle < 1.0F) {
            float before = lidAngle;
            lidAngle += numPlayersUsing > 0 ? 0.1F : -0.1F;
            if (lidAngle > 1.0F) lidAngle = 1.0F;
            if (lidAngle < 0.5F && before >= 0.5F) playSound(false);
            if (lidAngle < 0.0F) lidAngle = 0.0F;
        }
    }

    private void playSound(boolean open) {
        if (world == null) return;
        world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                open ? SoundEvents.BLOCK_CHEST_OPEN : SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS,
                0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            numPlayersUsing = type;
            return true;
        }
        return super.receiveClientEvent(id, type);
    }

    @Override
    public void openInventory(EntityPlayer player) {
        if (!player.isSpectator()) {
            if (numPlayersUsing < 0) numPlayersUsing = 0;
            numPlayersUsing++;
            world.addBlockEvent(pos, getBlockType(), 1, numPlayersUsing);
        }
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        if (!player.isSpectator()) {
            numPlayersUsing--;
            world.addBlockEvent(pos, getBlockType(), 1, numPlayersUsing);
        }
    }
}
