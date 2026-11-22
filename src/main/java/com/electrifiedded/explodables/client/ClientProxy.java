package com.electrifiedded.explodables.client;

import com.electrifiedded.explodables.CommonProxy;
import com.electrifiedded.explodables.Explodables;
import com.electrifiedded.explodables.block.BlockBlastingGel;
import com.electrifiedded.explodables.entity.*;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        RenderingRegistry.registerEntityRenderingHandler(EntityShapedChargePrimed.class, m -> new RenderDirectionalExplosive(m, () -> Explodables.blockShapedCharge));
        RenderingRegistry.registerEntityRenderingHandler(EntitySpokeBombPrimed.class, m -> new RenderDirectionalExplosive(m, () -> Explodables.blockSpokeBomb));
        RenderingRegistry.registerEntityRenderingHandler(EntityClaymoreMinePrimed.class, m -> new RenderDirectionalExplosive(m, () -> Explodables.blockClaymoreMine));
        RenderingRegistry.registerEntityRenderingHandler(EntityBreachingChargePrimed.class, m -> new RenderDirectionalExplosive(m, () -> Explodables.blockBreachingCharge));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExplosivesChest.class, new TileEntityExplosivesChestRenderer());
        final TileEntityExplosivesChest dummy = new TileEntityExplosivesChest();
        Item.getItemFromBlock(Explodables.blockExplosivesChest).setTileEntityItemStackRenderer(new TileEntityItemStackRenderer() {
            @Override
            public void renderByItem(ItemStack stack) {
                TileEntityExplosivesChestRenderer.renderChest(dummy, 0, 0, 0, 0.0F, -1);
            }
        });
    }

    @Override
    public void init() {
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((state, world, pos, tint) ->
                        state.getBlock() instanceof BlockBlastingGel ? ((BlockBlastingGel) state.getBlock()).tint(state.getValue(BlockRedstoneWire.POWER)) : -1,
                Explodables.blockBlastingGel, Explodables.blockHighExBlastingGel);
    }
}
