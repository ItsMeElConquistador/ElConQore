package elcon.mods.elconqore.tileentities;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elcon.mods.elconqore.EQUtilClient;
import elcon.mods.elconqore.ElConQore;
import elcon.mods.elconqore.network.EQMessageTile;

public class TileEntityNBT extends TileEntityExtended {

	public static class MessageTileNBT extends EQMessageTile {

		public NBTTagCompound nbt;
		
		public MessageTileNBT(int x, int y, int z, NBTTagCompound nbt) {
			super(x, y, z);
			this.nbt = nbt;
		}

		@Override
		public void encodeTo(ByteBuf target) {
			super.encodeTo(target);
			writeNBTTagCompound(target, nbt);
		}
		
		@Override
		public void decodeFrom(ByteBuf source) {
			super.decodeFrom(source);
			nbt = readNBTTagCompound(source);
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public void handle() {
			World world = EQUtilClient.getWorld();
			TileEntityNBT tile = (TileEntityNBT) world.getTileEntity(x, y, z);
			if(tile == null) {
				tile = new TileEntityNBT();
				world.setTileEntity(x, y, z, tile);
			}
			tile.setNBT(nbt);
			world.markBlockForUpdate(x, y, z);
		}		
	}
	
	private NBTTagCompound nbt;
	
	@Override
	public boolean canUpdate() {
		return false;
	}
	
	@Override
	public Packet getDescriptionPacket() {
		return ElConQore.packetHandler.getPacketToClient(new MessageTileNBT(xCoord, yCoord, zCoord, getNBT()));
	}
	
	public NBTTagCompound getNBT() {
		return nbt;
	}
	
	public void setNBT(NBTTagCompound nbt) {
		this.nbt = nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		nbt = nbt.getCompoundTag("NBT");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setTag("NBT", nbt);
	}
}
