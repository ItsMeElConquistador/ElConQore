package elcon.mods.elconqore.structure;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import elcon.mods.elconqore.tileentities.TileEntityStructure;

public abstract class MBStructure {

	public static enum MBStructureState {
		VALID, INVALID, UNKOWN;
	}

	public String name;
	public HashMap<String, MBStructurePattern> patterns = new HashMap<String, MBStructurePattern>();
	public HashMap<Character, Integer> idOnValid = new HashMap<Character, Integer>();
	public HashMap<Character, Integer> metaOnValid = new HashMap<Character, Integer>();

	public MBStructure(String name) {
		this.name = name;
	}

	public void addPattern(MBStructurePattern pattern) {
		patterns.put(pattern.name, pattern);
	}

	public void removePattern(String pattern) {
		patterns.remove(pattern);
	}
	
	public void validateStructure(TileEntityStructure tile) {
		if(!tile.isMaster()) {
			TileEntityStructure tileMaster = tile.getStructureCenter();
			if(tileMaster != null) {
				tileMaster.validateStructure();
				return;
			}
		}
		MBStructureState state = MBStructureState.UNKOWN;
		ForgeDirection[] rotations = new ForgeDirection[]{ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.WEST, ForgeDirection.SOUTH};
		for(MBStructurePattern pattern : patterns.values()) {
			if(pattern.width != pattern.depth) {
				for(int i = 0; i < rotations.length; i++) {
					state = tile.determineMasterState(pattern, rotations[i]);
					if(state == MBStructureState.VALID) {
						tile.structure = name;
						tile.structurePattern = pattern.name;
						tile.rotation = rotations[i];
						if(!tile.isMaster()) {
							tile.setMaster(true);
							tile.markStructureBlocks();
						}
						return;
					}
				}
			} else {
				state = tile.determineMasterState(pattern, ForgeDirection.NORTH);
				if(state == MBStructureState.VALID) {
					tile.structure = name;
					tile.structurePattern = pattern.name;
					tile.rotation = ForgeDirection.NORTH;
					if(!tile.isMaster()) {
						tile.setMaster(true);
						tile.markStructureBlocks();
					}
					return;
				}
			}
		}
		if(tile.isMaster()) {
			tile.resetStructureBlocks();
		}
	}
	
	public MBStructureState determineMasterState(TileEntityStructure tile, MBStructurePattern pattern, ForgeDirection rotation) {
		if(pattern != null) {
			int[] dimensions = pattern.getDimensions(tile.rotation);
			int[] offsets = pattern.getOffsets(tile.rotation);
			for(int i = 0; i < dimensions[0]; i++) {
				for(int j = 0; j < dimensions[1]; j++) {
					for(int k = 0; k < dimensions[2]; k++) {
						int x = tile.xCoord + i + offsets[0];
						int y = tile.yCoord + j + offsets[1];
						int z = tile.zCoord + k + offsets[2];
						if(!tile.getWorldObj().blockExists(x, y, z)) {
							return MBStructureState.UNKOWN;
						}
						if(!areCharAndBlockEqual(pattern.getCharAt(i, j, k, rotation), tile.getWorldObj().getBlock(x, y, z), tile.getWorldObj().getBlockMetadata(x, y, z), tile.getWorldObj().getTileEntity(x, y, z))) {
							return MBStructureState.INVALID;
						}
					}
				}
			}
		}		
		return MBStructureState.VALID;
	}
	
	public void markStructureBlocks(TileEntityStructure tile) {
		MBStructurePattern pattern = MBStructureRegistry.getStructurePattern(tile.structure, tile.structurePattern);
		if(pattern != null) {
			int[] dimensions = pattern.getDimensions(tile.rotation);
			int[] offsets = pattern.getOffsets(tile.rotation);
			for(int i = 0; i < dimensions[0]; i++) {
				for(int j = 0; j < dimensions[1]; j++) {
					for(int k = 0; k < dimensions[2]; k++) {
						int x = tile.xCoord + i + offsets[0];
						int y = tile.yCoord + j + offsets[1];
						int z = tile.zCoord + k + offsets[2];
						TileEntity otherTile = tile.getWorldObj().getTileEntity(x, y, z);
						if(otherTile instanceof TileEntityStructure) {
							TileEntityStructure part = (TileEntityStructure) otherTile;
							if(part.structure.equals(tile.structure)) {
								part.rotation = tile.rotation;
								part.structurePattern = tile.structurePattern;
								part.setStructureMaster(tile);
							}
						}
						char type = pattern.getCharAt(i, j, k, tile.rotation);
						if(idOnValid.containsKey(type)) {
							tile.getWorldObj().setBlock(x, y, z, Block.getBlockById(idOnValid.get(type).intValue()), tile.getWorldObj().getBlockMetadata(x, y, z), 2);
						}
						if(metaOnValid.containsKey(type)) {
							tile.getWorldObj().setBlockMetadataWithNotify(x, y, z, metaOnValid.get(type).intValue(), 2);
						}
					}
				}
			}
		}
	}
	
	public void resetStructureBlocks(TileEntityStructure tile) {
		MBStructurePattern pattern = MBStructureRegistry.getStructurePattern(tile.structure, tile.structurePattern);
		if(pattern != null) {
			int[] dimensions = pattern.getDimensions(tile.rotation);
			int[] offsets = pattern.getOffsets(tile.rotation);
			for(int i = 0; i < dimensions[0]; i++) {
				for(int j = 0; j < dimensions[1]; j++) {
					for(int k = 0; k < dimensions[2]; k++) {
						int x = tile.xCoord + i + offsets[0];
						int y = tile.yCoord + j + offsets[1];
						int z = tile.zCoord + k + offsets[2];
						TileEntity otherTile = tile.getWorldObj().getTileEntity(x, y, z);
						if(otherTile != null && otherTile instanceof TileEntityStructure) {
							TileEntityStructure part = (TileEntityStructure) otherTile;
							if(part.structure.equals(tile.structure) && part.structurePattern.equals(tile.structurePattern)) {
								part.onStructureReset();
							}
						}
					}
				}
			}
		}
	}
	
	public abstract boolean areCharAndBlockEqual(char required, Block block, int blockMetadata, TileEntity blockTileEntity);
}
