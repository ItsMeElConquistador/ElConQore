package elcon.mods.example.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elcon.mods.elconqore.blocks.BlockStructure;
import elcon.mods.example.ExampleMod;

public class BlockStructureTest extends BlockStructure {

	public BlockStructureTest() {
		super(Material.rock);
		setStepSound(Block.soundTypeStone);
		setCreativeTab(CreativeTabs.tabBlock);
	}
	
	@Override
	public String getStructureName() {
		return ExampleMod.structureTest.name;
	}

	@Override
	public String getUnlocalizedName() {
		return "tile.structureTest.name";
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return Blocks.dropper.getIcon(2, 0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
	}
}
