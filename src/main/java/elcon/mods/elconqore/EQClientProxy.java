package elcon.mods.elconqore;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EQClientProxy extends EQCommonProxy {
	
	@Override
	public void registerRenderingInformation() {
		//register tick handler
		FMLCommonHandler.instance().bus().register(new EQTickHandlerClient());
	}
}
