package elcon.mods.elconqore;

import java.util.HashMap;

import cpw.mods.fml.common.Mod;

public class EQMod {

	public static HashMap<String, EQMod> mods = new HashMap<String, EQMod>();
	
	public Mod mod;
	public HashMap<String, EQConfig> configs = new HashMap<String, EQConfig>();
	public String versionURL;
	
	public EQMod(Mod mod, String versionURL) {
		this.mod = mod;
		this.versionURL = versionURL;
		mods.put(mod.modid(), this);
	}
	
	public EQMod(Object mod, String versionURL) {
		if(mod instanceof Mod) {
			this.mod = (Mod) mod;
			this.versionURL = versionURL;
			mods.put(this.mod.modid(), this);
		} else {
			ElConQore.log.error(mod + " does not have the @Mod annotation, so can't create an ElConQoreMod for it");
		}
	}
	
	public EQConfig getConfig(String configName) {
		return configs.get(configName);
	}
	
	public void addConfig(String configName, EQConfig config) {
		configs.put(configName, config);
		config.load();
		config.save();
	}
	
	public void removeConfig(String configName) {
		configs.remove(configName);
	}
	
	public void loadConfigs() {
		for(EQConfig config : configs.values()) {
			config.load();
		}
	}
	
	public void saveConfigs() {
		for(EQConfig config : configs.values()) {
			config.save();
		}
	}
}
