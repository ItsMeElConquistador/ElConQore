package elcon.mods.elconqore.lang;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import elcon.mods.elconqore.EQMod;
import elcon.mods.elconqore.EQUtil;
import elcon.mods.elconqore.ElConQore;

public class LanguageManager {

	private static String currentLanguage = "en_US";
	private static HashMap<String, Language> languages = new HashMap<String, Language>();
	private static boolean loaded = false;
	private static boolean downloaded = false;

	private static final Splitter splitter = Splitter.on('=').limit(2);
	private static final Pattern pattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");

	public static void load() {
		if(!loaded) {
			languages.put("en_US", new Language("en_US"));
			ElConQore.log.info("[LanguageManager] Loading languages...");
			for(String modName : EQMod.mods.keySet()) {
				if(!modName.equals("ElConQore")) {
					ElConQore.log.info("[LanguageManager] Loading languages for mod: " + modName);
					File sourceFile = EQMod.mods.get(modName).sourceFile;
					File languageDirectory = new File(Minecraft.getMinecraft().mcDataDir, "/lang/" + modName.toLowerCase() + "/");
					languageDirectory.delete();
					languageDirectory.mkdirs();
					if(sourceFile.isDirectory()) {
						ElConQore.log.info("[LanguageManager] Copying files from " + new File(sourceFile, "/assets/" + modName.toLowerCase() + "/lang/").getAbsolutePath() + " folder to " + languageDirectory.getAbsolutePath());
						try {
							FileUtils.copyDirectory(new File(sourceFile, "/assets/" + modName.toLowerCase() + "/lang/"), languageDirectory);
						} catch(Exception e) {
							e.printStackTrace();
						}
					} else {
						ElConQore.log.info("[LanguageManager] Copying files from jar (" + sourceFile.getAbsolutePath() + ") to " + languageDirectory.getAbsolutePath());
						try {
							JarFile jar = new JarFile(sourceFile);
							Enumeration<JarEntry> entries = jar.entries();
							while(entries.hasMoreElements()) {
								JarEntry jarEntry = (JarEntry) entries.nextElement();
								if(jarEntry.getName().contains("assets/" + modName.toLowerCase() + "/lang/")) {
									File f = new File(languageDirectory, jarEntry.getName());
									if(jarEntry.isDirectory()) {
										f.mkdirs();
										continue;
									}
									if(!f.exists()) {
										f.createNewFile();
									}
									InputStream is = jar.getInputStream(jarEntry);
									FileOutputStream fos = new FileOutputStream(f);
									while(is.available() > 0) {
										fos.write(is.read());
									}
									fos.close();
									is.close();
								}
							}
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
					if(!downloaded) {
						EQMod mod = EQMod.mods.get(modName);
						for(String localizationURL : mod.localizationURLs) {
							try {
								File file = new File(languageDirectory, modName.toLowerCase() + ".zip");
								FileUtils.copyURLToFile(new URL(localizationURL), file);
								ElConQore.log.info("[LanguageManager] Downloaded localization for " + modName + " from " + localizationURL);
								if(file.exists()) {
									EQUtil.extractZip(file, languageDirectory, true);
									ElConQore.log.info("[LanguageManager] Extracted localization for " + modName);
								}
								file.delete();
							} catch(Exception e) {
								e.printStackTrace();
							}
						}
					}
					searchLanguageDirectory(languageDirectory);
				}
			}
			loaded = true;
			downloaded = true;
		}
	}

	private static void searchLanguageDirectory(File dir) {
		File[] files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isDirectory() || EQUtil.getFileExtension(file).equals("lang");
			}
		});
		for(int i = 0; i < files.length; i++) {
			if(files[i] != null) {
				if(files[i].isDirectory()) {
					searchLanguageDirectory(files[i]);
				} else {
					loadLanguageFile(files[i]);
				}
			}
		}
	}

	public static void loadLanguageFile(File file) {
		String dirName = file.getParentFile().getName();
		String fileName = file.getName();
		try {
			ElConQore.log.info("[LanguageManager] Loading language file: " + dirName + "/" + fileName);
			FileInputStream inputStream = new FileInputStream(file);

			String language = fileName.replace(EQUtil.getFileExtension(fileName), "").replace(".", "");
			if(!languages.containsKey(language)) {
				languages.put(language, new Language(language));
			}
			Iterator<String> iterator = IOUtils.readLines(inputStream, Charsets.UTF_8).iterator();
			while(iterator.hasNext()) {
				String s = (String) iterator.next();
				if(!s.isEmpty() && s.charAt(0) != 35) {
					String[] split = (String[]) Iterables.toArray(splitter.split(s), String.class);

					if(split != null && split.length == 2) {
						String key = split[0];
						String localization = pattern.matcher(split[1]).replaceAll("%$1s");
						setLocatization(language, key, localization);
					}
				}
			}
		} catch(Exception e) {
			ElConQore.log.error("[LanguageManger] Error while reading language file (" + fileName + "): ");
			e.printStackTrace();
		}
	}

	public static String getCurrentLanguage() {
		return currentLanguage;
	}

	public static void setCurrentLanguage(String language) {
		currentLanguage = language;
	}

	public static Language getLanguage() {
		return getLanguage(currentLanguage);
	}

	public static Language getLanguage(String name) {
		if(languages.containsKey(name)) {
			return languages.get(name);
		}
		return languages.get("en_US");
	}

	public static void setLanguage(String name, Language language) {
		languages.put(name, language);
	}

	public static String getLocalization(String key) {
		if(!loaded) {
			return key;
		}
		return getLanguage().getLocalization(key);
	}

	public static void setLocatization(String key, String localization) {
		getLanguage().setLocalization(key, localization);
	}

	public static String getLocalization(String language, String key) {
		if(!loaded) {
			return key;
		}
		return getLanguage(language).getLocalization(key);
	}

	public static void setLocatization(String language, String key, String localization) {
		getLanguage(language).setLocalization(key, localization);
	}

	public static void setLoaded(boolean flag) {
		loaded = flag;
	}
}
