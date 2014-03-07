package elcon.mods.elconqore.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import elcon.mods.elconqore.lang.LanguageManager;

public class InventoryBasic implements IInventory {

	protected Container container;
	protected ItemStack[] stacks;
	protected String unlocalizedName;
	
	public InventoryBasic(Container container, int size, String unlocalizedName) {
		this.container = container;
		this.stacks = new ItemStack[size];
		this.unlocalizedName = unlocalizedName;
	}
	
	@Override
	public int getSizeInventory() {
		return stacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return slot >= getSizeInventory() ? null : stacks[slot];
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if(slot < getSizeInventory() && stacks[slot] != null) {
			ItemStack oldStack = stacks[slot];
			stacks[slot] = null;
			return oldStack;
		}
		return null;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if(slot < getSizeInventory() && stacks[slot] != null) {
			ItemStack stack;
			if(stacks[slot].stackSize <= amount) {
				stack = stacks[slot];
				stacks[slot] = null;
				container.onCraftMatrixChanged(this);
				return stack;
			} else {
				stack = stacks[slot].splitStack(amount);
				if(stacks[slot].stackSize == 0) {
					stacks[slot] = null;
				}
				container.onCraftMatrixChanged(this);
				return stack;
			}
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		if(slot < getSizeInventory()) {
			stacks[slot] = stack;
			container.onCraftMatrixChanged(this);
		}
	}

	@Override
	public String getInventoryName() {
		return LanguageManager.getLocalization(unlocalizedName);
	}

	@Override
	public boolean hasCustomInventoryName() {
		return true;
	}	

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}
	
	@Override
	public void markDirty() {
	}
	
	@Override
	public void openInventory() {
	}
	
	@Override
	public void closeInventory() {
	}
	
	public void readFromNBT(NBTTagList list) {
		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) list.getCompoundTagAt(i);
			int slot = tag.getInteger("Slot");
			ItemStack stack = ItemStack.loadItemStackFromNBT(tag);
			if(stack != null && slot >= 0 && slot < getSizeInventory()) {
				stacks[slot] = stack;
			}
		}
	}
	
	public void writeToNBT(NBTTagList list) {
		for(int i = 0; i < getSizeInventory(); i++) {
			if(stacks[i] != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setInteger("Slot", i);
				stacks[i].writeToNBT(tag);
				list.appendTag(tag);
			}
		}
	}
}
