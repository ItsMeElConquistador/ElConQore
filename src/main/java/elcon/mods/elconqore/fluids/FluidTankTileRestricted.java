package elcon.mods.elconqore.fluids;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidTankTileRestricted extends FluidTankTile {

	private Fluid[] acceptedFluids;
	
	public FluidTankTileRestricted(String name, TileEntity tile, int capacity) {
		super(name, tile, capacity);
	}
	
	public void setAcceptedFluids(Fluid... acceptedFluids) {
		this.acceptedFluids = acceptedFluids;
	}
	
	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if(!acceptsFluid(resource.getFluid())) {
			return 0;
		}
		return super.fill(resource, doFill);
	}
	
	public boolean acceptsFluid(Fluid fluid) {
		for(int i = 0; i < acceptedFluids.length; i++) {
			if(acceptedFluids[i].equals(fluid)) {
				return true;
			}
		}
		return false;
	}
}
