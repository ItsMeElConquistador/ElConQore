package elcon.mods.elconqore.asm.patcher;

public class Patch {

	public final String name;
	public final String sourceClassName;
	public final String targetClassName;
	public final boolean existsAtTarget;
	public final byte[] patch;
	public final int inputChecksum;

	public Patch(String name, String sourceClassName, String targetClassName, boolean existsAtTarget, int inputChecksum, byte[] patch) {
		this.name = name;
		this.sourceClassName = sourceClassName;
		this.targetClassName = targetClassName;
		this.existsAtTarget = existsAtTarget;
		this.inputChecksum = inputChecksum;
		this.patch = patch;
	}

	@Override
	public String toString() {
		return String.format("%s : %s => %s (%b) size %d", name, sourceClassName, targetClassName, existsAtTarget, patch.length);
	}
}
