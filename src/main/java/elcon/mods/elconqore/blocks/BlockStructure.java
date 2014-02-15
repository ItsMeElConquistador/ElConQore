package elcon.mods.elconqore.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import elcon.mods.elconqore.tileentities.TileEntityStructure;

public abstract class BlockStructure extends BlockExtendedContainer {

	public BlockStructure(Material material) {
		super(material);
		setTickRandomly(true);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityStructure();
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityStructure.class;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		if(!world.isRemote) {
			TileEntityStructure tile = (TileEntityStructure) getTileEntity(world, x, y, z);
			if(tile != null && tile.hasStructure() && !tile.isMaster()) {
				TileEntityStructure tileCenter = tile.getStructureCenter();
				if(tileCenter != null) {
					tileCenter.validateStructure();
				}
			}
		}
		super.breakBlock(world, x, y, z, block, meta);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		if(!world.isRemote) {
			TileEntity tile = world.getTileEntity(x, y, z);
			if(tile instanceof TileEntityStructure) {
				((TileEntityStructure) tile).validateStructure();
			}
		}
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		if(!world.isRemote) {
			TileEntity tile = world.getTileEntity(x, y, z);
			if(tile instanceof TileEntityStructure) {
				((TileEntityStructure) tile).validateStructure();
			}
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) {
			TileEntityStructure tile = (TileEntityStructure) world.getTileEntity(x, y, z);
			tile.validateStructure();
		}
		return true;
	}
	
	public void onStructureReset(World world, int x, int y, int z) {
	}
	
	public abstract String getStructureName();
}
