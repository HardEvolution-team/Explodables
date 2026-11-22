package com.electrifiedded.explodables.client;

import com.electrifiedded.explodables.block.BlockExplosivesChest;
import com.electrifiedded.explodables.entity.TileEntityExplosivesChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

/** Vanilla single-chest model with the explosives chest texture (served from the original jar). */
public class TileEntityExplosivesChestRenderer extends TileEntitySpecialRenderer<TileEntityExplosivesChest> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("explodables", "textures/models/explosiveschest.png");
    private static final ModelChest MODEL = new ModelChest();

    @Override
    public void render(TileEntityExplosivesChest te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        renderChest(te, x, y, z, partialTicks, destroyStage);
    }

    public static void renderChest(TileEntityExplosivesChest te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        EnumFacing facing = EnumFacing.SOUTH;
        if (te.hasWorld()) {
            IBlockState s = te.getWorld().getBlockState(te.getPos());
            if (s.getBlock() instanceof BlockExplosivesChest) facing = s.getValue(BlockExplosivesChest.FACING);
        }
        Minecraft mc = Minecraft.getMinecraft();
        if (destroyStage >= 0) {
            mc.getTextureManager().bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else {
            mc.getTextureManager().bindTexture(TEXTURE);
        }
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        int rot = 0;
        switch (facing) {
            case NORTH: rot = 180; break;
            case WEST: rot = 90; break;
            case EAST: rot = -90; break;
            default: break;
        }
        GlStateManager.rotate(rot, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
        f = 1.0F - f;
        f = 1.0F - f * f * f;
        MODEL.chestLid.rotateAngleX = -(f * (float) (Math.PI / 2));
        MODEL.renderAll();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}
