package elcon.mods.elconqore.lang;

import java.util.HashMap;

public class Language {

	public String name;
	private HashMap<String, String> localizations = new HashMap<String, String>();
	
	public Language(String name) {
		this.name = name;
	}
	
	public String getLocalization(String key) {
		if(localizations.containsKey(key)) {
			return localizations.get(key);
		}
		return key;
	}
	
	public void setLocalization(String key, String localization) {
		localizations.put(key, localization);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
