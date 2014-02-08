package elcon.mods.elconqore.player;

import java.util.ArrayList;

public class PlayerAPI {

	public static enum PlayerCoreType {
		CLIENT, 
		SERVER, 
		RENDER;
	}
	
	public static ArrayList<Class<? extends PlayerCoreClient>> playerCoreClientList = new ArrayList<Class<? extends PlayerCoreClient>>();
	public static ArrayList<Class<? extends PlayerCoreServer>> playerCoreServerList = new ArrayList<Class<? extends PlayerCoreServer>>();
	public static ArrayList<Class<? extends PlayerCoreRender>> playerCoreRenderList = new ArrayList<Class<? extends PlayerCoreRender>>();

	@SuppressWarnings("unchecked")
	public static void register(PlayerCoreType type, Class<?> clazz) {
		switch(type) {
		case CLIENT:
			playerCoreClientList.add((Class<? extends PlayerCoreClient>) clazz);
			break;
		case SERVER:
			playerCoreServerList.add((Class<? extends PlayerCoreServer>) clazz);
			break;
		case RENDER:
			playerCoreRenderList.add((Class<? extends PlayerCoreRender>) clazz);
			break;
		}
	}
}
