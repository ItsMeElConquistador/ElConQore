package elcon.mods.elconqore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = EQReference.MOD_ID, name = EQReference.NAME, version = EQReference.VERSION, acceptedMinecraftVersions = EQReference.MC_VERSION, dependencies = EQReference.DEPENDENCIES)
public class ElConQore {

	@Instance(EQReference.MOD_ID)
	public static ElConQore instance;
	
	@SidedProxy(clientSide = EQReference.CLIENT_PROXY_CLASS, serverSide = EQReference.SERVER_PROXY_CLASS)
	public static EQCommonProxy proxy;
	
	public static Logger log = LogManager.getLogger(EQReference.MOD_ID);
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		EQMod mod = new EQMod(this, EQReference.VERSION_URL);
		mod.addConfig("config", new EQConfig(event.getSuggestedConfigurationFile()));
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderingInformation();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}
}
