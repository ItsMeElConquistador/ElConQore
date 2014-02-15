package elcon.mods.example;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import elcon.mods.elconqore.EQMod;
import elcon.mods.elconqore.network.EQCodec;
import elcon.mods.elconqore.network.EQMessage;
import elcon.mods.elconqore.network.EQPacketHandler;
import elcon.mods.elconqore.network.EQPacketHandlerServer;

@Mod(modid = EMReference.MOD_ID, name = EMReference.NAME, version = EMReference.VERSION, acceptedMinecraftVersions = EMReference.MC_VERSION, dependencies = EMReference.DEPENDENCIES)
public class ExampleMod {

	@Instance(EMReference.MOD_ID)
	public static ExampleMod instance;
	
	@SidedProxy(clientSide = EMReference.CLIENT_PROXY_CLASS, serverSide = EMReference.SERVER_PROXY_CLASS)
	public static EMCommonProxy proxy;
	
	public static EQPacketHandler<EQMessage> packetHandler;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		new EQMod(this, EMReference.VERSION_URL, new EMConfig(event.getSuggestedConfigurationFile()), event.getSourceFile());
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		//init packet handler
		packetHandler = new EQPacketHandler<EQMessage>(EMReference.MOD_ID, new EQCodec());
		packetHandler.setServerHandler(new EQPacketHandlerServer());
		
		proxy.registerRenderingInformation();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}
}
