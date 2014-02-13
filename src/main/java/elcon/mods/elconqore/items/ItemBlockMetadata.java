package elcon.mods.elconqore.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import elcon.mods.elconqore.blocks.BlockMetadata;

public class ItemBlockMetadata extends ItemBlockName {

	public ItemBlockMetadata(Block block) {
		super(block);
		setMaxDamage(0);
        setHasSubtypes(true);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return ((BlockMetadata) field_150939_a).getLocalizedName(stack);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return ((BlockMetadata) field_150939_a).getUnlocalizedName(stack);
	}
	
	@Override
	public int getMetadata(int meta) {
		return meta;
	}
}
