package elcon.mods.elconqore;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elcon.mods.elconqore.network.EQPacketHandlerClient;

@SideOnly(Side.CLIENT)
public class EQClientProxy extends EQCommonProxy {

	@Override
	public void registerRenderingInformation() {
		// register tick handler
		FMLCommonHandler.instance().bus().register(new EQTickHandlerClient());

		// init event handler
		MinecraftForge.EVENT_BUS.register(new EQEventHandlerClient());
		
		//set client packet handler
		ElConQore.packetHandler.setClientHandler(new EQPacketHandlerClient());
	}
}
