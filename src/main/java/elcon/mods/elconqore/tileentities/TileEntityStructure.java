package elcon.mods.elconqore.tileentities;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elcon.mods.elconqore.EQUtilClient;
import elcon.mods.elconqore.ElConQore;
import elcon.mods.elconqore.blocks.BlockStructure;
import elcon.mods.elconqore.network.EQMessageTile;
import elcon.mods.elconqore.structure.MBStructure.MBStructureState;
import elcon.mods.elconqore.structure.MBStructurePattern;
import elcon.mods.elconqore.structure.MBStructureRegistry;

public class TileEntityStructure extends TileEntityExtended {

	public static class MessageTileStructure extends EQMessageTile {

		public boolean hasStructure;
		
		public MessageTileStructure() {
		}
		
		public MessageTileStructure(int x, int y, int z, boolean hasStructure) {
			super(x, y, z);
			this.hasStructure = hasStructure;
		}
		
		@Override
		public void encodeTo(ByteBuf target) {
			super.encodeTo(target);
			target.writeBoolean(hasStructure);
		}
		
		@Override
		public void decodeFrom(ByteBuf source) {
			super.decodeFrom(source);
			hasStructure = source.readBoolean();
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void handle() {
			World world = EQUtilClient.getWorld();
			TileEntityStructure tile = (TileEntityStructure) world.getTileEntity(x, y, z);
			tile.hasStructure = hasStructure;
			world.markBlockForUpdate(x, y, z);
		}
	}
	
	private BlockStructure block;
	
	public String structure;
	public String structurePattern;
	public ForgeDirection rotation;
	private boolean isMaster;
	public int masterX;
	public int masterY;
	public int masterZ;
	public boolean hasMasterTile;
	
	@SideOnly(Side.CLIENT)
	public boolean hasStructure;
	
	public TileEntityStructure() {
		this.structure = "";
		this.structurePattern = "";
	}
	
	public TileEntityStructure(String structure, BlockStructure block) {
		this.block = block;
		
		this.structure = structure;
		this.structurePattern = "";
	}
	
	public boolean hasStructure() {
		return structure.length() > 0 && structurePattern.length() > 0;
	}

	public boolean isMaster() {
		return isMaster;
	}
	
	public void setMaster(boolean isMaster) {
		this.isMaster = isMaster;
		if(isMaster) {
			setStructureMaster(null);
		}
	}
	
	public TileEntityStructure getStructureCenter() {
		if(hasMasterTile) {
			TileEntity tile = worldObj.getTileEntity(masterX, masterY, masterZ);
			if(tile != null && tile instanceof TileEntityStructure) {
				return (TileEntityStructure) tile;
			}
		}
		return null;
	}
	
	public void setStructureMaster(TileEntityStructure structureMaster) {
		hasMasterTile = structureMaster != null;
		if(structureMaster != null) {
			masterX = structureMaster.xCoord;
			masterY = structureMaster.yCoord;
			masterZ = structureMaster.zCoord;
		}
	}
	
	public void validateStructure() {
		if(structure.length() > 0) {
			char[][][] chars = MBStructureRegistry.getStructurePattern(structure, "4x4").structure;
			for(int j = 0; j < 3; j++) {
				System.out.println();
				for(int i = 0; i < 4; i++) {
					System.out.print(" ");
					for(int k = 0; k < 4; k++) {
						System.out.print(chars[i][j][k]);
					}
				}
			}
			System.out.println();
			MBStructureRegistry.getStructure(structure).validateStructure(this);
		}
	}

	public void markStructureBlocks() {
		MBStructureRegistry.getStructure(structure).markStructureBlocks(this);
	}
	
	public void resetStructureBlocks() {
		MBStructureRegistry.getStructure(structure).resetStructureBlocks(this);
	}
	
	public MBStructureState determineMasterState(MBStructurePattern pattern, ForgeDirection rotation) {
		return MBStructureRegistry.getStructure(structure).determineMasterState(this, pattern, rotation);
	}
	
	public void onStructureReset() {
		setMaster(false);
		setStructureMaster(null);
		if(block != null) {
			block.onStructureReset(worldObj, xCoord, yCoord, zCoord);
		}
	}
	
	@Override
	public Packet getDescriptionPacket() {
		return ElConQore.packetHandler.getPacketToClient(new MessageTileStructure(xCoord, yCoord, zCoord, hasStructure()));
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		structure = nbt.getString("Structure");
		if(nbt.hasKey("StructurePattern")) {
			structurePattern = nbt.getString("StructurePattern");
		}
		rotation = ForgeDirection.values()[nbt.getByte("Rotation")];
		isMaster = nbt.getBoolean("IsMaster");
		masterX = nbt.getInteger("MasterX");
		masterY = nbt.getInteger("MasterY");
		masterZ = nbt.getInteger("MasterZ");
		hasMasterTile = nbt.getBoolean("HasMasterTile");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("Structure", structure);
		if(structurePattern != null && structurePattern.length() > 0) {
			nbt.setString("StructurePattern", structurePattern);
		}
		if(rotation != null) {
			nbt.setByte("Rotation", (byte) rotation.ordinal());
		} else {
			nbt.setByte("Rotation", (byte) 0);
		}
		nbt.setBoolean("IsMaster", isMaster);
		nbt.setInteger("MasterX", masterX);
		nbt.setInteger("MasterY", masterY);
		nbt.setInteger("MasterZ", masterZ);
		nbt.setBoolean("HasMasterTile", hasMasterTile);
	}
}
