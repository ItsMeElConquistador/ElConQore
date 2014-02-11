package elcon.mods.elconqore.world;

import java.lang.reflect.Constructor;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public abstract class WorldGenBase extends WorldGenerator {

	public boolean notifyBlocks;
	public int notifyFlag;
	public Random random;

	public WorldGenBase() {
		this(false, 1);
	}

	public WorldGenBase(boolean notifyBlocks, int notifyFlag) {
		this.notifyBlocks = notifyBlocks;
		this.notifyFlag = notifyFlag;
		random = new Random();
	}
	
	protected void setBlockAndMetadata(World world, int x, int y, int z, Block block, int meta) {
		setBlockAndNotifyAdequately(world, x, y, z, block, meta);
	}

	/**
	 * Sets the block and tile entity without metadata in the world, notifying neighbors if enabled.
	 */
	protected void setBlockAndTileEntity(World world, int x, int y, int z, Block block, TileEntity tile) {
		setBlockAndMetadataAndTileEntity(world, x, y, z, block, 0, tile);
	}

	/**
	 * Sets the block and tile entity in the world, notifying neighbors if enabled.
	 */
	protected void setBlockAndMetadataAndTileEntity(World world, int x, int y, int z, Block block, int meta, TileEntity tile) {
		if(notifyBlocks) {
			world.setBlock(x, y, z, block, meta, notifyFlag);
			world.setTileEntity(x, y, z, tile);
		} else {
			world.setBlock(x, y, z, block, meta, 0);
			world.setTileEntity(x, y, z, tile);
		}
	}

	/**
	 * Generates a cube, if replace is enabled it overrides existing blocks
	 */
	public void generateCube(World world, int x, int y, int z, int length, int height, int width, Block block, int meta, boolean replace) {
		for(int i = 0; i < length; i++) {
			for(int j = 0; j < height; j++) {
				for(int k = 0; k < width; k++) {
					Block oldBlock = world.getBlock(x + i, y + j, z + k);
					if(oldBlock == null || (oldBlock != null && replace)) {
						setBlockAndMetadata(world, x + i, y + j, z + k, block, meta);
					}
				}
			}
		}
	}

	/**
	 * Generates a cube with tile entities, if replace is enabled it overrides existing blocks
	 */
	public void generateCube(World world, int x, int y, int z, int length, int height, int width, Block block, int meta, boolean replace, Class<? extends TileEntity> tileClass, Object... args) {
		try {
			Class<?>[] argTypes = new Class[args.length];
			for(int i = 0; i < args.length; i++) {
				argTypes[i] = args[i].getClass();
			}
			Constructor<? extends TileEntity> constructor = tileClass.getConstructor(argTypes);
			for(int i = 0; i < length; i++) {
				for(int j = 0; j < height; j++) {
					for(int k = 0; k < width; k++) {
						Block oldBlock = world.getBlock(x + i, y + j, z + k);
						if(oldBlock == null || (oldBlock != null && replace)) {
							TileEntity tile = constructor.newInstance(args);
							setBlockAndMetadataAndTileEntity(world, x + i, y + j, z + k, block, meta, tile);
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates a vertical cylinder, if replace is enabled it overrides existing blocks
	 */
	public void generateCylinderVertical(World world, int x, int y, int z, double radius, int height, Block block, int meta, boolean replace) {
		for(int i = (int) -radius; i <= (int) radius; i++) {
			for(int k = (int) -radius; k <= (int) radius; k++) {
				if(BlockPosition.distance(new BlockPosition(x + i, y, z + k), new BlockPosition(x, y, z)) <= radius + 0.1D) {
					for(int j = 0; j < height; j++) {
						Block oldBlock = world.getBlock(x + i, y + j, z + k);
						if(oldBlock == null || (oldBlock != null && replace)) {
							setBlockAndMetadata(world, x + i, y + j, z + k, block, meta);
						}
					}
				}
			}
		}
	}

	/**
	 * Generates a vertical cylinder with tile entities, if replace is enabled it overrides existing blocks
	 */
	public void generateCylinderVertical(World world, int x, int y, int z, double radius, int height, Block block, int meta, boolean replace, Class<? extends TileEntity> tileClass, Object... args) {
		try {
			Class<?>[] argTypes = new Class[args.length];
			for(int i = 0; i < args.length; i++) {
				argTypes[i] = args[i].getClass();
			}
			Constructor<? extends TileEntity> constructor = tileClass.getConstructor(argTypes);
			for(int i = (int) -radius; i <= (int) radius; i++) {
				for(int k = (int) -radius; k <= (int) radius; k++) {
					if(BlockPosition.distance(new BlockPosition(x + i, y, z + k), new BlockPosition(x, y, z)) <= radius + 0.1D) {
						for(int j = 0; j < height; j++) {
							Block oldBlock = world.getBlock(x + i, y + j, z + k);
							if(oldBlock == null || (oldBlock != null && replace)) {
								TileEntity tile = constructor.newInstance(args);
								setBlockAndMetadataAndTileEntity(world, x + i, y + j, z + k, block, meta, tile);
							}
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates a horizontal cylinder along the X-axis, if replace is enabled it overrides existing blocks
	 */
	public void generateCylinderHorizontalX(World world, int x, int y, int z, double radius, int height, Block block, int meta, boolean replace) {
		for(int j = (int) -radius; j <= (int) radius; j++) {
			for(int k = (int) -radius; k <= (int) radius; k++) {
				if(BlockPosition.distance(new BlockPosition(x, y + j, z + k), new BlockPosition(x, y, z)) <= radius + 0.1D) {
					for(int i = 0; i < height; i++) {
						Block oldBlock = world.getBlock(x + i, y + j, z + k);
						if(oldBlock == null || (oldBlock != null && replace)) {
							setBlockAndMetadata(world, x + i, y + j, z + k, block, meta);
						}
					}
				}
			}
		}
	}

	/**
	 * Generates a horizontal cylinder with tile entities along the X-axis, if replace is enabled it overrides existing blocks
	 */
	public void generateCylinderHorizontalX(World world, int x, int y, int z, double radius, int height, Block block, int meta, boolean replace, Class<? extends TileEntity> tileClass, Object... args) {
		try {
			Class<?>[] argTypes = new Class[args.length];
			for(int i = 0; i < args.length; i++) {
				argTypes[i] = args[i].getClass();
			}
			Constructor<? extends TileEntity> constructor = tileClass.getConstructor(argTypes);
			for(int j = (int) -radius; j <= (int) radius; j++) {
				for(int k = (int) -radius; k <= (int) radius; k++) {
					if(BlockPosition.distance(new BlockPosition(x, y + j, z + k), new BlockPosition(x, y, z)) <= radius + 0.1D) {
						for(int i = 0; i < height; i++) {
							Block oldBlock = world.getBlock(x + i, y + j, z + k);
							if(oldBlock == null || (oldBlock != null && replace)) {
								TileEntity tile = constructor.newInstance(args);
								setBlockAndMetadataAndTileEntity(world, x + i, y + j, z + k, block, meta, tile);
							}
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates a horizontal cylinder along the Z-axis, if replace is enabled it overrides existing blocks
	 */
	public void generateCylinderHorizontalZ(World world, int x, int y, int z, double radius, int height, Block block, int meta, boolean replace) {
		for(int i = (int) -radius; i <= (int) radius; i++) {
			for(int j = (int) -radius; j <= (int) radius; j++) {
				if(BlockPosition.distance(new BlockPosition(x + i, y + j, z), new BlockPosition(x, y, z)) <= radius + 0.1D) {
					for(int k = 0; k < height; k++) {
						Block oldBlock = world.getBlock(x + i, y + j, z + k);
						if(oldBlock == null || (oldBlock != null && replace)) {
							setBlockAndMetadata(world, x + i, y + j, z + k, block, meta);
						}
					}
				}
			}
		}
	}

	/**
	 * Generates a horizontal cylinder with tile entities along the Z-axis, if replace is enabled it overrides existing blocks
	 */
	public void generateCylinderHorizontalZ(World world, int x, int y, int z, double radius, int height, Block block, int meta, boolean replace, Class<? extends TileEntity> tileClass, Object... args) {
		try {
			Class<?>[] argTypes = new Class[args.length];
			for(int i = 0; i < args.length; i++) {
				argTypes[i] = args[i].getClass();
			}
			Constructor<? extends TileEntity> constructor = tileClass.getConstructor(argTypes);
			for(int i = (int) -radius; i <= (int) radius; i++) {
				for(int j = (int) -radius; j <= (int) radius; j++) {
					if(BlockPosition.distance(new BlockPosition(x + i, y + j, z), new BlockPosition(x, y, z)) <= radius + 0.1D) {
						for(int k = 0; k < height; k++) {
							Block oldBlock = world.getBlock(x + i, y + j, z + k);
							if(oldBlock == null || (oldBlock != null && replace)) {
								TileEntity tile = constructor.newInstance(args);
								setBlockAndMetadataAndTileEntity(world, x + i, y + j, z + k, block, meta, tile);
							}
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates a vertical circle, if replace is enabled it overrides existing blocks
	 */
	public void generateCircleVertical(World world, int x, int y, int z, double radius, Block block, int meta, boolean replace) {
		generateCylinderVertical(world, x, y, z, radius, 1, block, meta, replace);
	}

	/**
	 * Generates a vertical circle with tile entities, if replace is enabled it overrides existing blocks
	 */
	public void generateCircleVertical(World world, int x, int y, int z, double radius, Block block, int meta, boolean replace, Class<? extends TileEntity> tileClass, Object... args) {
		generateCylinderVertical(world, x, y, z, radius, 1, block, meta, replace, tileClass, args);
	}

	/**
	 * Generates a horizontal circle along the X-axis, if replace is enabled it overrides existing blocks
	 */
	public void generateCircleHorizontalX(World world, int x, int y, int z, double radius, Block block, int meta, boolean replace) {
		generateCylinderHorizontalX(world, x, y, z, radius, 1, block, meta, replace);
	}

	/**
	 * Generates a horizontal circle with tile entities along the X-axis, if replace is enabled it overrides existing blocks
	 */
	public void generateCircleHorizontalX(World world, int x, int y, int z, double radius, Block block, int meta, boolean replace, Class<? extends TileEntity> tileClass, Object... args) {
		generateCylinderHorizontalX(world, x, y, z, radius, 1, block, meta, replace, tileClass, args);
	}

	/**
	 * Generates a horizontal circle along the Z-axis, if replace is enabled it overrides existing blocks
	 */
	public void generateCircleHorizontalZ(World world, int x, int y, int z, double radius, Block block, int meta, boolean replace) {
		generateCylinderHorizontalZ(world, x, y, z, radius, 1, block, meta, replace);
	}

	/**
	 * Generates a horizontal circle with tile entities along the Z-axis, if replace is enabled it overrides existing blocks
	 */
	public void generateCircleHorizontalZ(World world, int x, int y, int z, double radius, Block block, int meta, boolean replace, Class<? extends TileEntity> tileClass, Object... args) {
		generateCylinderHorizontalZ(world, x, y, z, radius, 1, block, meta, replace, tileClass, args);
	}

	/**
	 * Generates a sphere with different radii, if replace is enabled it overrides existing blocks
	 */
	public void generateSphereCustom(World world, int x, int y, int z, double radiusX, double radiusY, double radiusZ, Block block, int meta, boolean replace) {
		for(int i = (int) -radiusX; i <= (int) radiusX; i++) {
			for(int j = (int) -radiusY; j <= (int) radiusY; j++) {
				for(int k = (int) -radiusZ; k <= (int) radiusZ; k++) {
					if(BlockPosition.distance(new BlockPosition(x + i, y + j, z + k), new BlockPosition(x, y, z)) <= radiusX + 0.1D && BlockPosition.distance(new BlockPosition(x + i, y + j, z + k), new BlockPosition(x, y, z)) <= radiusY + 0.1D && BlockPosition.distance(new BlockPosition(x + i, y + j, z + k), new BlockPosition(x, y, z)) <= radiusZ + 0.1D) {
						Block oldBlock = world.getBlock(x + i, y + j, z + k);
						if(oldBlock == null || (oldBlock != null && replace)) {
							setBlockAndMetadata(world, x + i, y + j, z + k, block, meta);
						}
					}
				}
			}
		}

	}

	/**
	 * Generates a sphere with tile entities with different radii, if replace is enabled it overrides existing blocks
	 */
	public void generateSphereCustom(World world, int x, int y, int z, double radiusX, double radiusY, double radiusZ, Block block, int meta, boolean replace, Class<? extends TileEntity> tileClass, Object... args) {
		try {
			Class<?>[] argTypes = new Class[args.length];
			for(int i = 0; i < args.length; i++) {
				argTypes[i] = args[i].getClass();
			}
			Constructor<? extends TileEntity> constructor = tileClass.getConstructor(argTypes);
			for(int i = (int) -radiusX; i <= (int) radiusX; i++) {
				for(int j = (int) -radiusY; j <= (int) radiusY; j++) {
					for(int k = (int) -radiusZ; k <= (int) radiusZ; k++) {
						if(BlockPosition.distance(new BlockPosition(x + i, y + j, z + k), new BlockPosition(x, y, z)) <= radiusX + 0.1D && BlockPosition.distance(new BlockPosition(x + i, y + j, z + k), new BlockPosition(x, y, z)) <= radiusY + 0.1D && BlockPosition.distance(new BlockPosition(x + i, y + j, z + k), new BlockPosition(x, y, z)) <= radiusZ + 0.1D) {
							Block oldBlock = world.getBlock(x + i, y + j, z + k);
							if(oldBlock == null || (oldBlock != null && replace)) {
								TileEntity tile = constructor.newInstance(args);
								setBlockAndMetadataAndTileEntity(world, x + i, y + j, z + k, block, meta, tile);
							}
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates a sphere with the same different radius, if replace is enabled it overrides existing blocks
	 */
	public void generateSphere(World world, int x, int y, int z, double radius, Block block, int meta, boolean replace) {
		for(int i = (int) -radius; i <= (int) radius; i++) {
			for(int j = (int) -radius; j <= (int) radius; j++) {
				for(int k = (int) -radius; k <= (int) radius; k++) {
					if(BlockPosition.distance(new BlockPosition(x + i, y + j, z + k), new BlockPosition(x, y, z)) <= radius + 0.01D) {
						Block oldBlock = world.getBlock(x + i, y + j, z + k);
						if(oldBlock == null || (oldBlock != null && replace)) {
							setBlockAndMetadata(world, x + i, y + j, z + k, block, meta);
						}
					}
				}
			}
		}
	}

	/**
	 * Generates a sphere with tile entities with the same different radius, if replace is enabled it overrides existing blocks
	 */
	public void generateSphere(World world, int x, int y, int z, double radius, Block block, int meta, boolean replace, Class<? extends TileEntity> tileClass, Object... args) {
		try {
			Class<?>[] argTypes = new Class[args.length];
			for(int i = 0; i < args.length; i++) {
				argTypes[i] = args[i].getClass();
			}
			Constructor<? extends TileEntity> constructor = tileClass.getConstructor(argTypes);
			for(int i = (int) -radius; i <= (int) radius; i++) {
				for(int j = (int) -radius; j <= (int) radius; j++) {
					for(int k = (int) -radius; k <= (int) radius; k++) {
						if(BlockPosition.distance(new BlockPosition(x + i, y + j, z + k), new BlockPosition(x, y, z)) <= radius + 0.01D) {
							Block oldBlock = world.getBlock(x + i, y + j, z + k);
							if(oldBlock == null || (oldBlock != null && replace)) {
								TileEntity tile = constructor.newInstance(args);
								setBlockAndMetadataAndTileEntity(world, x + i, y + j, z + k, block, meta, tile);
							}
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
