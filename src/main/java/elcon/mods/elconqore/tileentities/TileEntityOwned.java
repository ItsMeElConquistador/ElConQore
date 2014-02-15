package elcon.mods.elconqore.tileentities;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elcon.mods.elconqore.EQUtilClient;
import elcon.mods.elconqore.ElConQore;
import elcon.mods.elconqore.network.EQMessageTile;

public class TileEntityOwned extends TileEntity {

	public static class MessageTileOwned extends EQMessageTile {

		public String owner;
		
		public MessageTileOwned() {
		}
		
		public MessageTileOwned(int x, int y, int z, String owner) {
			super(x, y, z);
		}
		
		@Override
		public void encodeTo(ByteBuf target) {
			super.encodeTo(target);
			writeString(target, owner);
		}
		
		@Override
		public void decodeFrom(ByteBuf source) {
			super.decodeFrom(source);
			owner = readString(source);
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public void handle() {
			World world = EQUtilClient.getWorld();
			TileEntityOwned tile = (TileEntityOwned) world.getTileEntity(x, y, z);
			if(tile == null) {
				tile = new TileEntityOwned();
				world.setTileEntity(x, y, z, tile);
			}
			tile.setOwner(owner);
			world.markBlockForUpdate(x, y, z);
		}
	}
	
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
		return ElConQore.packetHandler.getPacketToClient(new MessageTileOwned(xCoord, yCoord, zCoord, owner));
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
