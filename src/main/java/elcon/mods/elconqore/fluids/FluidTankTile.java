package elcon.mods.elconqore.fluids;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class FluidTankTile extends FluidTank {

	public String name;

	public FluidTankTile(String name, TileEntity tile, int capacity) {
		super(capacity);
		this.name = name;
		this.tile = tile;
	}

	public FluidTankTile(String name, TileEntity tile, FluidStack stack, int capacity) {
		super(stack, capacity);
		this.name = name;
		this.tile = tile;
	}

	public FluidTankTile(String name, TileEntity tile, Fluid fluid, int amount, int capacity) {
		super(fluid, amount, capacity);
		this.name = name;
		this.tile = tile;
	}

	public boolean isEmpty() {
		return getFluid() == null || getFluid().amount <= 0;
	}

	public boolean isFull() {
		return getFluid() != null && getFluid().amount >= getCapacity();
	}

	public Fluid getFluidType() {
		return getFluid() != null ? getFluid().getFluid() : null;
	}

	@Override
	public FluidTank readFromNBT(NBTTagCompound nbt) {
		if(nbt.hasKey(name)) {
			NBTTagCompound tankData = nbt.getCompoundTag(name);
			super.readFromNBT(tankData);
			readTankFromNBT(tankData);
		}
		return this;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound tankData = new NBTTagCompound();
		super.writeToNBT(tankData);
		writeTankToNBT(tankData);
		nbt.setTag(name, tankData);
		return nbt;
	}

	public void writeTankToNBT(NBTTagCompound nbt) {
	}

	public void readTankFromNBT(NBTTagCompound nbt) {
	}
}
