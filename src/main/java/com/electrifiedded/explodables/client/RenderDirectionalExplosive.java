package com.electrifiedded.explodables.client;

import com.electrifiedded.explodables.block.BlockDirectionalExplosive;
import com.electrifiedded.explodables.entity.EntityDirectionalExplosive;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.function.Supplier;

/** Primed charge rendered like vanilla primed TNT, using the charge block in its facing. */
public class RenderDirectionalExplosive extends Render<EntityDirectionalExplosive> {
    private final Supplier<Block> block;

    public RenderDirectionalExplosive(RenderManager manager, Supplier<Block> block) {
        super(manager);
        this.block = block;
        shadowSize = 0.5F;
    }

    @Override
    public void doRender(EntityDirectionalExplosive entity, double x, double y, double z, float yaw, float partialTicks) {
        BlockRendererDispatcher brd = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBlockState state = block.get().getDefaultState().withProperty(BlockDirectionalExplosive.FACING, EnumFacing.byIndex(entity.getDirection()));
        int fuse = entity.getFuse();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y + 0.5F, (float) z);
        if (fuse - partialTicks + 1.0F < 10.0F) {
            float f = MathHelper.clamp(1.0F - (fuse - partialTicks + 1.0F) / 10.0F, 0.0F, 1.0F);
            f *= f;
            f *= f;
            float s = 1.0F + f * 0.3F;
            GlStateManager.scale(s, s, s);
        }
        float alpha = (1.0F - (fuse - partialTicks + 1.0F) / 100.0F) * 0.8F;
        bindEntityTexture(entity);
        GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, 0.5F);
        brd.renderBlockBrightness(state, entity.getBrightness());
        GlStateManager.translate(0.0F, 0.0F, 1.0F);
        if (fuse / 5 % 2 == 0) {
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.DST_ALPHA);
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
            GlStateManager.doPolygonOffset(-3.0F, -3.0F);
            GlStateManager.enablePolygonOffset();
            brd.renderBlockBrightness(state, 1.0F);
            GlStateManager.doPolygonOffset(0.0F, 0.0F);
            GlStateManager.disablePolygonOffset();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
        }
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, yaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityDirectionalExplosive entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
