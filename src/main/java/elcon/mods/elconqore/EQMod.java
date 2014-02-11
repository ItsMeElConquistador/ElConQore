package elcon.mods.elconqore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import cpw.mods.fml.common.Mod;

public class EQMod {

	public static HashMap<String, EQMod> mods = new HashMap<String, EQMod>();
	
	public Mod mod;
	public String versionURL;
	public EQConfig config;
	public HashMap<String, EQConfig> configs = new HashMap<String, EQConfig>();
	public File sourceFile;
	public IEQSaveHandler saveHandler;
	
	public ArrayList<String> localizationURLs = new ArrayList<String>();
	
	public byte versionResult;
	public boolean versionMessage = false;
	public String remoteVersion;
	public String remoteUpdateLocation;
	
	public EQMod(Object mod, String versionURL, EQConfig config, File sourceFile) {
		if(mod.getClass().getAnnotation(Mod.class) != null) {
			this.mod = mod.getClass().getAnnotation(Mod.class);
			this.versionURL = versionURL;
			this.config = config;
			this.sourceFile = sourceFile;
			
			config.load();
			config.save();
			
			mods.put(this.mod.modid(), this);
		} else {
			ElConQore.log.error(mod + " does not have the @Mod annotation, so can't create an ElConQoreMod for it");
		}
	}
	
	public EQMod(Object mod, String versionURL, EQConfig config, File sourceFile, IEQSaveHandler saveHandler) {
		this(mod, versionURL, config, sourceFile);
		this.saveHandler = saveHandler;
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
		config.load();
		for(EQConfig config : configs.values()) {
			config.load();
		}
	}
	
	public void saveConfigs() {
		config.save();
		for(EQConfig config : configs.values()) {
			config.save();
		}
	}
}
