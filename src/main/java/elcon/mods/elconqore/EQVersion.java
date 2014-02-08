package elcon.mods.elconqore;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.Loader;
import elcon.mods.elconqore.lang.LanguageManager;

public class EQVersion implements Runnable {

	private static final String VERSION_CHECK_INIT_LOG_MESSAGE = "elconqore.version.init_log_message";
	private static final String UNINITIALIZED_MESSAGE = "elconqore.version.uninitialized";
	private static final String CURRENT_MESSAGE = "elconqore.version.current";
	private static final String OUTDATED_MESSAGE = "elconqore.version.outdated";
	private static final String GENERAL_ERROR_MESSAGE = "elconqore.version.general_error";
	private static final String FINAL_ERROR_MESSAGE = "elconqore.version.final_error";
	private static final String MC_VERSION_NOT_FOUND_MESSAGE = "elconqore.version.mc_version_not_found";
	
	private static final int VERSION_CHECK_ATTEMPTS = 3;
	
	private static EQVersion instance = new EQVersion();

	public static final byte UNINITIALIZED = 0;
	public static final byte CURRENT = 1;
	public static final byte OUTDATED = 2;
	public static final byte ERROR = 3;
	public static final byte FINAL_ERROR = 4;
	public static final byte MC_VERSION_NOT_FOUND = 5;
	
	public static Properties remoteVersionProperties = new Properties();

	public static void checkVersion(EQMod mod) {
		InputStream remoteVersionRepoStream = null;
		mod.versionResult = UNINITIALIZED;
		try {
			URL remoteVersionURL = new URL(mod.versionURL);
			remoteVersionRepoStream = remoteVersionURL.openStream();
			remoteVersionProperties.loadFromXML(remoteVersionRepoStream);
			String remoteVersionProperty = remoteVersionProperties.getProperty(Loader.instance().getMCVersionString());

			if(remoteVersionProperty != null) {
				String[] remoteVersionTokens = remoteVersionProperty.split("\\|");
				if(remoteVersionTokens.length >= 2) {
					mod.remoteVersion = remoteVersionTokens[0];
					mod.remoteUpdateLocation = remoteVersionTokens[1];
				} else {
					mod.versionResult = ERROR;
				}
				if(mod.remoteVersion != null) {
					if(!mod.config.lastDiscoveredVersion.equalsIgnoreCase(mod.remoteVersion)) {
						mod.config.set(EQConfig.CATEGORY_VERSION, "last_discovered_version", mod.remoteVersion);
					}					
					if(mod.remoteVersion.equalsIgnoreCase(getVersionForCheck(mod))) {
						mod.versionResult = CURRENT;
					} else {
						mod.versionResult = OUTDATED;
					}
				}
			} else {
				mod.versionResult = MC_VERSION_NOT_FOUND;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(mod.versionResult == UNINITIALIZED) {
				mod.versionResult = ERROR;
			}
			try {
				if(remoteVersionRepoStream != null) {
					remoteVersionRepoStream.close();
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private static String getVersionForCheck(EQMod mod) {
		String[] versionTokens = mod.mod.version().split(" ");

		if(versionTokens.length >= 1) {
			return versionTokens[0];
		} else {
			return mod.mod.version();
		}
	}

	public static void logResult(EQMod mod) {
		if(mod.versionResult == CURRENT || mod.versionResult == OUTDATED) {
			ElConQore.log.info(getResultMessage(mod));
		} else {
			ElConQore.log.warn(getResultMessage(mod));
		}
	}

	public static String getResultMessage(EQMod mod) {
		if(mod.versionResult == UNINITIALIZED) {
			return LanguageManager.getLocalization(UNINITIALIZED_MESSAGE);
		} else if(mod.versionResult == CURRENT) {
			String returnString = LanguageManager.getLocalization(CURRENT_MESSAGE);
			returnString = returnString.replace("@MOD_NAME@", mod.mod.name());
			returnString = returnString.replace("@REMOTE_MOD_VERSION@", mod.remoteVersion);
			returnString = returnString.replace("@MINECRAFT_VERSION@", Loader.instance().getMCVersionString());
			return returnString;
		} else if(mod.versionResult == OUTDATED && mod.remoteVersion != null && mod.remoteUpdateLocation != null) {
			String returnString = LanguageManager.getLocalization(OUTDATED_MESSAGE);
			returnString = returnString.replace("@MOD_NAME@", mod.mod.name());
			returnString = returnString.replace("@REMOTE_MOD_VERSION@", mod.remoteVersion);
			returnString = returnString.replace("@MINECRAFT_VERSION@", Loader.instance().getMCVersionString());
			returnString = returnString.replace("@MOD_UPDATE_LOCATION@", mod.remoteUpdateLocation);
			return returnString;
		} else if(mod.versionResult == OUTDATED && mod.remoteVersion != null && mod.remoteUpdateLocation != null) {
			String returnString = LanguageManager.getLocalization(OUTDATED_MESSAGE);
			returnString = returnString.replace("@MOD_NAME@", mod.mod.name());
			returnString = returnString.replace("@REMOTE_MOD_VERSION@", mod.remoteVersion);
			returnString = returnString.replace("@MINECRAFT_VERSION@", Loader.instance().getMCVersionString());
			returnString = returnString.replace("@MOD_UPDATE_LOCATION@", mod.remoteUpdateLocation);
			return returnString;
		} else if(mod.versionResult == ERROR) { 
			return LanguageManager.getLocalization(GENERAL_ERROR_MESSAGE);
		} else if(mod.versionResult == FINAL_ERROR) {
			return LanguageManager.getLocalization(FINAL_ERROR_MESSAGE);
		} else if(mod.versionResult == MC_VERSION_NOT_FOUND) {
			String returnString = LanguageManager.getLocalization(MC_VERSION_NOT_FOUND_MESSAGE);
			returnString = returnString.replace("@MOD_NAME@", mod.mod.name());
			returnString = returnString.replace("@MINECRAFT_VERSION@", Loader.instance().getMCVersionString());
			return returnString;
		} else {
			mod.versionResult = ERROR;
			return LanguageManager.getLocalization(GENERAL_ERROR_MESSAGE);
		}
	}

	public static String getResultMessageForClient(EQMod mod) {
		String returnString = LanguageManager.getLocalization(OUTDATED_MESSAGE);
		returnString = returnString.replace("@MOD_NAME@", EnumChatFormatting.YELLOW + mod.mod.name() + EnumChatFormatting.WHITE);
		returnString = returnString.replace("@REMOTE_MOD_VERSION@", EnumChatFormatting.YELLOW + mod.remoteVersion + EnumChatFormatting.WHITE);
		returnString = returnString.replace("@MINECRAFT_VERSION@", EnumChatFormatting.YELLOW + Loader.instance().getMCVersionString() + EnumChatFormatting.WHITE);
		returnString = returnString.replace("@MOD_UPDATE_LOCATION@", EnumChatFormatting.YELLOW + mod.remoteUpdateLocation + EnumChatFormatting.WHITE);
		return returnString;
	}

	public static byte getResult(EQMod mod) {
		return mod.versionResult;
	}

	@Override
	public void run() {
		int count = 0;
		for(EQMod mod : EQMod.mods.values()) {
			ElConQore.log.info(LanguageManager.getLocalization(VERSION_CHECK_INIT_LOG_MESSAGE) + " " + mod.versionURL);
			
			remoteVersionProperties = new Properties();
			try {
				while(count < VERSION_CHECK_ATTEMPTS - 1 && (mod.versionResult == UNINITIALIZED || mod.versionResult == ERROR)) {
					checkVersion(mod);
					count++;
					logResult(mod);
					if(mod.versionResult == UNINITIALIZED || mod.versionResult == ERROR) {
						Thread.sleep(10000);
					}
				}	
				if(mod.versionResult == ERROR) {
					mod.versionResult = FINAL_ERROR;
					logResult(mod);
				}
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			count = 0;
		}
	}

	public static void execute() {
		new Thread(instance).start();
	}
}
