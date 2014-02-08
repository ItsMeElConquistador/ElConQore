package elcon.mods.elconqore.player;

import java.lang.reflect.Constructor;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.world.WorldServer;

import com.mojang.authlib.GameProfile;

public class PlayerCoreServer extends EntityPlayerMP {

	protected PlayerCoreServer nextPlayerCore;
	protected PlayerCoreServer player;
	private boolean shouldCallSuper;
	private boolean init = false;

	public PlayerCoreServer(MinecraftServer mcServer, WorldServer worldServer, GameProfile gameProfile, ItemInWorldManager itemInWorldManager) {
		this(mcServer, worldServer, gameProfile, itemInWorldManager, 0, null);
	}

	public PlayerCoreServer(MinecraftServer mcServer, WorldServer worldServer, GameProfile gameProfile, ItemInWorldManager itemInWorldManager, int playerCoreIndex, PlayerCoreServer entityPlayerMP) {
		super(mcServer, worldServer, gameProfile, itemInWorldManager);
		player = (entityPlayerMP == null ? this : entityPlayerMP);
		if(playerCoreIndex < PlayerAPI.playerCoreServerList.size()) {
			Class<? extends PlayerCoreServer> nextPlayerCoreClass = PlayerAPI.playerCoreServerList.get(playerCoreIndex);
			try {
				Constructor<? extends PlayerCoreServer> constructor = nextPlayerCoreClass.getConstructor(new Class<?>[]{MinecraftServer.class, WorldServer.class, GameProfile.class, ItemInWorldManager.class, Integer.TYPE, PlayerCoreRender.class});
				nextPlayerCore = (constructor.newInstance(new Object[]{mcServer, worldServer, gameProfile, itemInWorldManager, Integer.valueOf(playerCoreIndex + 1), player}));
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			nextPlayerCore = player;
		}
		init = true;
	}

	public PlayerCoreServer getPlayerCoreObject(Class<? extends PlayerCoreServer> clazz) {
		if(getClass() == clazz) {
			return this;
		}
		if(nextPlayerCore == player) {
			return null;
		}
		return nextPlayerCore.getPlayerCoreObject(clazz);
	}

	@SuppressWarnings("unused")
	private boolean shouldCallSuper() {
		if(!init) {
			return true;
		}
		if(!shouldCallSuper) {
			if(nextPlayerCore == null) {
				return true;
			}
			nextPlayerCore.shouldCallSuper = (nextPlayerCore == player);
			return false;
		}
		shouldCallSuper = false;
		return true;
	}
}
