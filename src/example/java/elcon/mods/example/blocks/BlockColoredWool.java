package elcon.mods.example.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elcon.mods.elconqore.blocks.BlockExtendedMetadata;

public class BlockColoredWool extends BlockExtendedMetadata {

	public static int[] colors = new int[256];
	
	public BlockColoredWool() {
		super(Material.cloth);
		setStepSound(Block.soundTypeCloth);
		setCreativeTab(CreativeTabs.tabBlock);
		for(int i = 0; i < 256; i++) {
			colors[i] = i * 0xFFFF + i * 0xFF + i;
		}
	}
	
	@Override
	public String getUnlocalizedName() {
		return "tile.coloredWool.name";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int meta) {
		return colors[meta];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z) {
		return getRenderColor(getMetadata(blockAccess, x, y, z));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return Blocks.wool.getIcon(side, 0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
		for(int i = 0; i < 256; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}
}
