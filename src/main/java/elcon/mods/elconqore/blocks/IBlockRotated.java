package elcon.mods.elconqore.blocks;

import net.minecraft.world.IBlockAccess;

public interface IBlockRotated {

	public int getBlockRotation(IBlockAccess blockAccess, int x, int y, int z);
}
