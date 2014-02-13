package elcon.mods.elconqore.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import elcon.mods.elconqore.lang.LanguageManager;

public class BlockMetadata extends Block {

	public BlockMetadata(Material material) {
		super(material);
	}

	public String getLocalizedName(ItemStack stack) {
		return LanguageManager.getLocalization(getUnlocalizedName(stack));
	}

	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName();
	}
	
	@Override
	public int damageDropped(int meta) {
		return meta;
	}
}
