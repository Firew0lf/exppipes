package ihamfp.exppipes.tileentities.pipeconfig;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.oredict.OreDictionary;

public class Filters {
	@SuppressWarnings("serial")
	public static List<Filter> filters = new ArrayList<Filter>() {{
		// Default filter: match on item/meta: isItemEqual()
		add(new Filter() {
			@Override
			public String getLongName() {
				return "Default";
			}

			@Override
			public String getShortName() {
				return "D";
			}
			
			@Override
			public boolean doesMatch(ItemStack reference, ItemStack stack) {
				return reference.isItemEqual(stack);
			}
		});
		
		// Fuzzy filter: match on item only
		add(new Filter() {
			@Override
			public String getLongName() {
				return "Fuzzy";
			}

			@Override
			public String getShortName() {
				return "F";
			}

			@Override
			public boolean doesMatch(ItemStack reference, ItemStack stack) {
				return reference.isItemEqualIgnoreDurability(stack);
			}
		});
		
		// Strict filter: match on item/meta/tags
		add(new Filter() {
			@Override
			public String getLongName() {
				return "Strict";
			}

			@Override
			public String getShortName() {
				return "S";
			}

			@Override
			public boolean doesMatch(ItemStack reference, ItemStack stack) {
				return reference.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(reference, stack);
			}
		});
		
		// OreDict filter: match if at least one oreDict entry matches
		add(new Filter() {
			@Override
			public String getLongName() {
				return "OreDict";
			}

			@Override
			public String getShortName() {
				return "O";
			}

			@Override
			public boolean doesMatch(ItemStack reference, ItemStack stack) {
				int[] refOreIDs = OreDictionary.getOreIDs(reference);
				int[] stackOreIDs = OreDictionary.getOreIDs(stack);
				for (int i : refOreIDs) {
					for (int j : stackOreIDs) {
						if (i==j) return true;
					}
				}
				return false;
			}
		});
		
		// Strict OreDict filter: match if all oreDict entry match
		add(new Filter() {
			@Override
			public String getLongName() {
				return "Strict OreDict";
			}

			@Override
			public String getShortName() {
				return "OS";
			}

			@Override
			public boolean doesMatch(ItemStack reference, ItemStack stack) {
				int[] refOreIDs = OreDictionary.getOreIDs(reference);
				int[] stackOreIDs = OreDictionary.getOreIDs(stack);
				if (refOreIDs.length != stackOreIDs.length) return false;
				for (int i=0;i<refOreIDs.length;i++) {
						if (refOreIDs[i] != stackOreIDs[i]) return false;
				}
				return true;
			}
		});
		
		// Shape filter: match on first oreDict word (=type), i.e. "ore", "block", "ingot"...
		add(new Filter() {
			@Override
			public String getLongName() {
				return "Shape";
			}

			@Override
			public String getShortName() {
				return "SH";
			}

			@Override
			public boolean doesMatch(ItemStack reference, ItemStack stack) {
				int[] refOreIDs = OreDictionary.getOreIDs(reference);
				int[] stackOreIDs = OreDictionary.getOreIDs(stack);
				for (int i : refOreIDs) {
					String refOreShape = OreDictionary.getOreName(i).replaceAll("[A-Z][a-z]+", "");
					for (int j : stackOreIDs) {
						String stackOreName = OreDictionary.getOreName(j);
						if (stackOreName.startsWith(refOreShape)) return true;
					}
				}
				return false;
			}
		});
		
		// Fluid filter: match on contained fluid
		add(new Filter() {
			@Override
			public String getLongName() {
				return "Fluid";
			}

			@Override
			public String getShortName() {
				return "FL";
			}

			@Override
			public boolean doesMatch(ItemStack reference, ItemStack stack) {
				if (reference.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) && stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
					IFluidHandlerItem refFluid = reference.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
					IFluidTankProperties[] refProp = refFluid.getTankProperties();
					boolean isRefEmpty = true;
					for (IFluidTankProperties tank : refProp) {
						if (tank.getContents().amount != 0) isRefEmpty = false;
						for (IFluidTankProperties stackTank : stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).getTankProperties()) {
							if (stackTank.getContents().getFluid().equals(tank.getContents().getFluid())) return true;
						}
					}
					if (isRefEmpty) return true; // Match any fluid-storing item
				}
				return false;
			}
		});
	}};
	
	public static Filter fromShortString(String shortName) {
		for (Filter f : filters) {
			if (f.getShortName().equals(shortName)) return f;
		}
		return null;
	}
	
	public static int idFromShortString(String shortName) {
		return filters.indexOf(fromShortString(shortName));
	}
}