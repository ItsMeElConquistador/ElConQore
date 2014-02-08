package elcon.mods.elconqore.player;

import java.lang.reflect.Constructor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.Session;
import net.minecraft.world.World;

public class PlayerCoreClient extends EntityClientPlayerMP {

	protected PlayerCoreClient nextPlayerCore;
	protected PlayerCoreClient player;
	private boolean shouldCallSuper;
	private boolean init = false;

	public PlayerCoreClient(Minecraft mc, World world, Session session, NetHandlerPlayClient netHandlerClient, StatFileWriter statFileWriter) {
		this(mc, world, session, netHandlerClient, statFileWriter, 0, null);
	}

	public PlayerCoreClient(Minecraft mc, World world, Session session, NetHandlerPlayClient netHandlerClient, StatFileWriter statFileWriter, int playerCoreIndex, PlayerCoreClient entityPlayerSP) {
		super(mc, world, session, netHandlerClient, statFileWriter);
		player = (entityPlayerSP == null ? this : entityPlayerSP);
		if(playerCoreIndex < PlayerAPI.playerCoreClientList.size()) {
			Class<? extends PlayerCoreClient> nextPlayerCoreClass = PlayerAPI.playerCoreClientList.get(playerCoreIndex);
			try {
				Constructor<? extends PlayerCoreClient> constructor = nextPlayerCoreClass.getConstructor(new Class<?>[]{Minecraft.class, World.class, Session.class, NetHandlerPlayClient.class, StatFileWriter.class, Integer.TYPE, PlayerCoreClient.class});
				nextPlayerCore = (constructor.newInstance(new Object[]{mc, world, session, netHandlerClient, statFileWriter, Integer.valueOf(playerCoreIndex + 1), player}));
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			nextPlayerCore = player;
		}
		init = true;
	}

	public PlayerCoreClient getPlayerCoreObject(Class<? extends PlayerCoreClient> clazz) {
		if(getClass() == clazz) {
			return this;
		}
		if(nextPlayerCore == player) {
			return null;
		}
		return nextPlayerCore.getPlayerCoreObject(clazz);
	}

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
	
	@Override
	public void onUpdate() {
		if(!shouldCallSuper()) {
			nextPlayerCore.onUpdate();
		} else {
			super.onUpdate();
		}
	}
	
	@Override
	public float getFOVMultiplier() {
		if(!shouldCallSuper()) {
			return nextPlayerCore.getFOVMultiplier();
		} else {
			return super.getFOVMultiplier();
		}
	}
}
