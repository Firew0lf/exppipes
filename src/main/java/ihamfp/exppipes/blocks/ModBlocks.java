package ihamfp.exppipes.blocks;

import java.util.ArrayList;
import java.util.List;

import ihamfp.exppipes.tileentities.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {
	public static BlockPipe pipe = new BlockPipe("blockPipe");
	public static BlockRoutingPipe routingPipe = new BlockRoutingPipe("blockRoutingPipe");
	public static BlockProviderPipe providerPipe = new BlockProviderPipe("blockProviderPipe");
	public static BlockSupplierPipe supplierPipe = new BlockSupplierPipe("blockSupplierPipe");
	public static BlockRequestPipe requestPipe = new BlockRequestPipe("blockRequestPipe");
	public static BlockNoInsertionPipe noInsertionPipe = new BlockNoInsertionPipe("blockNoInsertionPipe");
	public static BlockCraftingPipe craftingPipe = new BlockCraftingPipe("blockCraftingPipe");
	public static BlockExtractionPipe extractionPipe = new BlockExtractionPipe("blockExtractionPipe");
	public static BlockPolyProviderPipe polyproviderPipe = new BlockPolyProviderPipe("blockPolyProviderPipe");
	public static BlockCountingPipe countingPipe = new BlockCountingPipe("blockCountingPipe");
	public static BlockStockKeeperPipe stockKeeperPipe = new BlockStockKeeperPipe("blockStockKeeperPipe");
	public static BlockRetrieverPipe retrieverPipe = new BlockRetrieverPipe("blockRetrieverPipe");
	public static BlockRobinPipe robinPipe = new BlockRobinPipe("blockRobinPipe");
	
	public static BlockStackDisplay stackDisplay = new BlockStackDisplay("blockStackDisplay");
	public static BlockBufferStackDisplay bufferStackDisplay = new BlockBufferStackDisplay("blockBufferStackDisplay");
	
	public static BlockRequestStation requestStation = new BlockRequestStation("blockRequestStation");
	
	public static ArrayList<Block> modBlocks = new ArrayList<Block>();
	public static ArrayList<Item> modItemBlocks = new ArrayList<Item>();
	
	public static void preInit() {
		modBlocks.add(pipe);
		modBlocks.add(routingPipe);
		modBlocks.add(providerPipe);
		modBlocks.add(supplierPipe);
		modBlocks.add(requestPipe);
		modBlocks.add(noInsertionPipe);
		modBlocks.add(craftingPipe);
		modBlocks.add(extractionPipe);
		modBlocks.add(polyproviderPipe);
		modBlocks.add(countingPipe);
		modBlocks.add(stockKeeperPipe);
		modBlocks.add(retrieverPipe);
		modBlocks.add(robinPipe);
		
		modBlocks.add(stackDisplay);
		modBlocks.add(bufferStackDisplay);
		modBlocks.add(requestStation);
		
		MinecraftForge.EVENT_BUS.register(new ModBlocks());
	}
	
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		for (Block b : modBlocks) {
			b.setTranslationKey(b.getRegistryName().toString());
			event.getRegistry().register(b);
		}
		ModTileEntities.registerTileEntities();
	}
	
	@SubscribeEvent
	public void registerItemBlocks(RegistryEvent.Register<Item> event) {
		for (Block b : modBlocks) {
			Item ib = new ItemBlock(b) {
				@Override
				public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
					super.addInformation(stack, worldIn, tooltip, flagIn);
					
					TextComponentTranslation tct = new TextComponentTranslation(b.getTranslationKey() + ".tooltip");
					if (!tct.getUnformattedText().equals(b.getTranslationKey() + ".tooltip")) tooltip.add(tct.getFormattedText());
				}
			};
			ib.setRegistryName(b.getRegistryName());
			modItemBlocks.add(ib);
			event.getRegistry().register(ib);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void registerColors(ColorHandlerEvent.Block event) {
		BlockPipeColor blockPipeColor = new BlockPipeColor();
		BlockColors blockColors = event.getBlockColors();
		if (blockColors == null) {
			
		}
		for (Block block : modBlocks) {
			if (block instanceof BlockPipe) {
				blockColors.registerBlockColorHandler(blockPipeColor, block);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void registerItemBlockModels(ModelRegistryEvent event) {
		for (Block b : modBlocks) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 0, new ModelResourceLocation(b.getRegistryName(), "gui"));
		}
	}
}
