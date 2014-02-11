package elcon.mods.elconqore;

import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EQEventHandler {

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		if(!event.world.isRemote) {
			EQSaveHandler sm = new EQSaveHandler(event.world.getSaveHandler(), event.world);
			sm.load();
		}
	}

	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save event) {
		if(!event.world.isRemote) {
			EQSaveHandler sm = new EQSaveHandler(event.world.getSaveHandler(), event.world);
			sm.save();
		}		
	}
}
