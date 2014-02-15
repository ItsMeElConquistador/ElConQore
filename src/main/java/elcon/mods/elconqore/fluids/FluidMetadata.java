package elcon.mods.elconqore.fluids;

public class FluidMetadata extends FluidName {

	public int metadata;
	
	public FluidMetadata(String fluidName, int metadata) {
		super(fluidName);
		this.metadata = metadata;
	}
}
