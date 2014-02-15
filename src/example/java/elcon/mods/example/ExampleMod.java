package elcon.mods.example;

import net.minecraft.block.Block;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import elcon.mods.elconqore.EQMod;
import elcon.mods.elconqore.items.ItemBlockExtendedMetadata;
import elcon.mods.elconqore.items.ItemBlockName;
import elcon.mods.elconqore.network.EQCodec;
import elcon.mods.elconqore.network.EQMessage;
import elcon.mods.elconqore.network.EQPacketHandler;
import elcon.mods.elconqore.network.EQPacketHandlerServer;
import elcon.mods.elconqore.structure.MBStructureRegistry;
import elcon.mods.example.blocks.BlockColoredWool;
import elcon.mods.example.blocks.BlockStructureTest;
import elcon.mods.example.structure.MBStructureTest;

@Mod(modid = EMReference.MOD_ID, name = EMReference.NAME, version = EMReference.VERSION, acceptedMinecraftVersions = EMReference.MC_VERSION, dependencies = EMReference.DEPENDENCIES)
public class ExampleMod {

	@Instance(EMReference.MOD_ID)
	public static ExampleMod instance;
	
	@SidedProxy(clientSide = EMReference.CLIENT_PROXY_CLASS, serverSide = EMReference.SERVER_PROXY_CLASS)
	public static EMCommonProxy proxy;
	
	public static EQPacketHandler<EQMessage> packetHandler;
	
	public static MBStructureTest structureTest;
	
	public static Block coloredWool;
	public static Block structureTestBlock;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		new EQMod(this, EMReference.VERSION_URL, new EMConfig(event.getSuggestedConfigurationFile()), event.getSourceFile());
		
		//init blocks
		coloredWool = new BlockColoredWool().setBlockName("EM_coloredWool");
		structureTestBlock = new BlockStructureTest().setBlockName("EM_structureTest");
		
		//register blocks
		GameRegistry.registerBlock(coloredWool, ItemBlockExtendedMetadata.class, "EM_coloredWool");
		GameRegistry.registerBlock(structureTestBlock, ItemBlockName.class, "EM_structureTest");
		
		//init structures
		structureTest = new MBStructureTest();
		
		//register structures
		MBStructureRegistry.registerStructure(structureTest);
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
