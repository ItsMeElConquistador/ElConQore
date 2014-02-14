package elcon.mods.elconqore.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import elcon.mods.elconqore.tileentities.TileEntityMetadata;

public class BlockExtendedMetadata extends BlockExtendedContainer {

	public BlockExtendedMetadata(Material material) {
		super(material);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityMetadata();
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityMetadata.class;
	}
	
	//TODO: this
}
