package me.capit.tep.command;

import me.capit.tep.TEPPlugin;
import me.capit.tep.group.Group;
import me.capit.tep.structure.aegis.Aegis;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAegis implements CommandExecutor {

	private TEPPlugin plugin;
	public CommandAegis(TEPPlugin plugin){
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String lbl, String[] args) {
		if (cmd.getName().equalsIgnoreCase("aegis") && s instanceof Player){
			Player player = (Player) s;
			if (args.length==0 || (args.length==1 && args[0].equalsIgnoreCase("help"))){
				player.sendMessage(new String[]{
						"Aegis help file:",
						"/aegis create <group>  "+ChatColor.GRAY+"Creates a aegis.",
						"/aegis delete <group>  "+ChatColor.GRAY+"Deletes a aegis.",
				});
				return true;
			} else if (args.length==1){
				
			} else if (args.length==2){
				if (args[0].equalsIgnoreCase("create")){
					Group group = plugin.getGroup(args[1]);
					if (group!=null && group.isAdmin(player)){
						if (group.getParent()!=null){ player.sendMessage(ChatColor.YELLOW+"That group already has an aegis."); return true; }
						Block block = player.getTargetBlock(null, 10);
						if (block!=null && block.getType()==Material.CHEST){
							Aegis aegis = plugin.createAeigs(group, block.getLocation());
							player.sendMessage(aegis!=null
									? ChatColor.GREEN+"Successfully created "+aegis.getParser().getMeta().getDisplayName()+ChatColor.GREEN+"."
									: ChatColor.RED+"Target chest is not a valid aegis.");
						} else {
							player.sendMessage(ChatColor.RED+"You must be looking at a nearby chest.");
						}
					} else {
						player.sendMessage(ChatColor.RED+"The group does not exist or you are not a group admin.");
					}
					return true;
				} else if (args[0].equalsIgnoreCase("delete")){
					Group group = plugin.getGroup(args[1]);
					if (group!=null && group.isAdmin(player)){
						Aegis aegis = group.getParent();
						if (aegis==null){ player.sendMessage(ChatColor.YELLOW+"That group does not have an aegis."); return true; }
						aegis.breakLinks();
						group.setParent(null);
						player.sendMessage(ChatColor.YELLOW+"Aegis removed from the group.");
					} else {
						player.sendMessage(ChatColor.RED+"The group does not exist or you are not a group admin.");
					}
					return true;
				}
			} else if (args.length==3){
				
			}
		}
		return false;
	}

}
