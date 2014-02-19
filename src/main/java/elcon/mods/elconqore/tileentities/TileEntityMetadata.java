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

public class TileEntityMetadata extends TileEntityExtended {

	public static class MessageTileMetadata extends EQMessageTile {
		
		public int metadata;
		
		public MessageTileMetadata() {
		}
		
		public MessageTileMetadata(int x, int y, int z, int metadata) {
			super(x, y, z);
			this.metadata = metadata;
		}
		
		@Override
		public void encodeTo(ByteBuf target) {
			super.encodeTo(target);
			target.writeShort(metadata);
		}
		
		@Override
		public void decodeFrom(ByteBuf source) {
			super.decodeFrom(source);
			metadata = source.readUnsignedShort();
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public void handle() {
			World world = EQUtilClient.getWorld();
			TileEntityMetadata tile = (TileEntityMetadata) world.getTileEntity(x, y, z);
			if(tile == null) {
				tile = new TileEntityMetadata();
				world.setTileEntity(x, y, z, tile);
			}
			tile.setTileMetadata(metadata);
			world.markBlockForUpdate(x, y, z);
		}
	}
	
	private int metadata = 0;
	
	public TileEntityMetadata() {
	}
	
	public TileEntityMetadata(byte metadata) {
		setTileMetadata(metadata);
	}
	
	public TileEntityMetadata(Byte metadata) {
		this(metadata.byteValue());
	}
	
	@Override
	public Packet getDescriptionPacket() {
		return ElConQore.packetHandler.getPacketToClient(new MessageTileMetadata(xCoord, yCoord, zCoord, getTileMetadata()));
	}
	
	public int getTileMetadata() {
		return metadata;
	}
	
	public void setTileMetadata(int metadata) {
		if(metadata >= 65536) {
			ElConQore.log.error(getBlockType().getUnlocalizedName() + " (" + getBlockType() + ") is using metadata >= 65536, this will crash or derp!");
		}
		this.metadata = metadata;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		metadata = nbt.getInteger("Metadata");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("Metadata", metadata);
	}
}
