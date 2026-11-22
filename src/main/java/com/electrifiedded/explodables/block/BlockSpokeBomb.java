package com.electrifiedded.explodables.block;

import com.electrifiedded.explodables.entity.EntitySpokeBombPrimed;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/** Spoke bombs only bore horizontal tunnels. */
public class BlockSpokeBomb extends BlockDirectionalExplosive {
    public BlockSpokeBomb() {
        super(EntitySpokeBombPrimed::new);
    }

    @Override
    protected EnumFacing placementFacing(BlockPos pos, EntityLivingBase placer) {
        EnumFacing f = super.placementFacing(pos, placer);
        return f.getAxis() == EnumFacing.Axis.Y ? EnumFacing.NORTH : f;
    }
}
