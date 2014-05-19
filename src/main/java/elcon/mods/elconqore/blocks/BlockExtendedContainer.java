package elcon.mods.elconqore.blocks;

import java.util.ArrayList;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import elcon.mods.elconqore.lang.LanguageManager;
import elcon.mods.elconqore.tileentities.TileEntityExtended;

public abstract class BlockExtendedContainer extends BlockContainer {

	public BlockExtendedContainer(Material material) {
		super(material);
	}

	@Override
	public String getLocalizedName() {
		return LanguageManager.getLocalization(getUnlocalizedName());
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		TileEntityExtended tile = (TileEntityExtended) getTileEntity(world, x, y, z);
		if(tile != null && !tile.hasDroppped) {
			drops = world.getBlock(x, y, z).getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), EnchantmentHelper.getFortuneModifier(player));
		}
		boolean hasBeenBroken = world.setBlockToAir(x, y, z);
		if(hasBeenBroken && !world.isRemote && drops.size() > 0 && (player == null || !player.capabilities.isCreativeMode)) {
			float chance = ForgeEventFactory.fireBlockHarvesting(drops, world, this, x, y, z, world.getBlockMetadata(x, y, z), EnchantmentHelper.getFortuneModifier(player), 1.0F, false, player);
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
		TileEntityExtended tile = (TileEntityExtended) world.getTileEntity(x, y, z);
		if(tile != null && !tile.hasDroppped) {
			super.dropBlockAsItemWithChance(world, x, y, z, meta, chance, fortune);
		}
	}

	public TileEntity getTileEntity(IBlockAccess blockAccess, int x, int y, int z) {
		TileEntity tile = (TileEntity) blockAccess.getTileEntity(x, y, z);
		BlockContainer block = (BlockContainer) blockAccess.getBlock(x, y, z);
		if(tile == null || (block instanceof BlockExtendedContainer && !tile.getClass().isAssignableFrom(((BlockExtendedContainer) block).getTileEntityClass()))) {
			tile = block.createNewTileEntity(null, blockAccess.getBlockMetadata(x, y, z));
			if(blockAccess instanceof World) {
				tile = block.createNewTileEntity(((World) blockAccess), blockAccess.getBlockMetadata(x, y, z));
				((World) blockAccess).setTileEntity(x, y, z, tile);
			}
		}
		return tile;
	}

	public abstract Class<? extends TileEntity> getTileEntityClass();
}
