package me.capit.tep.recipe;

import java.io.Serializable;

import me.capit.eapi.item.MaterialParser;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface Recipe extends Serializable {

	public default MaterialParser getMaterial(int slot) throws IndexOutOfBoundsException { return getMaterials()[slot]; }
	public default short getDataValue(int slot) throws IndexOutOfBoundsException { return getDataValues()[slot]; }
	
	public int size();
	
	public MaterialParser[] getMaterials();
	public short[] getDataValues();
	
	public default boolean isInput(Inventory inv){ return isInput(inv.getContents()); }
	public boolean isInput(ItemStack[] stacks);
	
	public ItemStack[] getOutput();
	
}
