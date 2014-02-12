package elcon.mods.elconqore.metadata;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elcon.mods.elconqore.ElConQore;

public class EQExtendedBlockStorage extends ExtendedBlockStorage {

	//TODO: S21PacketChunkData.func_149269_a(Chunk, boolean, int)
	//TODO: Chunk.fillChunk(byte[], int, int, boolean)
	//TODO: AnvilChunkLoader.readChunkFromNBT(World, NBTTagCompound)
	//TODO: AnvilChunkLoader.writeChunkToNBT(Chunk, World, NBTTagCompound)
	
	private int newBlockRefCount;
	private int newTickRefCount;

	private short[] newBlockIDArray;
	private short[] newBlockMetadataArray;

	private NibbleArray newBlocklightArray;
	private NibbleArray newSkylightArray;

	public EQExtendedBlockStorage(int yBase, boolean initSkylight) {
		super(yBase, initSkylight);
		newBlockIDArray = new short[4096];
		newBlockMetadataArray = new short[4096];
		newBlocklightArray = new NibbleArray(4096, 4);
		if(initSkylight) {
			newSkylightArray = new NibbleArray(4096, 4);
		}
	}

	@Override
	public Block getBlockByExtId(int x, int y, int z) {
		return Block.getBlockById(newBlockIDArray[y << 8 | z << 4 | x]);
	}

	@Override
	public void func_150818_a(int x, int y, int z, Block block) {
		Block oldBlock = Block.getBlockById(newBlockIDArray[y << 8 | z << 4 | x]);
		if(oldBlock != Blocks.air) {
			newBlockRefCount--;
			if(oldBlock.getTickRandomly()) {
				newTickRefCount--;
			}
		}
		if(block != Blocks.air) {
			newBlockRefCount++;
			if(block.getTickRandomly()) {
				newTickRefCount++;
			}
		}
		newBlockIDArray[y << 8 | z << 4 | x] = (short) Block.getIdFromBlock(block);
	}

	@Override
	public int getExtBlockMetadata(int x, int y, int z) {
		return newBlockMetadataArray[y << 8 | z << 4 | x];
	}

	@Override
	public void setExtBlockMetadata(int x, int y, int z, int metadata) {
		newBlockMetadataArray[y << 8 | z << 4 | x] = (short) metadata;
	}

	@Override
	public boolean isEmpty() {
		return newBlockRefCount == 0;
	}

	@Override
	public boolean getNeedsRandomTick() {
		return newTickRefCount > 0;
	}

	@Override
	public int getExtBlocklightValue(int x, int y, int z) {
		return newBlocklightArray.get(x, y, z);
	}

	@Override
	public void setExtBlocklightValue(int x, int y, int z, int blocklight) {
		newBlocklightArray.set(x, y, z, blocklight);
	}

	@Override
	public int getExtSkylightValue(int x, int y, int z) {
		return newSkylightArray.get(x, y, z);
	}

	@Override
	public void setExtSkylightValue(int x, int y, int z, int skylight) {
		newSkylightArray.set(x, y, z, skylight);
	}

	@Override
	public void removeInvalidBlocks() {
		newBlockRefCount = 0;
		newTickRefCount = 0;
		for(int i = 0; i < 16; i++) {
			for(int j = 0; j < 16; j++) {
				for(int k = 0; k < 16; k++) {
					Block block = getBlockByExtId(i, j, k);
					if(block != Blocks.air) {
						newBlockRefCount++;
						if(block.getTickRandomly()) {
							newTickRefCount++;
						}
					}
				}
			}
		}
	}
	
	@Override
	public NibbleArray getBlocklightArray() {
		return newBlocklightArray;
	}
	
	@Override
	public void setBlocklightArray(NibbleArray blocklightArray) {
		this.newBlocklightArray = blocklightArray;
	}
	
	@Override
	public NibbleArray getSkylightArray() {
		return newSkylightArray;
	}
	
	@Override
	public void setSkylightArray(NibbleArray skylightArray) {
		this.newSkylightArray = skylightArray;
	}
	
	@Override
	public byte[] getBlockLSBArray() {
		ElConQore.log.error("DO NOT USE: EQExtendedBlockStorage.getBlockLSBArray()");
		return null;
	}
	
	@Override
	public void setBlockLSBArray(byte[] blockLSBArray) {
		ElConQore.log.error("DO NOT USE: EQExtendedBlockStorage.setBlockLSBArray(byte[])");
	}
	
	@Override
	public NibbleArray getBlockMSBArray() {
		ElConQore.log.error("DO NOT USE: EQExtendedBlockStorage.getBlockMSBArray()");
		return null;
	}
	
	@Override
	public void setBlockMSBArray(NibbleArray blockMSBArray) {
		ElConQore.log.error("DO NOT USE: EQExtendedBlockStorage.setBlockMSBArray(NibbleArray)");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public NibbleArray createBlockMSBArray() {
		ElConQore.log.error("DO NOT USE: EQExtendedBlockStorage.createBlockMSBArray()");
		return null;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void clearMSBArray() {
		ElConQore.log.error("DO NOT USE: EQExtendedBlockStorage.clearMSBArray()");
	}
	
	@Override
	public NibbleArray getMetadataArray() {
		ElConQore.log.error("DO NOT USE: EQExtendedBlockStorage.getMetadataArray()");
		return null;
	}
	
	@Override
	public void setBlockMetadataArray(NibbleArray blockMetadataArray) {
		ElConQore.log.error("DO NOT USE: EQExtendedBlockStorage.setBlockMetadataArray(NibbleArray)");
	}
}
