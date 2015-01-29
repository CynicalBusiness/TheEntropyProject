package me.capit.tep;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import me.capit.eapi.DataHandler;
import me.capit.eapi.data.DataFile;
import me.capit.eapi.io.EntropyConsole;
import me.capit.tep.async.AsyncEventRunner;
import me.capit.tep.command.CommandAegis;
import me.capit.tep.command.CommandGroup;
import me.capit.tep.exception.ParseException;
import me.capit.tep.group.Group;
import me.capit.tep.structure.aegis.Aegis;
import me.capit.tep.structure.aegis.AegisParser;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdom2.JDOMException;

public class TEPPlugin extends JavaPlugin {
	public static final String filename = "coredata";
	
	private EntropyConsole console;
	
	//private Map<String, RecipeParser> recipes = new TreeMap<>();
	private List<AegisParser> aegises = new ArrayList<>();
	
	private volatile DataFile groups;
	
	@Override
	public void onEnable(){
		if (!Bukkit.getPluginManager().isPluginEnabled("EntropyAPI") || !Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")){
			getLogger().severe("! Could not load the EntropyAPI and/or HolographicDisplays !");
			getLogger().severe("!   Please ensure both plugins are installed and enabled   !");
			setEnabled(false);
			return;
		}
		
		console = EntropyConsole.getPluginConsole(this);
		console.setHeaderEnabled(false);
		console.info("Enabling &6The_Entropy_Project &fplugin...");
		
		console.info("Initializing content...");
		saveDefaultResource("data/recipes/steel.xml");
		saveDefaultResource("data/structures/aegises/tier1.xml");
		saveDefaultResource("data/structures/aegises/tier2.xml");
		File dataDir = new File(getDataFolder(), "data");
		//File recipeDir = new File(dataDir, "recipes");
		File structuresDir = new File(dataDir, "structures");
			File aegisDir = new File(structuresDir, "aegises");
		getCommand("groups").setExecutor(new CommandGroup(this));
		getCommand("aegis").setExecutor(new CommandAegis(this));
        new AsyncEventRunner(this).runTaskTimerAsynchronously(this, 0, 1);

		
		console.info("");
		console.info("Loading aegises...");
        //noinspection ConstantConditions
        for (File file : aegisDir.listFiles()){
			try {
				DataFile df = DataHandler.loadXMLFile(this, dataDir.getName()+File.separator+structuresDir.getName()
						+File.separator+aegisDir.getName()+File.separator+file.getName().replaceAll("\\.[^\\.]*$", ""));
				AegisParser ap = new AegisParser(df);
				aegises.add(ap);
				console.info("> Loaded "+ap.getMeta().getDisplayName());
			} catch (JDOMException | IOException e) {
				console.warn("Failed to parse "+file.getName());
				e.printStackTrace();
			} catch (ParseException e) {
				console.warn("Could not build from "+file.getName()+":");
				console.info("  > "+e.getMessage());
			}
		}
		
		console.info("");
		console.info("Loading groups...");
		try {
			groups = DataHandler.loadFile(this, filename);
			console.info("Loaded &e"+groups.getChildren().size()+" &fgroup(s).");
		} catch (ClassNotFoundException | ClassCastException | IOException e) {
			console.error("Existing data could not be loaded!");
			e.printStackTrace();
		}
		
		console.info("");
		console.info("Done!");
		console.setHeaderEnabled(true);
	}
	
	@Override
	public void onDisable(){
		console.setHeaderEnabled(false);
		console.info("Disabling &6The_Entropy_Project &fplugin...");
		
		console.info("");
		console.info("Saving groups...");
		try {
			groups.save(this);
			console.info("Saved!");
		} catch (IOException e) {
			console.error("Could not save structures!");
			e.printStackTrace();
		}
		
		console.setHeaderEnabled(true);
	}
	
	public void saveDefaultResource(String resource){
		if (!new File(getDataFolder(), resource).exists()){
			this.saveResource(resource, false);
			console.info("Default resource at /"+resource+" saved.");
		}
	}
	
	// GROUPS
	public List<Group> getGroups(){
		return groups.getChildren().stream().filter(t -> t instanceof Group)
                .map(t -> (Group) t).collect(Collectors.toList());
	}
	
	public List<Group> getGroupsOf(Player player){
		return getGroupsOf(player.getUniqueId());
	}
	public List<Group> getGroupsOf(UUID id){
        return getGroups().stream().filter(t -> t.isMember(id))
                .collect(Collectors.toList());
	}
	
	public Group getGroup(String name){
		for (Group group : getGroups()) if (group.getName().equalsIgnoreCase(name)) return group;
		return null;
	}
	
	public void addGroup(Group group){
		groups.addChild(group);
	}
	
	public void removeGroup(Group group){
		groups.removeChild(group);
	}
	
	// AEGISES
	public Aegis createAeigs(Group group, Location location){
		for (AegisParser parser : aegises){
			try {
				return new Aegis(parser, location, group);
			} catch (IllegalStateException | ParseException e){
				// Nothing
			}
		}
		return null;
	}
}
