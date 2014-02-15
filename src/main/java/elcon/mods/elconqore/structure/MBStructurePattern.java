package elcon.mods.elconqore.structure;

import net.minecraftforge.common.util.ForgeDirection;
import elcon.mods.elconqore.ElConQore;

public class MBStructurePattern {

	public String name;
	public char[][][] structure;
	public int width;
	public int height;
	public int depth;
	public int offsetX;
	public int offsetY;
	public int offsetZ;
	
	public MBStructurePattern(String name, int width, int height, int depth) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.depth = depth;
		structure = new char[width][height][depth];
	}
	
	public MBStructurePattern(String name, int width, int height, int depth, String[] pattern) {
		this(name, width, height, depth);
		setStructure(pattern);
	}
	
	public MBStructurePattern(String name, int width, int height, int depth, String pattern) {
		this(name, width, height, depth);
		setStructure(pattern);
	}
	
	public MBStructurePattern setStructure(String[] pattern) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < pattern.length; i++) {
			sb.append(pattern[i]);
		}
		return setStructure(sb.toString());
	}
	
	public MBStructurePattern setStructure(String pattern) {
		if(pattern.length() != (width * height * depth)) {
			ElConQore.log.info("[MultiBlockStructures] Incorrect pattern " + pattern + " (" + pattern.length() + ") for (" + width + "/" + height + "/" + depth + ")");
		}
		for(int j = 0; j < height; j++) {
			for(int i = 0; i < width; i++) {
				for(int k = 0; k < depth; k++) {
					structure[i][j][k] = pattern.charAt(i + k * width + j * width * depth);
				}
			}
		}
		return this;
	}
	
	public MBStructurePattern setOffsets(int offsetX, int offsetY, int offsetZ) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		return this;
	}
	
	public int[] getDimensions(ForgeDirection direction) {
		if(direction == null) {
			return new int[]{width, height, depth};
		}
		switch(direction) {
		default:
		case NORTH:
		case SOUTH:
			return new int[]{width, height, depth};
		case EAST:
		case WEST:
			return new int[]{depth, height, width};
		}
	}
	
	public int[] getOffsets(ForgeDirection direction) {
		if(direction == null) {
			return new int[]{offsetX, offsetY, offsetZ};
		}
		switch(direction) {
		default:
		case NORTH:
			return new int[]{offsetX, offsetY, offsetZ};
		case EAST:
			return new int[]{offsetZ, offsetY, offsetX};
		case SOUTH:
			return new int[]{width - offsetX - 1, offsetY, depth - offsetZ - 1};
		case WEST:
			return new int[]{width - offsetZ - 1, offsetY, depth - offsetX - 1};
		}
	}
	
	public char getCharAt(int x, int y, int z, ForgeDirection direction) {
		switch(direction) {
		default:
		case NORTH:
			return structure[x][y][z];
		case EAST:
			return structure[z][y][x];
		case SOUTH:
			return structure[width - x - 1][y][depth - z - 1];
		case WEST:
			return structure[width - z - 1][y][depth - x - 1];
		}
	}
}
