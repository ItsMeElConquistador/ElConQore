package elcon.mods.elconqore;

import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import elcon.mods.elconqore.lang.LanguageManager;
import elcon.mods.elconqore.network.EQCodec;
import elcon.mods.elconqore.network.EQMessage;
import elcon.mods.elconqore.network.EQPacketHandler;
import elcon.mods.elconqore.network.EQPacketHandlerServer;
import elcon.mods.elconqore.tileentities.TileEntityMetadata.MessageTileMetadata;
import elcon.mods.elconqore.tileentities.TileEntityNBT.MessageTileNBT;
import elcon.mods.elconqore.tileentities.TileEntityOwned.MessageTileOwned;

@Mod(modid = EQReference.MOD_ID, name = EQReference.NAME, version = EQReference.VERSION, acceptedMinecraftVersions = EQReference.MC_VERSION, dependencies = EQReference.DEPENDENCIES)
public class ElConQore {

	@Instance(EQReference.MOD_ID)
	public static ElConQore instance;

	@SidedProxy(clientSide = EQReference.CLIENT_PROXY_CLASS, serverSide = EQReference.SERVER_PROXY_CLASS)
	public static EQCommonProxy proxy;

	public static EQPacketHandler<EQMessage> packetHandler;

	public static Logger log = LogManager.getLogger(EQReference.MOD_ID);

	public ElConQore() {
		if(instance == null) {
			instance = this;
		}
		if(proxy == null) {
			proxy = FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT ? new EQClientProxy() : new EQCommonProxy();
		}
	}

	@EventHandler
	@Subscribe
	public void preInit(FMLPreInitializationEvent event) {
		new EQMod(this, EQReference.VERSION_URL, new EQConfig(event.getSuggestedConfigurationFile()), event.getSourceFile());
	}

	@SuppressWarnings("unchecked")
	@EventHandler
	@Subscribe
	public void init(FMLInitializationEvent event) {
		// load languages
		LanguageManager.setLoaded(false);
		LanguageManager.load();
		addElConQoreLocalizations();

		// excecute version check
		EQVersion.execute();

		// init event handler
		MinecraftForge.EVENT_BUS.register(new EQEventHandler());

		// init packet handler
		packetHandler = new EQPacketHandler<EQMessage>("ElConQore", new EQCodec(MessageTileMetadata.class, MessageTileNBT.class, MessageTileOwned.class));
		ElConQore.packetHandler.setServerHandler(new EQPacketHandlerServer());

		proxy.registerRenderingInformation();
	}

	@EventHandler
	@Subscribe
	public void postInit(FMLPostInitializationEvent event) {

	}

	private void addElConQoreLocalizations() {
		LanguageManager.setLocatization("en_US", "elconqore.version.init_log_message", "Initializing remote version check against remote version authority, located at");
		LanguageManager.setLocatization("en_US", "elconqore.version.uninitialized", "Remote version check failed to initialize properly");
		LanguageManager.setLocatization("en_US", "elconqore.version.current", "Currently using the most up to date version (@REMOTE_MOD_VERSION@) of @MOD_NAME@ for @MINECRAFT_VERSION@");
		LanguageManager.setLocatization("en_US", "elconqore.version.outdated", "A new @MOD_NAME@ version exists (@REMOTE_MOD_VERSION@) for @MINECRAFT_VERSION@. Get it here: @MOD_UPDATE_LOCATION@");
		LanguageManager.setLocatization("en_US", "elconqore.version.general_error", "Error while connecting to remote version authority file; trying again");
		LanguageManager.setLocatization("en_US", "elconqore.version.final_error", "Version check stopping after three unsuccessful connection attempts");
		LanguageManager.setLocatization("en_US", "elconqore.version.mc_version_not_found", "Unable to find a version of @MOD_NAME@ for @MINECRAFT_VERSION@ in the remote version authority");
		LanguageManager.setLocatization("en_US", "elconqore.version.chat_hover", "Click this to go the update page");
	}
}
