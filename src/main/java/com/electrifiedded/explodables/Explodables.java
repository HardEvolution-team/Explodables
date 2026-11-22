package com.electrifiedded.explodables;

import com.electrifiedded.archaiclib.legacy.registry.LegacyGameRegistry;
import com.electrifiedded.archaiclib.legacy.registry.LegacyRecipes;
import com.electrifiedded.archaiclib.original.OriginalModAssets;
import com.electrifiedded.explodables.block.*;
import com.electrifiedded.explodables.entity.*;
import com.electrifiedded.explodables.item.ItemBlastingGel;
import com.electrifiedded.explodables.item.ItemPlasticExplosive;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Explodables (Fearitude / Stealthware, 1.7.10) reimplemented for 1.12.2.
 * The StealthwareCore helpers it relied on (config getters, block/item registration, icon naming)
 * are folded in here; its version checker and the unused metadata store are dropped.
 */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = "required-after:archaiclib@[1.30.0,)")
public class Explodables {
    public static final String ORIGINAL_ID = "Explodables";
    public static final String ORIGINAL_NAME = "Explodables-1.7.10.jar (Explodables 1.9 for 1.7.10 by Fearitude)";
    public static final String ORIGINAL_URL = "http://stealthware.co.uk/explodables-a6 (search: Explodables Fearitude 1.7.10)";

    @Mod.Instance(Reference.MOD_ID)
    public static Explodables instance;

    @SidedProxy(clientSide = "com.electrifiedded.explodables.client.ClientProxy", serverSide = "com.electrifiedded.explodables.CommonProxy")
    public static CommonProxy proxy;

    public static float shapedChargePower = 10.0F;
    public static int blastingGelPower = 4;
    public static int highExBlastingGelPower = 8;
    public static int spokeBombPower = 4;
    public static float claymoreMinePower = 8.0F;
    public static float breachingChargePower = 6.0F;

    public static Block blockShapedCharge, blockSpokeBomb, blockReinforcedIron, blockPlasticExplosive, blockExplosivesChest,
            blockObsidianDoor, blockReinforcedGlass, blockClaymoreMine, blockClaymoreMulti, blockBreachingCharge,
            blockReinforcedGlassPane, blockObsidianButton;
    public static BlockBlastingGel blockBlastingGel, blockHighExBlastingGel;
    public static Item itemBlastingGel, itemHighExBlastingGel, itemMoltenIron, itemScrapIron, itemGelatine, itemObsidianDoor;

    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        OriginalModAssets.require(Reference.MOD_ID, ORIGINAL_ID, ORIGINAL_NAME, ORIGINAL_URL, Reference.MOD_ID);
        loadConfig(new Configuration(e.getSuggestedConfigurationFile()));

        blockShapedCharge = block(new BlockDirectionalExplosive(EntityShapedChargePrimed::new), "shapedcharge", "shapedCharge", CreativeTabs.REDSTONE);
        blockSpokeBomb = block(new BlockSpokeBomb(), "spokebomb", "spokeBomb", CreativeTabs.REDSTONE);
        blockClaymoreMine = block(new BlockDirectionalExplosive(EntityClaymoreMinePrimed::new), "claymore", "claymore", CreativeTabs.REDSTONE);
        blockClaymoreMulti = block(new BlockDirectionalExplosive(EntityClaymoreMinePrimed::new), "claymoremulti", "claymoreMulti", CreativeTabs.REDSTONE);
        blockBreachingCharge = block(new BlockDirectionalExplosive(EntityBreachingChargePrimed::new), "breachcharge", "breachCharge", CreativeTabs.REDSTONE);
        blockPlasticExplosive = new BlockPlasticExplosive().setTranslationKey("explodables.plasticExplosive").setCreativeTab(CreativeTabs.MATERIALS);
        LegacyGameRegistry.registerBlock(blockPlasticExplosive, new ItemPlasticExplosive(blockPlasticExplosive), "plasticexplosive");
        LegacyGameRegistry.registerItemModel(blockPlasticExplosive, 0, Reference.MOD_ID + ":plasticexplosive", "inventory");
        blockReinforcedIron = block(new BlockReinforcedIron(), "reinforcediron", "reinforcedIron", null);
        blockExplosivesChest = block(new BlockExplosivesChest(), "explosiveschest", "explosivesChest", CreativeTabs.DECORATIONS);
        blockReinforcedGlass = block(new BlockReinforcedGlass(), "reinforcedglass", "reinforcedGlass", CreativeTabs.BUILDING_BLOCKS);
        blockReinforcedGlassPane = block(new BlockReinforcedGlassPane(), "reinforcedglasspane", "reinforcedGlassPane", CreativeTabs.DECORATIONS);
        blockObsidianButton = block(new BlockButtonObsidian(), "obsidianbutton", "obsidianButton", CreativeTabs.REDSTONE);
        blockBlastingGel = new BlockBlastingGel(BlockBlastingGel.COLOUR_NORMAL, blastingGelPower, false);
        blockBlastingGel.setTranslationKey("explodables.blastingGel");
        LegacyGameRegistry.registerBlock(blockBlastingGel, (Item) null, "blastinggel");
        blockHighExBlastingGel = new BlockBlastingGel(BlockBlastingGel.COLOUR_HIGHEX, highExBlastingGelPower, true);
        blockHighExBlastingGel.setTranslationKey("explodables.blastingGelHE");
        LegacyGameRegistry.registerBlock(blockHighExBlastingGel, (Item) null, "blastinggel_he");
        blockObsidianDoor = new BlockObsidianDoor().setTranslationKey("explodables.doorObsidianBlock");
        LegacyGameRegistry.registerBlock(blockObsidianDoor, (Item) null, "obsidiandoor");

        itemBlastingGel = item(new ItemBlastingGel(false), "iblastinggel", "IBlastingGel", CreativeTabs.REDSTONE);
        itemHighExBlastingGel = item(new ItemBlastingGel(true), "iblastinggel_he", "IBlastingGelHE", CreativeTabs.REDSTONE);
        itemMoltenIron = item(new Item(), "molteniron", "moltenIron", CreativeTabs.MATERIALS);
        itemScrapIron = item(new Item(), "scrapiron", "scrapIron", CreativeTabs.MATERIALS);
        itemGelatine = item(new Item(), "gelatine", "gelatine", CreativeTabs.MATERIALS);
        itemObsidianDoor = item(new ItemDoor(blockObsidianDoor).setMaxStackSize(8), "doorobsidian", "doorObsidian", CreativeTabs.REDSTONE);

        GameRegistry.registerTileEntity(TileEntityExplosivesChest.class, new ResourceLocation(Reference.MOD_ID, "explosiveschest"));
        LegacyGameRegistry.registerModEntity(EntityShapedChargePrimed.class, "ShapedChargePrimed", 0, this, 160, 5, false);
        LegacyGameRegistry.registerModEntity(EntitySpokeBombPrimed.class, "SpokeBombPrimed", 1, this, 160, 5, false);
        LegacyGameRegistry.registerModEntity(EntityClaymoreMinePrimed.class, "ClaymoreMinePrimed", 2, this, 160, 5, false);
        LegacyGameRegistry.registerModEntity(EntityBreachingChargePrimed.class, "BreachingChargePrimed", 3, this, 160, 5, false);
        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent e) {
        LegacyRecipes.addRecipe(new ItemStack(blockShapedCharge, 6), "#X#", "###", "ZZZ", '#', blockPlasticExplosive, 'X', Items.IRON_INGOT, 'Z', Blocks.OBSIDIAN);
        LegacyRecipes.addShapelessRecipe(new ItemStack(itemBlastingGel, 4), Items.REDSTONE, Items.GUNPOWDER, itemGelatine);
        LegacyRecipes.addShapelessRecipe(new ItemStack(itemHighExBlastingGel, 1), itemBlastingGel, new ItemStack(Items.DYE, 1, 4));
        LegacyRecipes.addRecipe(new ItemStack(blockSpokeBomb, 1), " # ", "#X#", " # ", '#', itemMoltenIron, 'X', blockPlasticExplosive);
        LegacyRecipes.addRecipe(new ItemStack(itemMoltenIron, 20), "#", "#", "X", '#', Items.IRON_INGOT, 'X', Items.LAVA_BUCKET);
        LegacyRecipes.addShapelessRecipe(new ItemStack(blockPlasticExplosive, 2), Blocks.TNT, Blocks.CLAY);
        LegacyRecipes.addRecipe(new ItemStack(blockExplosivesChest, 1), "#X#", "X X", "#X#", '#', Blocks.OBSIDIAN, 'X', Items.IRON_INGOT);
        LegacyRecipes.addRecipe(new ItemStack(itemObsidianDoor, 1), "##", "##", "##", '#', Blocks.OBSIDIAN);
        LegacyRecipes.addRecipe(new ItemStack(blockReinforcedGlass, 4), " X ", "X#X", " X ", '#', Blocks.GLASS, 'X', Blocks.OBSIDIAN);
        LegacyRecipes.addRecipe(new ItemStack(blockClaymoreMine, 4), "###", "X#X", "XXX", '#', blockPlasticExplosive, 'X', Items.IRON_INGOT);
        LegacyRecipes.addRecipe(new ItemStack(blockClaymoreMulti, 1), "X#X", "XXX", '#', Items.REDSTONE, 'X', Items.IRON_INGOT);
        LegacyRecipes.addRecipe(new ItemStack(blockBreachingCharge, 6), "XXX", "ZZZ", 'X', blockPlasticExplosive, 'Z', Blocks.PLANKS);
        LegacyRecipes.addRecipe(new ItemStack(blockReinforcedGlassPane, 16), "###", "###", '#', blockReinforcedGlass);
        LegacyRecipes.addShapelessRecipe(new ItemStack(blockObsidianButton, 1), Blocks.OBSIDIAN);
        GameRegistry.addSmelting(itemScrapIron, new ItemStack(itemMoltenIron, 1), 0.3F);
        GameRegistry.addSmelting(Items.BONE, new ItemStack(itemGelatine, 1), 0.3F);
        GameRegistry.addSmelting(Items.LEATHER, new ItemStack(itemGelatine, 1), 0.3F);
        proxy.init();
    }

    private void loadConfig(Configuration c) {
        shapedChargePower = range(c, "ShapedChargePower", "Controls size of explosion for Shaped Charges", (int) shapedChargePower, 1, 100);
        blastingGelPower = range(c, "BlastingGelPower", "Controls size of explosion for Blasting Gel (the number of blocks to remove)", blastingGelPower, 1, 20);
        highExBlastingGelPower = range(c, "HighExBlastingGelPower", "Controls size of explosion for High-Ex Blasting Gel (the number of blocks to remove)", highExBlastingGelPower, 1, 30);
        spokeBombPower = range(c, "SpokeBombPower", "Controls size of explosion for Spoke Bombs (the length of the tunnel)", spokeBombPower, 1, 30);
        claymoreMinePower = range(c, "ClaymoreMinePower", "Controls size of explosion for Claymore Mines", (int) claymoreMinePower, 1, 40);
        breachingChargePower = range(c, "BreachingChargePower", "Controls size of explosion for Breaching Charge", (int) breachingChargePower, 1, 40);
        if (c.hasChanged()) c.save();
    }

    private static int range(Configuration c, String name, String comment, int def, int min, int max) {
        Property p = c.get("general", name, def);
        p.setComment(comment + " (valid range: " + min + "-" + max + ")");
        return Math.max(min, Math.min(max, p.getInt()));
    }

    private static Block block(Block b, String name, String key, CreativeTabs tab) {
        b.setTranslationKey("explodables." + key);
        if (tab != null) b.setCreativeTab(tab);
        LegacyGameRegistry.registerBlock(b, name);
        LegacyGameRegistry.registerItemModel(b, 0, Reference.MOD_ID + ":" + name, "inventory");
        return b;
    }

    private static Item item(Item i, String name, String key, CreativeTabs tab) {
        i.setTranslationKey("explodables." + key).setCreativeTab(tab);
        LegacyGameRegistry.registerItem(i, name);
        LegacyGameRegistry.registerItemModel(i, 0, Reference.MOD_ID + ":" + name, "inventory");
        return i;
    }
}
