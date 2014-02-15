package elcon.mods.elconqore.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elcon.mods.elconqore.blocks.BlockExtendedMetadata;
import elcon.mods.elconqore.tileentities.TileEntityMetadata;

public class ItemBlockExtendedMetadata extends ItemBlock {

	public ItemBlockExtendedMetadata(Block block) {
		super(block);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return ((BlockExtendedMetadata) field_150939_a).getLocalizedName(stack);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return ((BlockExtendedMetadata) field_150939_a).getUnlocalizedName(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int renderPass) {
		return field_150939_a.getRenderColor(stack.getItemDamage());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return field_150939_a.getIcon(2, meta);
	}

	@Override
	public int getMetadata(int meta) {
		return 0;
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
		byte placedMeta = ((BlockExtendedMetadata) field_150939_a).getPlacedMetadata(player, stack, world, x, y, z, side, hitX, hitY, hitZ);
		if(!world.setBlock(x, y, z, field_150939_a, metadata, 3)) {
			return false;
		}
		if(world.getBlock(x, y, z) == field_150939_a) {
			TileEntity tile = world.getTileEntity(x, y, z);
			if(tile != null) {
				if(!(tile instanceof TileEntityMetadata)) {
					tile = new TileEntityMetadata();
					world.setTileEntity(x, y, z, tile);
				}
				((TileEntityMetadata) tile).setTileMetadata(placedMeta);
			}
			field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
			field_150939_a.onPostBlockPlaced(world, x, y, z, metadata);
		}
		return true;
	}
}
