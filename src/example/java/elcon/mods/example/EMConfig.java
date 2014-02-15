package elcon.mods.example;

import java.io.File;

import elcon.mods.elconqore.EQConfig;

public class EMConfig extends EQConfig {

	public static int launchCount = 0; 
	
	public EMConfig(File config) {
		super(config);
	}
	
	@Override
	public void load() {
		super.load();
		launchCount = config.get(CATEGORY_GENERAL, "launchCount", 0).getInt();
		launchCount++;
		set(CATEGORY_GENERAL, "launchCount", launchCount);
	}
}
