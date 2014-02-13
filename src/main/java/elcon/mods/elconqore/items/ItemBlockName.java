package elcon.mods.elconqore.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockName extends ItemBlock {

	public ItemBlockName(Block block) {
		super(block);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return field_150939_a.getLocalizedName();
	}
}
