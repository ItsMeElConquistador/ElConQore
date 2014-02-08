package elcon.mods.elconqore.asm;

import java.util.Arrays;

import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import elcon.mods.elconqore.EQReference;
import elcon.mods.elconqore.ElConQore;

public class EQModContainer extends DummyModContainer {

	public EQModContainer() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = EQReference.MOD_ID;
		meta.name = EQReference.NAME;
		meta.version = EQReference.VERSION;
		meta.authorList = Arrays.asList("ElConquistador");
		meta.description = "Core mod for all mods made by ElConquistador";
		meta.url = "https://github.com/ItsMeElConquistador/ElConQore";
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(new ElConQore());
		return true;
	}
}
