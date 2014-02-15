package elcon.mods.elconqore.blocks;

import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IBlockOverlay {

	@SideOnly(Side.CLIENT)
	public boolean shouldOverlaySideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side);

	@SideOnly(Side.CLIENT)
	public IIcon getBlockOverlayTexture(int side, int metadata);
	
	@SideOnly(Side.CLIENT)
	public IIcon getBlockOverlayTexture(IBlockAccess blockAccess, int x, int y, int z, int side);
	
	@SideOnly(Side.CLIENT)
	public int getBlockOverlayColor(int side, int metadata);
	
	@SideOnly(Side.CLIENT)
	public int getBlockOverlayColor(IBlockAccess blockAccess, int x, int y, int z, int side);
}
