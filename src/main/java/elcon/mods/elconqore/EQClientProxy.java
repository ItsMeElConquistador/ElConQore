package elcon.mods.elconqore;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elcon.mods.elconqore.entities.EntityBlock;
import elcon.mods.elconqore.network.EQPacketHandlerClient;
import elcon.mods.elconqore.render.EQBlockRenderingHandler;
import elcon.mods.elconqore.render.RenderEntityBlock;

@SideOnly(Side.CLIENT)
public class EQClientProxy extends EQCommonProxy {

	@Override
	public void registerRenderingInformation() {
		// register tick handler
		FMLCommonHandler.instance().bus().register(new EQTickHandlerClient());

		// init event handler
		MinecraftForge.EVENT_BUS.register(new EQEventHandlerClient());
		
		// init packet handler
		ElConQore.packetHandler.setClientHandler(new EQPacketHandlerClient());
		
		//register block rendering handlers
		EQConfig.BLOCK_OVERLAY_RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
		EQConfig.BLOCK_FLUID_RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(EQConfig.BLOCK_OVERLAY_RENDER_ID, new EQBlockRenderingHandler());
		RenderingRegistry.registerBlockHandler(EQConfig.BLOCK_FLUID_RENDER_ID, new EQBlockRenderingHandler());
		
		//register entity rendering handlers
		RenderingRegistry.registerEntityRenderingHandler(EntityBlock.class, RenderEntityBlock.instance);
	}
}
