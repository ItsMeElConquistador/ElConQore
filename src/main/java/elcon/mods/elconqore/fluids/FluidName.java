package elcon.mods.elconqore.fluids;

import net.minecraftforge.fluids.Fluid;
import elcon.mods.elconqore.lang.LanguageManager;

public class FluidName extends Fluid {

	public FluidName(String fluidName) {
		super(fluidName);
	}
	
	@Override
	public String getLocalizedName() {
		return LanguageManager.getLocalization(getUnlocalizedName());
	}
}
