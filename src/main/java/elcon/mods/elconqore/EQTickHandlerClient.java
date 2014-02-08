package elcon.mods.elconqore;

import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elcon.mods.elconqore.lang.LanguageManager;

@SideOnly(Side.CLIENT)
public class EQTickHandlerClient {

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		for(EQMod mod : EQMod.mods.values()) {
			if(mod.config.displayVersionResult && !mod.versionMessage && mod.versionResult == EQVersion.OUTDATED) {
				if(FMLClientHandler.instance().getClient().currentScreen == null) {
					if(mod.versionResult != EQVersion.UNINITIALIZED || mod.versionResult != EQVersion.FINAL_ERROR) {
						mod.versionMessage = true;
						if(mod.versionResult == EQVersion.OUTDATED) {
							ChatComponentText chatComponent = new ChatComponentText(EQVersion.getResultMessageForClient(mod));
							chatComponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(LanguageManager.getLocalization("elconqore.version.chat_hover"))));
							chatComponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, mod.remoteUpdateLocation));
							Minecraft.getMinecraft().thePlayer.addChatMessage(chatComponent);
						}
					}
				}
			}
		}
	}
}
