package elcon.mods.elconqore.blocks;

import java.util.ArrayList;

import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elcon.mods.elconqore.EQUtilClient;
import elcon.mods.elconqore.lang.LanguageManager;
import elcon.mods.elconqore.tileentities.TileEntityMetadata;

public abstract class BlockExtendedMetadata extends BlockExtendedContainer {

	public BlockExtendedMetadata(Material material) {
		super(material);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityMetadata();
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return TileEntityMetadata.class;
	}

	public String getLocalizedName(ItemStack stack) {
		return LanguageManager.getLocalization(getUnlocalizedName(stack));
	}

	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName();
	}

	public byte getPlacedMetadata(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side, float xx, float yy, float zz) {
		return (byte) stack.getItemDamage();
	}

	public byte getDroppedMetadata(World world, int x, int y, int z, byte meta, int fortune) {
		return meta;
	}
	
	public boolean shouldDropItems(World world, int x, int y, int z, byte meta, EntityPlayer player, ItemStack stack) {
		return true;
	}

	public byte getMetadata(IBlockAccess blockAccess, int x, int y, int z) {
		TileEntity tile = blockAccess.getTileEntity(x, y, z);
		if(tile instanceof TileEntityMetadata) {
			return ((TileEntityMetadata) tile).getTileMetadata();
		}
		return 0;
	}

	public void setMetadata(World world, int x, int y, int z, byte meta) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TileEntityMetadata) {
			((TileEntityMetadata) tile).setTileMetadata(meta);
		}
	}

	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
		int metadata = getMetadata(world, x, y, z);
		float hardness = getBlockHardness(world, x, y, z);
		if(hardness < 0.0F) {
			return 0.0F;
		}
		if(!ForgeHooks.canHarvestBlock(this, player, metadata)) {
			return player.getBreakSpeed(this, true, metadata) / hardness / 100F;
		} else {
			return player.getBreakSpeed(this, false, metadata) / hardness / 30F;
		}
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		TileEntityMetadata tile = (TileEntityMetadata) getTileEntity(world, x, y, z);
		if(tile != null && !tile.hasDroppped) {
			drops = world.getBlock(x, y, z).getDrops(world, x, y, z, tile.getTileMetadata(), EnchantmentHelper.getFortuneModifier(player));
		}
		boolean hasBeenBroken = world.setBlockToAir(x, y, z);
		if(hasBeenBroken && !world.isRemote && drops.size() > 0 && (player == null || !player.capabilities.isCreativeMode) && shouldDropItems(world, x, y, z, tile.getTileMetadata(), player, player.getCurrentEquippedItem())) {
			float chance = ForgeEventFactory.fireBlockHarvesting(drops, world, this, x, y, z, tile.getTileMetadata(), EnchantmentHelper.getFortuneModifier(player), 1.0F, false, player);
			for(ItemStack drop : drops) {
				if(world.rand.nextFloat() <= chance) {
					dropBlockAsItem(world, x, y, z, drop);
				}
			}
			tile.hasDroppped = true;
		}
		return hasBeenBroken;
	}

	@Override
	public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float chance, int fortune) {
		TileEntityMetadata tile = (TileEntityMetadata) world.getTileEntity(x, y, z);
		if(tile != null && !tile.hasDroppped) {
			super.dropBlockAsItemWithChance(world, x, y, z, meta, chance, fortune);
		}
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		int count = quantityDropped(metadata, fortune, world.rand);
		for(int i = 0; i < count; i++) {
			Item item = getItemDropped(metadata, world.rand, fortune);
			if(item != null) {
				ret.add(new ItemStack(item, 1, getDroppedMetadata(world, x, y, z, (byte) metadata, fortune)));
			}
		}
		return ret;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
		return EQUtilClient.addBlockDestroyEffects(world, x, y, z, meta, effectRenderer, this, getMetadata(world, x, y, z));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer) {
		return EQUtilClient.addBlockHitEffects(world, target, effectRenderer, getMetadata(world, target.blockX, target.blockY, target.blockZ));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
		return getIcon(side, getMetadata(blockAccess, x, y, z));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegistry) {
	}
}