package elcon.mods.elconqore.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elcon.mods.elconqore.EQConfig;

public abstract class BlockExtendedMetadataOverlay extends BlockExtendedMetadata implements IBlockOverlay {

	public BlockExtendedMetadataOverlay(Material material) {
		super(material);
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return EQConfig.BLOCK_OVERLAY_RENDER_ID;
	}	
	
	@Override
	public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
		return getMaterial().isOpaque() && isOpaqueCube() && !canProvidePower();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldOverlaySideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getBlockOverlayTexture(int side, int metadata) {
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getBlockOverlayTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		return getBlockOverlayTexture(side, getMetadata(blockAccess, x, y, z));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockOverlayColor(int side, int metadata) {
		return 0xFFFFFF;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockOverlayColor(IBlockAccess blockAccess, int x, int y, int z, int side) {
		return getBlockOverlayColor(side, getMetadata(blockAccess, x, y, z));
	}
}
