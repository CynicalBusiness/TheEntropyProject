package me.capit.tep.command;

import java.util.List;

import me.capit.tep.TEPPlugin;
import me.capit.tep.group.Group;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGroup implements CommandExecutor {

	private TEPPlugin plugin;
	
	public CommandGroup(TEPPlugin plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String lbl, String[] args) {
		if (cmd.getName().equalsIgnoreCase("groups") && s instanceof Player){
			Player player = (Player) s;
			if (args.length==0 || (args.length==1 && args[0].equalsIgnoreCase("help"))){
				player.sendMessage(new String[]{
						"Groups help file:",
						"/group list  "+ChatColor.GRAY+"Lists all of your groups.",
						"/group create <name>  "+ChatColor.GRAY+"Creates a group.",
						"/group delete <group>  "+ChatColor.GRAY+"Deletes a group.",
						"/group clear <group>  "+ChatColor.GRAY+"Clears all players in a group.",
						"/group players <group>  "+ChatColor.GRAY+"Lists players in a group.",
						"/group add <group> <player>  "+ChatColor.GRAY+"Adds a player.",
						"/group remove <group> <player>  "+ChatColor.GRAY+"Removes a player.",
						"/group [promote|admin] <group> <player>  "+ChatColor.GRAY+"Promotes a player.",
						"/group [demote|member] <group> <player>  "+ChatColor.GRAY+"Demotes a player."
				});
				return true;
			} else if (args.length==1){
				if (args[0].equalsIgnoreCase("list")){
					List<Group> groups = plugin.getGroupsOf(player);
					if (groups.size()==0){ player.sendMessage(ChatColor.YELLOW+"You are not in any groups."); return true; }
					player.sendMessage("Your groups:");
					for (Group group : groups){
						player.sendMessage(group.getName()+" - "+(group.isOwner(player) ? ChatColor.GOLD+"Owner"
								: (group.isAdmin(player) ? ChatColor.RED+"Admin" : ChatColor.GREEN+"Member")));
					}
					return true;
				}
			} else if (args.length==2){
				if (args[0].equalsIgnoreCase("create") && args[1].matches(Group.NAME_FORMAT)){
					if (plugin.getGroup(args[1])!=null){
						player.sendMessage(ChatColor.RED+"A group by that name already exists.");
						return true;
					}
					Group group = new Group(args[1], player.getUniqueId());
					plugin.addGroup(group);
					player.sendMessage(ChatColor.GREEN+"Created group "+ChatColor.ITALIC+group.getName());
					return true;
				} else if (args[0].equalsIgnoreCase("delete")){
					Group group = plugin.getGroup(args[1]);
					if (group!=null && group.isOwner(player)){
						plugin.removeGroup(group);
						player.sendMessage(ChatColor.YELLOW+"Group deleted.");
					} else {
						player.sendMessage(ChatColor.RED+"That group does not exist or you are not the group owner.");
					}
					return true;
				} else if (args[0].equalsIgnoreCase("clear")){
					Group group = plugin.getGroup(args[1]);
					if (group!=null && group.isOwner(player)){
						group.clearPlayers();
						player.sendMessage(ChatColor.YELLOW+"Group cleared.");
					} else {
						player.sendMessage(ChatColor.RED+"That group does not exist or you are not the group owner.");
					}
					return true;
				} else if (args[0].equalsIgnoreCase("players")){
					Group group = plugin.getGroup(args[1]);
					if (group!=null && group.isMember(player)){
						player.sendMessage(group.getPlayers().size()+" player(s) in "+ChatColor.ITALIC+group.getName()+ChatColor.RESET+":");
						for (OfflinePlayer p : group.getPlayers()){
							player.sendMessage(p.getName()+" - "+(group.isOwner(p) ? ChatColor.GOLD+"Owner"
									: (group.isAdmin(p) ? ChatColor.RED+"Admin" : ChatColor.GREEN+"Member")));
						}
					} else {
						player.sendMessage(ChatColor.RED+"That group does not exist or you are not a group member.");
					}
					return true;
				}
			} else if (args.length==3){
				if (args[0].equalsIgnoreCase("add")){
					Group group = plugin.getGroup(args[1]);
					if (group!=null && group.isAdmin(player)){
						OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[2]);
						if (target!=null || group.isMember(target)){
							group.addPlayer(target.getUniqueId());
							player.sendMessage(ChatColor.GREEN+"Added "+target.getName()+" to the group.");
						} else {
							player.sendMessage(ChatColor.RED+"The specified player was not found or already in the group.");
						}
					} else {
						player.sendMessage(ChatColor.RED+"That group does not exist or you are not a group admin.");
					}
					return true;
				} else if (args[0].equalsIgnoreCase("remove")){
					Group group = plugin.getGroup(args[1]);
					if (group!=null && group.isAdmin(player)){
						OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[2]);
						if (target!=null || !group.isMember(target)){
							group.removePlayer(target.getUniqueId());
							player.sendMessage(ChatColor.GREEN+"Removed "+target.getName()+" from the group.");
						} else {
							player.sendMessage(ChatColor.RED+"The specified player is not in the group.");
						}
					} else {
						player.sendMessage(ChatColor.RED+"That group does not exist or you are not a group admin.");
					}
					return true;
				} else if (args[0].equalsIgnoreCase("promote") || args[0].equalsIgnoreCase("admin")){
					Group group = plugin.getGroup(args[1]);
					if (group!=null && group.isOwner(player)){
						OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[2]);
						if (target!=null && group.isMember(target.getUniqueId())){
							group.promotePlayer(target.getUniqueId());
							player.sendMessage(ChatColor.GREEN+"Promoted "+target.getName()+" to admin.");
						} else {
							player.sendMessage(ChatColor.RED+"The specified player was not found or not in the group.");
						}
					} else {
						player.sendMessage(ChatColor.RED+"That group does not exist or you are not the group owner.");
					}
					return true;
				} else if (args[0].equalsIgnoreCase("demote") || args[0].equalsIgnoreCase("member")){
					Group group = plugin.getGroup(args[1]);
					if (group!=null && group.isOwner(player)){
						OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[2]);
						if (target!=null && group.isMember(target.getUniqueId())){
							group.demotePlayer(target.getUniqueId());
							player.sendMessage(ChatColor.GREEN+"Demoted "+target.getName()+" to member.");
						} else {
							player.sendMessage(ChatColor.RED+"The specified player was not found or not in the group.");
						}
					} else {
						player.sendMessage(ChatColor.RED+"That group does not exist or you are not the group owner.");
					}
					return true;
				}
			}
		}
		return false;
	}

}
