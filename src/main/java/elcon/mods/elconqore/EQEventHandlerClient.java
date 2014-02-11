package elcon.mods.elconqore;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.TextureStitchEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import elcon.mods.elconqore.lang.LanguageManager;

public class EQEventHandlerClient {

	@SubscribeEvent
	public void onTextureStitchPost(TextureStitchEvent.Post event) {
		LanguageManager.setLoaded(false);
		LanguageManager.load();
		LanguageManager.setCurrentLanguage(Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode());
	}
}
