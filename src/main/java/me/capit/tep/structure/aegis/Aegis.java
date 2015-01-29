package me.capit.tep.structure.aegis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.plugin.Plugin;

import me.capit.eapi.data.Child;
import me.capit.eapi.data.Model;
import me.capit.eapi.math.Vector3;
import me.capit.tep.exception.ParseException;
import me.capit.tep.group.Group;
import me.capit.tep.structure.LinkedStructure;
import me.capit.tep.structure.Structure;
import me.capit.tep.structure.StructureMatrix;
import me.capit.tep.structure.WorldStructureEntity;

public class Aegis implements LinkedStructure {
	private static final long serialVersionUID = -1550877944149557852L;

	private AegisParser parser;
	
	private UUID id, world;
	private Vector3 loc;
	private double health;
	private int entropy;
	private boolean enabled;
	private List<Child> children;
	private WorldStructureEntity entity;
	private Group group;
	
	public Aegis(AegisParser parser, Location loc, Group group) throws IllegalStateException, ParseException {
		id = UUID.randomUUID();
		this.world = loc.getWorld().getUID();
		this.loc = new Vector3(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		this.group = group;
		children = new ArrayList<>();
		
		this.parser = parser;
		if (!valid()) throw new ParseException("Matrix is not valid for this aegis.");
		
		children = new ArrayList<>();
		health = getMaxHealth();
		entropy = getMaxEntropy();
		group.setParent(this);
        enabled = true;
	}
	
	public AegisParser getParser(){
		return parser;
	}
	
	public boolean valid(){
		return parser.getMatrix().isValid(getChest());
	}
	
	public Group getGroup(){ return group; }

	@Override
	public double getMaxHealth() {
		return parser.getData().getHealth();
	}

	@Override
	public double getHealth() {
		return health;
	}

	@Override
	public double healthRegenPerSecond() {
		return 0;
	}

	@Override
	public void takeDamage(double damage) {
		health-=damage;
		if (health<=0) breakLinks();
	}

	@Override
	public StructureMatrix getMatrix() {
		return parser.getMatrix();
	}

	@Override
	public Model getModel() {
		return parser.getModel();
	}

	@Override
	public int getEntropyLossPerSecond() {
		return 0;
	}

	@Override
	public int getEntropyGainPerSecond() {
		return 0;
	}

	@Override
	public int getEntropy() {
		return entropy;
	}

	@Override
	public int getMaxEntropy() {
		return parser.getData().getEntropy();
	}

	@Override
	public boolean canDischargeEntropy() {
		return true;
	}

	@Override
	public boolean online() {
		return health>0;
	}

	@Override
	public boolean enabled() {
		return enabled;
	}

	@Override
	public int getLevel() {
		return Integer.parseInt(parser.getMeta().getName().substring(4));
	}

	@Override
	public int getMaxLevel() {
		return Structure.MAX_AEGIS;
	}

	@Override
	public int getTeir() {
		return getLevel();
	}

	@Override
	public Chest getChest() throws IllegalStateException {
		Block block = new Location(Bukkit.getWorld(world), loc.x, loc.y, loc.z).getBlock();
		if (block.getType()!=Material.CHEST) throw new IllegalStateException("Chest location is no longer a chest!");
		return (Chest) block.getState();
	}

	@Override
	public int getPassiveSoundDelay() {
		return 2;
	}
	
	@Override
	public void playPassiveSound() {
		getWorld().playSound(getChestLocation(), Sound.BLAZE_BREATH, 0.3f, 0f);
	}

	@Override
	public void playActiveSound() {
		playPassiveSound();
	}

	@Override
	public void playActivationSound() {
		getWorld().playSound(getChestLocation(), Sound.AMBIENCE_THUNDER, 1f, 2f);
	}

	@Override
	public void playDeactivationSound() {
		playActivationSound();
	}

	@Override
	public String getName() {
		return "aegis-"+parser.getMeta().getName();
	}

	@Override
	public UUID getUniqueID() {
		return id;
	}

	@Override
	public void setName(String name) {
		// Nothing
	}

	@Override
	public Aegis getAegis() {
		return this;
	}

	@Override
	public List<Child> getChildren() {
		return children;
	}

	@Override
	public void registerWorldEntity(Plugin plugin) {
		entity = new WorldStructureEntity(plugin, this, "Testing %{name}");
	}
	
	@Override
	public WorldStructureEntity getWorldEntity() {
		return entity;
	}
	
	@Override
	public void updateWorldEntity(){
		Map<String,Object> data = new TreeMap<>();
		
		data.put("name", parser.getMeta().getDisplayName());
		
		if (entity!=null) entity.updateText(data);
	}

	@Override
	public boolean hasBlock(Location loc) {
		// TODO Auto-generated method stub
		return false;
	}
}
