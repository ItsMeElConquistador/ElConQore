package elcon.mods.elconqore.asm;

import java.util.Arrays;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.ModMetadata;
import elcon.mods.elconqore.EQReference;

public class EQModContainer extends DummyModContainer {

	public EQModContainer() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = "ElConQore-ASM";
		meta.name = "ElConQore-ASM";
		meta.description = "ASM part of ElConQore";
		meta.version = EQReference.VERSION;
		meta.authorList = Arrays.asList("ElConquistador");
	}
}
