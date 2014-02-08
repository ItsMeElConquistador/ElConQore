package elcon.mods.elconqore.player;

import java.lang.reflect.Constructor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class PlayerCoreRender extends RenderPlayer {

	protected PlayerCoreRender nextPlayerCore;
	protected PlayerCoreRender player;
	private boolean shouldCallSuper;
	private boolean init = false;

	public PlayerCoreRender() {
		this(0, null);
	}

	public PlayerCoreRender(int playerCoreIndex, PlayerCoreRender renderPlayer) {
		super();
		player = (renderPlayer == null ? this : renderPlayer);
		if(playerCoreIndex < PlayerAPI.playerCoreRenderList.size()) {
			Class<? extends PlayerCoreRender> nextPlayerCoreClass = PlayerAPI.playerCoreRenderList.get(playerCoreIndex);
			try {
				Constructor<? extends PlayerCoreRender> constructor = nextPlayerCoreClass.getConstructor(new Class<?>[]{Integer.TYPE, PlayerCoreRender.class});
				nextPlayerCore = (constructor.newInstance(new Object[]{Integer.valueOf(playerCoreIndex + 1), player}));
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			nextPlayerCore = player;
		}
		init = true;
	}

	public PlayerCoreRender getPlayerCoreObject(Class<? extends PlayerCoreRender> clazz) {
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
	protected void bindTexture(ResourceLocation par1ResourceLocation) {
		if(player.renderManager != null && player.renderManager.renderEngine != null) {
			player.renderManager.renderEngine.bindTexture(par1ResourceLocation);
		}
	}
	
	@Override
	protected void bindEntityTexture(Entity par1Entity) {
		if(!shouldCallSuper()) {
			nextPlayerCore.bindEntityTexture(par1Entity);
		} else {
			super.bindEntityTexture(par1Entity);
		}
	}
	
	@Override
	protected void passSpecialRender(EntityLivingBase entity, double x, double y, double z) {
		if(!shouldCallSuper()) {
			nextPlayerCore.passSpecialRender(entity, x, y, z);
		} else {
			super.passSpecialRender(entity, x, y, z);
		}
	}
	
	@Override
	protected void renderModel(EntityLivingBase entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		if(!shouldCallSuper()) {
			nextPlayerCore.renderModel(entity, par2, par3, par4, par5, par6, par7);
		} else {
			super.renderModel(entity, par2, par3, par4, par5, par6, par7);
		}
	}
	
	@Override
	public void renderFirstPersonArm(EntityPlayer player) {
		if(!shouldCallSuper()) {
			nextPlayerCore.renderFirstPersonArm(player);
		} else {
			super.renderFirstPersonArm(player);
		}
	}

	@Override
	public void setRenderManager(RenderManager renderManager) {
		if(!shouldCallSuper()) {
			nextPlayerCore.setRenderManager(renderManager);
		} else {
			super.setRenderManager(renderManager);
		}
	}	

	public RenderManager getRenderManager() {
		return renderManager;
	}

	public ModelBiped getModelBipedMain() {
		return (ModelBiped) mainModel;
	}
}
