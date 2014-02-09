package elcon.mods.elconqore.asm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import elcon.mods.elconqore.ElConQore;

@TransformerExclusions({"elcon.mods.elconqore.asm"})
@MCVersion("1.7.2")
public class EQLoadingPlugin implements IFMLLoadingPlugin, IFMLCallHook {

	public static final String mcVersion = "[1.7.2]";
	public static String currentMcVersion;
	public static File minecraftDir;
	public static File location;

	public EQLoadingPlugin() {
		if(minecraftDir != null) {
			return;
		}
		minecraftDir = (File) FMLInjectionData.data()[6];
		currentMcVersion = (String) FMLInjectionData.data()[4];
	}
	
	@Override
	public String getAccessTransformerClass() {
		return "elcon.mods.elconqore.asm.EQAccessTransformer";
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{
			"elcon.mods.elconqore.asm.EQInstanceTransformer",
			"elcon.mods.elconqore.asm.EQHookTransformer"
		};
	}

	@Override
	public String getModContainerClass() {
		return "elcon.mods.elconqore.asm.EQModContainer";
	}

	@Override
	public String getSetupClass() {
		return "elcon.mods.elconqore.asm.EQLoadingPlugin";
	}

	@Override
	public void injectData(Map<String, Object> data) {
		if(data.containsKey("mcLocation")) {
			minecraftDir = (File) data.get("mcLocation");
		}
		if(data.containsKey("coremodLocation")) {
			Object localObject = data.get("coremodLocation");
			if((localObject instanceof File)) {
				location = (File) localObject;
			}
		}
	}

	@Override
	public Void call() throws Exception {
		EQAccessTransformer.addTransformerMap("elconqore_at.cfg");
		EQHookTransformer.readHooksFile("elconqore_hooks.cfg");
		scanElConQoreMods();
		return null;
	}

	private void scanElConQoreMods() {
		ElConQore.log.info("[AccessTransformer] Scanning for mods that use ElConQore");
		File modsDir = new File(minecraftDir, "mods");
		for(File file : modsDir.listFiles()) {
			scanMod(file);
		}
		File versionModsDir = new File(minecraftDir, "mods/" + currentMcVersion);
		if(versionModsDir.exists()) {
			for(File file : versionModsDir.listFiles()) {
				scanMod(file);
			}
		}
	}

	private void scanMod(File file) {
		if(file.getName().endsWith(".jar") || file.getName().endsWith(".zip")) {
			try {
				ElConQore.log.info("[AccessTransformer] Scanning mod: " + file);
				JarFile jar = new JarFile(file);
				try {
					Manifest manifest = jar.getManifest();
					if(manifest == null)
						return;
					Attributes attr = manifest.getMainAttributes();
					if(attr == null) {
						return;
					}
					String mapFile = attr.getValue("AccessTransformer");
					if(mapFile != null) {
						File temp = extractTemp(jar, mapFile);
						ElConQore.log.info("[AccessTransformer] Adding AccessTransformer file: " + mapFile);
						EQAccessTransformer.addTransformerMap(temp.getPath());
						temp.delete();
					}
					String hookFile = attr.getValue("Hooks");
					if(hookFile != null) {
						File temp = extractTemp(jar, hookFile);
						ElConQore.log.info("[HookAPI] Adding hook file: " + hookFile);
						EQHookTransformer.readHooksFile(temp.getPath());
						temp.delete();
					}
				} finally {
					jar.close();
				}
				jar.close();
			} catch(Exception e) {
				ElConQore.log.error("[AccessTransformer] Failed to read jar file (" + file.getName() + "): ");
				e.printStackTrace();
			}
		}
	}

	private File extractTemp(JarFile jar, String mapFile) {
		try {
			File temp = new File(mapFile + ".temp");
			if(!temp.exists()) {
				temp.createNewFile();
			}
			FileOutputStream fout = new FileOutputStream(temp);
			byte[] data = new byte[4096];
			int read = 0;
			InputStream fin = jar.getInputStream(jar.getEntry(mapFile));
			while((read = fin.read(data)) > 0) {
				fout.write(data, 0, read);
			}
			fin.close();
			fout.close();
			return temp;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
