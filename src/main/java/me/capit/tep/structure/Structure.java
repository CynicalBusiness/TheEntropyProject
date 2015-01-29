package me.capit.tep.structure;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.plugin.Plugin;

import me.capit.eapi.data.Instance;
import me.capit.eapi.data.Model;
import me.capit.eapi.math.Vector3;
import me.capit.tep.structure.aegis.Aegis;

public interface Structure extends Instance {
	public static final int MAX_AEGIS = 5;
	
	public double getMaxHealth();
	public double getHealth();
	public double healthRegenPerSecond();
	public void takeDamage(double damage);
	
	public StructureMatrix getMatrix();
	public Model getModel();
	
	public int getEntropyLossPerSecond();
	public int getEntropyGainPerSecond();
	public int getEntropy();
	public int getMaxEntropy();
	public boolean canDischargeEntropy();
	
	public boolean online();
	public boolean enabled();
	
	public int getLevel();
	public int getMaxLevel();
	
	public int getTeir();
	
	public Aegis getAegis();
	
	public int getPassiveSoundDelay();
	public void playPassiveSound();
	public void playActiveSound();
	public void playActivationSound();
	public void playDeactivationSound();
	
	public void registerWorldEntity(Plugin plugin);
    public default boolean hasWorldEntity(){ return getWorldEntity()!=null; }
	public WorldStructureEntity getWorldEntity();
	public void updateWorldEntity();
	
	public default boolean hasBlock(Block block){ return hasBlock(block.getLocation()); }
	public boolean hasBlock(Location loc);
	
	public Chest getChest() throws IllegalStateException;
	public default Vector3 getChestOffset(){
		return getMatrix().getChestLocation();
	}
	public default World getWorld() throws IllegalStateException { return getChest().getWorld(); }
	public default Vector3 getChestVector() throws IllegalStateException {
		return new Vector3(getChestLocation().getBlockX(), getChestLocation().getBlockY(), getChestLocation().getBlockZ());
	}
	public default Location getChestLocation() throws IllegalStateException {
		return getChest().getLocation(); 
	}
	public default Vector3 getOriginVector() throws IllegalStateException {
		return getChestVector().subtract(StructureMatrix.getRelative(getChestOffset(), getChest()));
	}
	public default Location getOrigin() throws IllegalStateException {
		Vector3 loc = getOriginVector();
		return new Location(getChest().getWorld(), loc.x, loc.y, loc.z);
	}
}
