package elcon.mods.elconqore.asm;

import java.io.InputStream;
import java.util.HashMap;

import net.minecraft.launchwrapper.IClassTransformer;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import elcon.mods.elconqore.ElConQore;

public class EQPatchTransformer implements IClassTransformer {

	public static HashMap<String, byte[]> patches = new HashMap<String, byte[]>();

	public static void setup() {
		try {
			InputStream inputStream = EQPatchTransformer.class.getClass().getResourceAsStream("/patches.lzma");
			if(inputStream == null) {
				ElConQore.log.error("The patches are missing. Expect severe problems!");
				return;
			}
			byte[] bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
			ByteArrayDataInput dat = ByteStreams.newDataInput(bytes);
			int patchSize = dat.readInt();
			for(int i = 0; i < patchSize; i++) {
				String patchName = dat.readUTF();
				byte[] patchBytes = new byte[dat.readInt()];
				dat.readFully(bytes);
				patches.put(patchName, patchBytes);
				ElConQore.log.info("Read patch for class: " + patchName);
			}
			ElConQore.log.info("Read " + patches.size() + " patches: ");
			ElConQore.log.info(patches.keySet());
			inputStream.close();
		} catch(Exception e) {
			ElConQore.log.error("Error occurred reading patches. Expect severe problems!");
			e.printStackTrace();
		}
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if(bytes == null) {
			return null;
		}
		if(patches.containsKey(transformedName)) {
			ElConQore.log.info("Patches class: " + transformedName);
			return patches.get(transformedName);
		}
		return bytes;
	}
}
