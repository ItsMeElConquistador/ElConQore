package elcon.mods.elconqore.entities;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityBlock extends Entity {

	public Block block;
	public float shadowSize = 0;
	public float rotationX = 0;
	public float rotationY = 0;
	public float rotationZ = 0;
	public double xSize, ySize, zSize;
	private int brightness = -1;
	
	public EntityBlock(World world) {
		super(world);
		preventEntitySpawning = false;
		noClip = true;
		isImmuneToFire = true;
	}
	
	public EntityBlock(World world, double x, double y, double z) {
		this(world);
		setPositionAndRotation(x, y, z, 0, 0);
	}

	public EntityBlock(World world, double x, double y, double z, double xSize, double ySize, double zSize) {
		this(world);
		this.xSize = xSize;
		this.ySize = ySize;
		this.zSize = zSize;
		setPositionAndRotation(x, y, z, 0, 0);
		this.motionX = 0.0;
		this.motionY = 0.0;
		this.motionZ = 0.0;
	}
	
	@Override
	public void moveEntity(double x, double y, double z) {
		setPosition(posX + x, posY + y, posZ + z);
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound data) {
		xSize = data.getDouble("xSize");
		ySize = data.getDouble("ySize");
		zSize = data.getDouble("zSize");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound data) {
		data.setDouble("xSize", xSize);
		data.setDouble("ySize", ySize);
		data.setDouble("zSize", zSize);
	}

	@Override
	public int getBrightnessForRender(float par1) {
		return brightness > 0 ? brightness : super.getBrightnessForRender(par1);
	}
}
