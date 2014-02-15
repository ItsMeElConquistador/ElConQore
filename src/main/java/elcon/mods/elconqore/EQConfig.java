package elcon.mods.elconqore;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class EQConfig {

	public static final String CATEGORY_GENERAL = "general";
	public static final String CATEGORY_VERSION = "version";
	public static final String CATEGORY_BLOCK = "block";
	public static final String CATEGORY_ITEM = "item";
	
	public Configuration config;
	
	public boolean displayVersionResult = true;
	public String lastDiscoveredVersion = "";
	public String lastDiscoveredVersionType = "";
	
	public static int BLOCK_OVERLAY_RENDER_ID;
	public static int BLOCK_FLUID_RENDER_ID;
	
	public EQConfig(Configuration config) {
		this.config = config;
	}
	
	public EQConfig(File config) {
		this(new Configuration(config));
	}
	
	public void load() {
		config.load();
	}
	
	public void save() {
		config.save();
	}
	
	public Property get(String category, String key) {
		if(config.getCategoryNames().contains(category)) {
			if(config.getCategory(category).containsKey(category)) {
				return config.getCategory(category).get(key);
			}
		}
		return null;
	}
	
	public void set(String category, String key, String value) {
		if(config.getCategoryNames().contains(category)) {
			if(config.getCategory(category).containsKey(category)) {
				config.getCategory(category).get(key).set(value);
			}
		} else {
			config.get(category, key, value);
		}
		config.save();
	}
	
	public void set(String category, String key, boolean value) {
		if(config.getCategoryNames().contains(category)) {
			if(config.getCategory(category).containsKey(category)) {
				config.getCategory(category).get(key).set(value);
			}
		} else {
			config.get(category, key, value);
		}
		config.save();
	}
	
	public void set(String category, String key, int value) {
		if(config.getCategoryNames().contains(category)) {
			if(config.getCategory(category).containsKey(category)) {
				config.getCategory(category).get(key).set(value);
			}
		} else {
			config.get(category, key, value);
		}
		config.save();
	}
	
	public void set(String category, String key, double value) {
		if(config.getCategoryNames().contains(category)) {
			if(config.getCategory(category).containsKey(category)) {
				config.getCategory(category).get(key).set(value);
			}
		} else {
			config.get(category, key, value);
		}
		config.save();
	}
}
