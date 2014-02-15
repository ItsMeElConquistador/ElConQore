package elcon.mods.elconqore.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import elcon.mods.elconqore.tileentities.TileEntityOwned;

public abstract class BlockOwned extends BlockExtendedContainer {

	public BlockOwned(Material material) {
		super(material);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityOwned();
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityOwned.class;
	}
	
	public String getOwner(World world, int x, int y, int z) {
		return ((TileEntityOwned) getTileEntity(world, x, y, z)).getOwner();
	}
	
	public boolean isOwner(World world, int x, int y, int z, String name) {
		return ((TileEntityOwned) getTileEntity(world, x, y, z)).isOwner(name);
	}
	
	public void setOwner(World world, int x, int y, int z, String name) {
		((TileEntityOwned) getTileEntity(world, x, y, z)).setOwner(name);
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		TileEntityOwned tile = (TileEntityOwned) getTileEntity(world, x, y, z);
		tile.setOwner(entity.getCommandSenderName());
	}
}
