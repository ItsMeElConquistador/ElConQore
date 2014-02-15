package elcon.mods.example;

import elcon.mods.elconqore.ElConQore;
import elcon.mods.elconqore.network.EQPacketHandlerClient;

public class EMClientProxy extends EMCommonProxy {

	@Override
	public void registerRenderingInformation() {
		//init packet handler
		ElConQore.packetHandler.setClientHandler(new EQPacketHandlerClient());
	}
}
