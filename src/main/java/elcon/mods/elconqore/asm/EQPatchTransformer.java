package elcon.mods.elconqore.asm;

import elcon.mods.elconqore.asm.patcher.PatchManager;
import net.minecraft.launchwrapper.IClassTransformer;

public class EQPatchTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		return PatchManager.instance.applyPatch(name, transformedName, bytes);
	}
}
