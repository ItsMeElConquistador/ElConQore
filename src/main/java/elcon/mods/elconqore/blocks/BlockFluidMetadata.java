package elcon.mods.elconqore.blocks;

import java.util.HashMap;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elcon.mods.elconqore.EQConfig;
import elcon.mods.elconqore.ElConQore;
import elcon.mods.elconqore.fluids.FluidMetadata;

public class BlockFluidMetadata extends BlockExtendedMetadata implements IFluidBlock {

	public HashMap<Integer, Boolean> displacementIds = new HashMap<Integer, Boolean>();

	public boolean[] isOptimalFlowDirection = new boolean[4];
	public int[] flowCost = new int[4];

	public FluidMetadata[] fluidList;
	public int tickRate = 20;
	public int quantaPerBlock = 8;
	public float quantaPerBlockFloat = 8F;

	public BlockFluidMetadata(Material material) {
		super(material);
		setTickRandomly(true);
		disableStats();

		displacementIds.put(Block.getIdFromBlock(Blocks.wooden_door), false);
		displacementIds.put(Block.getIdFromBlock(Blocks.iron_door), false);
		displacementIds.put(Block.getIdFromBlock(Blocks.standing_sign), false);
		displacementIds.put(Block.getIdFromBlock(Blocks.wall_sign), false);
		displacementIds.put(Block.getIdFromBlock(Blocks.reeds), false);
		displacementIds.put(Block.getIdFromBlock(Blocks.snow), true);
	}

	public BlockFluidMetadata(Material material, FluidMetadata[] fluids) {
		this(material);
		setFluids(fluids);
	}

	public BlockFluidMetadata setFluids(FluidMetadata[] fluids) {
		int maxMeta = 0;
		for(int i = 0; i < fluids.length; i++) {
			if(fluids[i] != null) {
				fluids[i].setBlock(this);
				if(fluids[i].metadata > maxMeta) {
					maxMeta = fluids[i].metadata;
				}
			}
		}
		fluidList = new FluidMetadata[maxMeta + 1];
		for(int i = 0; i < fluids.length; i++) {
			if(fluids[i] != null) {
				fluidList[fluids[i].metadata] = fluids[i];
			}
		}
		if(fluidList.length > 256) {
			ElConQore.log.error(getUnlocalizedName() + "(" + getClass() + ") metadata has more than 256 fluids, expect crashes or derpyness!");
		}
		return this;
	}

	public BlockFluidMetadata setQuantaPerBlock(int quantaPerBlock) {
		if(quantaPerBlock > 16 || quantaPerBlock < 1) {
			quantaPerBlock = 8;
		}
		this.quantaPerBlock = quantaPerBlock;
		this.quantaPerBlockFloat = quantaPerBlock;
		return this;
	}

	public BlockFluidMetadata setTickRate(int tickRate) {
		if(tickRate <= 0) {
			tickRate = 20;
		}
		this.tickRate = tickRate;
		return this;
	}

	public Fluid getFluid(int meta) {
		return fluidList[meta];
	}

	public Fluid getFluid(IBlockAccess blockAccess, int x, int y, int z) {
		return getFluid(getMetadata(blockAccess, x, y, z));
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		int temperature = getFluid(world, x, y, z).getTemperature();
		if(temperature >= 50 && temperature < 75) {
			if(world.rand.nextInt(5) == 0) {
				entity.setFire(5);
			}
		} else if(temperature >= 75 && temperature < 100) {
			if(world.rand.nextInt(3) == 0) {
				entity.setFire(10);
			}
		} else if(temperature >= 100) {
			entity.setFire(30);
		}
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		world.scheduleBlockUpdate(x, y, z, this, tickRate);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		world.scheduleBlockUpdate(x, y, z, this, tickRate);
	}

	@Override
	public boolean func_149698_L() {
		return false;
	}

	@Override
	public boolean getBlocksMovement(IBlockAccess world, int x, int y, int z) {
		return true;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Item getItemDropped(int par1, Random random, int par3) {
		return null;
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public int tickRate(World world) {
		return tickRate;
	}

	public boolean canDisplace(IBlockAccess blockAccess, int x, int y, int z, int otherX, int otherY, int otherZ) {
		if(blockAccess.isAirBlock(otherX, otherY, otherZ)) {
			return true;
		}
		int id = Block.getIdFromBlock(blockAccess.getBlock(otherX, otherY, otherZ));
		if(id == Block.getIdFromBlock(this)) {
			return false;
		}
		if(displacementIds.containsKey(id)) {
			return displacementIds.get(id);
		}
		Material material = Block.getBlockById(id).getMaterial();
		if(material.blocksMovement() || material == Material.portal) {
			return false;
		}
		int density = getDensity(blockAccess, otherX, otherY, otherZ);
		if(density == Integer.MAX_VALUE) {
			return true;
		}
		if(getFluid(blockAccess, x, y, z).getDensity() > density) {
			return true;
		} else {
			return false;
		}
	}

	public boolean displaceIfPossible(World world, int x, int y, int z, int otherX, int otherY, int otherZ) {
		if(world.isAirBlock(otherX, otherY, otherZ)) {
			return true;
		}
		int id = Block.getIdFromBlock(world.getBlock(otherX, otherY, otherZ));
		if(id == Block.getIdFromBlock(this)) {
			return false;
		}
		Block block = Block.getBlockById(id);
		if(displacementIds.containsKey(id)) {
			if(displacementIds.get(id)) {
				block.dropBlockAsItem(world, otherX, otherY, otherZ, world.getBlockMetadata(otherX, otherY, otherZ), 0);
				return true;
			}
			return false;
		}
		Material material = block.getMaterial();
		if(material.blocksMovement() || material == Material.portal) {
			return false;
		}
		int density = getDensity(world, otherX, otherY, otherZ);
		if(density == Integer.MAX_VALUE) {
			block.dropBlockAsItem(world, otherX, otherY, otherZ, world.getBlockMetadata(otherX, otherY, otherZ), 0);
			return true;
		}
		if(getFluid(world, x, y, z).getDensity() > density) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean canCollideCheck(int meta, boolean fullHit) {
		return fullHit && meta == 0;
	}

	public int getMaxRenderHeightMeta() {
		return 0;
	}

	@Override
	public void velocityToAddToEntity(World world, int x, int y, int z, Entity entity, Vec3 vec) {
		if(getDensityDirection(world, x, y, z) > 0) {
			return;
		}
		Vec3 vec_flow = getFlowVector(world, x, y, z);
		vec.xCoord += vec_flow.xCoord * (quantaPerBlock * 4);
		vec.yCoord += vec_flow.yCoord * (quantaPerBlock * 4);
		vec.zCoord += vec_flow.zCoord * (quantaPerBlock * 4);
	}

	@Override
	public int getLightValue(IBlockAccess blockAccess, int x, int y, int z) {
		if(getFluid(blockAccess, x, y, z).getLuminosity() == 0) {
			return super.getLightValue(blockAccess, x, y, z);
		}
		int data = getQuantaValue(blockAccess, x, y, z);
		return (int) (data / quantaPerBlockFloat * getFluid(blockAccess, x, y, z).getLuminosity());
	}

	@Override
	public int getRenderType() {
		return EQConfig.BLOCK_FLUID_RENDER_ID;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	/*@Override
	public float getBlockBrightness(IBlockAccess world, int x, int y, int z) {
		float lightThis = world.getLightBrightness(x, y, z);
		float lightUp = world.getLightBrightness(x, y + 1, z);
		return lightThis > lightUp ? lightThis : lightUp;
	}*/

	@Override
	public int getMixedBrightnessForBlock(IBlockAccess world, int x, int y, int z) {
		int lightThis = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
		int lightUp = world.getLightBrightnessForSkyBlocks(x, y + 1, z, 0);
		int lightThisBase = lightThis & 255;
		int lightUpBase = lightUp & 255;
		int lightThisExt = lightThis >> 16 & 255;
		int lightUpExt = lightUp >> 16 & 255;
		return (lightThisBase > lightUpBase ? lightThisBase : lightUpBase) | ((lightThisExt > lightUpExt ? lightThisExt : lightUpExt) << 16);
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
		if(Block.getIdFromBlock(blockAccess.getBlock(x, y, z)) != Block.getIdFromBlock(this)) {
			return !blockAccess.getBlock(x, y, z).isOpaqueCube();
		}
		Material mat = blockAccess.getBlock(x, y, z).getMaterial();
		return mat == blockMaterial ? false : super.shouldSideBeRendered(blockAccess, x, y, z, side);
	}

	public int getDensity(IBlockAccess blockAccess, int x, int y, int z) {
		return getFluid(blockAccess, x, y, z).getDensity();
	}

	public int getDensityDirection(IBlockAccess blockAccess, int x, int y, int z) {
		return getFluid(blockAccess, x, y, z).getDensity() > 0 ? -1 : 1;
	}

	public int getTemperature(IBlockAccess blockAccess, int x, int y, int z) {
		return getFluid(blockAccess, x, y, z).getTemperature();
	}

	public double getFlowDirection(IBlockAccess blockAccess, int x, int y, int z) {
		Block block = blockAccess.getBlock(x, y, z);
		if(!block.getMaterial().isLiquid()) {
			return -1000.0;
		}
		Vec3 vec = ((BlockFluidMetadata) block).getFlowVector(blockAccess, x, y, z);
		return vec.xCoord == 0.0D && vec.zCoord == 0.0D ? -1000.0D : Math.atan2(vec.zCoord, vec.xCoord) - Math.PI / 2D;
	}

	public int getQuantaValue(IBlockAccess blockAccess, int x, int y, int z) {
		if(blockAccess.isAirBlock(x, y, z)) {
			return 0;
		}
		if(Block.getIdFromBlock(blockAccess.getBlock(x, y, z)) != Block.getIdFromBlock(this)) {
			return -1;
		}
		return quantaPerBlock - blockAccess.getBlockMetadata(x, y, z);
	}

	public void setQuantaValue(World world, int x, int y, int z, int quantaValue) {
		world.setBlockMetadataWithNotify(x, y, z, (quantaValue - 1) & quantaPerBlock, 2);
	}

	public int getQuantaValueBelow(IBlockAccess world, int x, int y, int z, int belowThis) {
		int quantaRemaining = getQuantaValue(world, x, y, z);
		if(quantaRemaining >= belowThis) {
			return -1;
		}
		return quantaRemaining;
	}

	public int getQuantaValueAbove(IBlockAccess blockAccess, int x, int y, int z, int aboveThis) {
		int quantaRemaining = getQuantaValue(blockAccess, x, y, z);
		if(quantaRemaining <= aboveThis) {
			return -1;
		}
		return quantaRemaining;
	}

	public float getQuantaPercentage(IBlockAccess blockAccess, int x, int y, int z) {
		int quantaRemaining = getQuantaValue(blockAccess, x, y, z);
		return quantaRemaining / quantaPerBlockFloat;
	}

	public Vec3 getFlowVector(IBlockAccess blockAccess, int x, int y, int z) {
		Vec3 vec = blockAccess.getWorldVec3Pool().getVecFromPool(0.0D, 0.0D, 0.0D);
		int decay = quantaPerBlock - getQuantaValue(blockAccess, x, y, z);
		for(int side = 0; side < 4; ++side) {
			int x2 = x;
			int z2 = z;
			switch(side) {
			case 0:
				x2--;
				break;
			case 1:
				z2--;
				break;
			case 2:
				x2++;
				break;
			case 3:
				z2++;
				break;
			}
			int otherDecay = quantaPerBlock - getQuantaValue(blockAccess, x2, y, z2);
			if(otherDecay >= quantaPerBlock) {
				if(!blockAccess.getBlock(x2, y, z2).getMaterial().blocksMovement()) {
					otherDecay = quantaPerBlock - getQuantaValue(blockAccess, x2, y - 1, z2);
					if(otherDecay >= 0) {
						int power = otherDecay - (decay - quantaPerBlock);
						vec = vec.addVector((x2 - x) * power, (y - y) * power, (z2 - z) * power);
					}
				}
			} else if(otherDecay >= 0) {
				int power = otherDecay - decay;
				vec = vec.addVector((x2 - x) * power, (y - y) * power, (z2 - z) * power);
			}
		}
		if(Block.getIdFromBlock(blockAccess.getBlock(x, y + 1, z)) == Block.getIdFromBlock(this)) {
			boolean flag = isBlockSolid(blockAccess, x, y, z - 1, 2) || isBlockSolid(blockAccess, x, y, z + 1, 3) || isBlockSolid(blockAccess, x - 1, y, z, 4) || isBlockSolid(blockAccess, x + 1, y, z, 5) || isBlockSolid(blockAccess, x, y + 1, z - 1, 2) || isBlockSolid(blockAccess, x, y + 1, z + 1, 3) || isBlockSolid(blockAccess, x - 1, y + 1, z, 4) || isBlockSolid(blockAccess, x + 1, y + 1, z, 5);
			if(flag) {
				vec = vec.normalize().addVector(0.0D, -6.0D, 0.0D);
			}
		}
		vec = vec.normalize();
		return vec;
	}

	@Override
	public Fluid getFluid() {
		return getFluid(0);
	}

	@Override
	public float getFilledPercentage(World world, int x, int y, int z) {
		int quantaRemaining = getQuantaValue(world, x, y, z) + 1;
		float remaining = quantaRemaining / quantaPerBlockFloat;
		if(remaining > 1) {
			remaining = 1.0F;
		}
		return remaining * (getFluid(world, x, y, z).getDensity() > 0 ? 1 : -1);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		int fluid = getMetadata(world, x, y, z);
		int quantaRemaining = quantaPerBlock - world.getBlockMetadata(x, y, z);
		int expQuanta = -101;
		if(quantaRemaining < quantaPerBlock) {
			int y2 = y - getDensityDirection(world, x, y, z);
			int blockID = Block.getIdFromBlock(this);
			if(Block.getIdFromBlock(world.getBlock(x, y2, z)) == blockID || Block.getIdFromBlock(world.getBlock(x - 1, y2, z)) == blockID || Block.getIdFromBlock(world.getBlock(x + 1, y2, z)) == blockID || Block.getIdFromBlock(world.getBlock(x, y2, z - 1)) == blockID || Block.getIdFromBlock(world.getBlock(x, y2, z + 1)) == blockID) {
				expQuanta = quantaPerBlock - 1;
			} else {
				int maxQuanta = -100;
				maxQuanta = getLargerQuanta(world, x - 1, y, z, maxQuanta);
				maxQuanta = getLargerQuanta(world, x + 1, y, z, maxQuanta);
				maxQuanta = getLargerQuanta(world, x, y, z - 1, maxQuanta);
				maxQuanta = getLargerQuanta(world, x, y, z + 1, maxQuanta);
				expQuanta = maxQuanta - 1;
			}
			if(expQuanta != quantaRemaining) {
				quantaRemaining = expQuanta;
				if(expQuanta <= 0) {
					world.setBlockToAir(x, y, z);
				} else {
					world.setBlockMetadataWithNotify(x, y, z, quantaPerBlock - expQuanta, 3);
					world.scheduleBlockUpdate(x, y, z, this, tickRate);
					world.notifyBlocksOfNeighborChange(x, y, z, this);
				}
			}
		} else if(quantaRemaining >= quantaPerBlock) {
			world.setBlockMetadataWithNotify(x, y, z, 0, 2);
		}
		if(canDisplace(world, x, y, z, x, y + getDensityDirection(world, x, y, z), z)) {
			flowIntoBlock(world, x, y, z, x, y + getDensityDirection(world, x, y, z), z, 1, fluid);
			return;
		}
		int flowMeta = quantaPerBlock - quantaRemaining + 1;
		if(flowMeta >= quantaPerBlock) {
			return;
		}
		if(isSourceBlock(world, x, y, z) || !isFlowingVertically(world, x, y, z)) {
			if(Block.getIdFromBlock(world.getBlock(x, y - getDensityDirection(world, x, y, z), z)) == Block.getIdFromBlock(this)) {
				flowMeta = 1;
			}
			boolean flowTo[] = getOptimalFlowDirections(world, x, y, z);
			if(flowTo[0]) {
				flowIntoBlock(world, x, y, z, x - 1, y, z, flowMeta, fluid);
			}
			if(flowTo[1]) {
				flowIntoBlock(world, x, y, z, x + 1, y, z, flowMeta, fluid);
			}
			if(flowTo[2]) {
				flowIntoBlock(world, x, y, z, x, y, z - 1, flowMeta, fluid);
			}
			if(flowTo[3]) {
				flowIntoBlock(world, x, y, z, x, y, z + 1, flowMeta, fluid);
			}
		}
	}

	public boolean isFlowingVertically(IBlockAccess world, int x, int y, int z) {
		return Block.getIdFromBlock(world.getBlock(x, y + getDensityDirection(world, x, y, z), z)) == Block.getIdFromBlock(this) || (Block.getIdFromBlock(world.getBlock(x, y, z)) == Block.getIdFromBlock(this) && canFlowInto(world, x, y, z, x, y + getDensityDirection(world, x, y, z), z));
	}

	public boolean isSourceBlock(IBlockAccess world, int x, int y, int z) {
		return Block.getIdFromBlock(world.getBlock(x, y, z)) == Block.getIdFromBlock(this) && world.getBlockMetadata(x, y, z) == 0;
	}

	public boolean[] getOptimalFlowDirections(World world, int x, int y, int z) {
		for(int side = 0; side < 4; side++) {
			flowCost[side] = 1000;
			int x2 = x;
			int y2 = y;
			int z2 = z;

			switch(side) {
			case 0:
				--x2;
				break;
			case 1:
				++x2;
				break;
			case 2:
				--z2;
				break;
			case 3:
				++z2;
				break;
			}
			if(!canFlowInto(world, x, y, z, x2, y2, z2) || isSourceBlock(world, x2, y2, z2)) {
				continue;
			}
			if(canFlowInto(world, x, y, z, x2, y2 + getDensityDirection(world, x, y, z), z2)) {
				flowCost[side] = 0;
			} else {
				flowCost[side] = calculateFlowCost(world, x2, y2, z2, 1, side);
			}
		}
		int min = flowCost[0];
		for(int side = 1; side < 4; side++) {
			if(flowCost[side] < min) {
				min = flowCost[side];
			}
		}
		for(int side = 0; side < 4; side++) {
			isOptimalFlowDirection[side] = flowCost[side] == min;
		}
		return isOptimalFlowDirection;
	}

	public int calculateFlowCost(World world, int x, int y, int z, int recurseDepth, int side) {
		int cost = 1000;
		for(int adjSide = 0; adjSide < 4; adjSide++) {
			if((adjSide == 0 && side == 1) || (adjSide == 1 && side == 0) || (adjSide == 2 && side == 3) || (adjSide == 3 && side == 2)) {
				continue;
			}
			int x2 = x;
			int y2 = y;
			int z2 = z;
			switch(adjSide) {
			case 0:
				--x2;
				break;
			case 1:
				++x2;
				break;
			case 2:
				--z2;
				break;
			case 3:
				++z2;
				break;
			}
			if(!canFlowInto(world, x, y, z, x2, y2, z2) || isSourceBlock(world, x2, y2, z2)) {
				continue;
			}
			if(canFlowInto(world, x, y, z, x2, y2 + getDensityDirection(world, x, y, z), z2)) {
				return recurseDepth;
			}
			if(recurseDepth >= 4) {
				continue;
			}
			int min = calculateFlowCost(world, x2, y2, z2, recurseDepth + 1, adjSide);
			if(min < cost) {
				cost = min;
			}
		}
		return cost;
	}

	public void flowIntoBlock(World world, int x, int y, int z, int otherX, int otherY, int otherZ, int meta, int fluid) {
		if(meta < 0) {
			return;
		}
		if(displaceIfPossible(world, x, y, z, otherX, otherY, otherZ)) {
			world.setBlock(otherX, otherY, otherZ, this, meta, 3);
			setMetadata(world, otherX, otherY, otherZ, fluid);
		}
	}

	public boolean canFlowInto(IBlockAccess blockAccess, int x, int y, int z, int otherX, int otherY, int otherZ) {
		if(blockAccess.isAirBlock(otherX, otherY, otherZ)) {
			return true;
		}
		int id = Block.getIdFromBlock(blockAccess.getBlock(otherX, otherY, otherZ));
		if(id == Block.getIdFromBlock(this)) {
			return true;
		}
		if(displacementIds.containsKey(id)) {
			return displacementIds.get(id);
		}
		Material material = Block.getBlockById(id).getMaterial();
		if(material.blocksMovement() || material == Material.water || material == Material.lava || material == Material.portal) {
			return false;
		}
		int density = getDensity(blockAccess, otherX, otherY, otherZ);
		if(density == Integer.MAX_VALUE) {
			return true;
		}
		if(getDensityDirection(blockAccess, x, y, z) > density) {
			return true;
		} else {
			return false;
		}
	}

	public int getLargerQuanta(IBlockAccess world, int x, int y, int z, int compare) {
		int quantaRemaining = getQuantaValue(world, x, y, z);
		if(quantaRemaining <= 0) {
			return compare;
		}
		return quantaRemaining >= compare ? quantaRemaining : compare;
	}

	@Override
	public FluidStack drain(World world, int x, int y, int z, boolean doDrain) {
		if(!isSourceBlock(world, x, y, z)) {
			return null;
		}
		FluidStack stack = new FluidStack(getFluid(world, x, y, z), FluidContainerRegistry.BUCKET_VOLUME);
		if(doDrain) {
			world.setBlockToAir(x, y, z);
		}
		return stack;
	}

	@Override
	public boolean canDrain(World world, int x, int y, int z) {
		return isSourceBlock(world, x, y, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return getFluid(meta).getFlowingIcon();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
		return isSourceBlock(blockAccess, x, y, z) ? getFluid(getMetadata(blockAccess, x, y, z)).getStillIcon() : getFluid(getMetadata(blockAccess, x, y, z)).getFlowingIcon();
	}
}
