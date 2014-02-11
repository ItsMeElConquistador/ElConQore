package elcon.mods.elconqore.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;

public class TileEntityOwned extends TileEntity {

	private String owner = "[Minecraft]";
	
	public String getOwner() {
		return owner;
	}
	
	public boolean isOwner(String name) {
		return owner.equals(name);
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	@Override
	public Packet getDescriptionPacket() {
		//TODO: setup PacketHandler and Packets
		return super.getDescriptionPacket();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		owner = nbt.getString("Owner");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("Owner", owner);
	}
}
