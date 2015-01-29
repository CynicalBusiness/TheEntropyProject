package me.capit.tep.group;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.capit.eapi.data.Child;
import me.capit.eapi.data.Parent;
import me.capit.tep.structure.aegis.Aegis;

public class Group implements Child {
	public static final GroupRank DEFAULT_RANK = GroupRank.MEMBER;
	public static final String NAME_FORMAT = "^[A-Za-z0-9-_]{4,16}$";
	public enum GroupRank implements Serializable {
		MEMBER, ADMINISTRATOR, OWNER;
	}
	
	private static final long serialVersionUID = -3077882311175798499L;
	
	private final UUID id;
	private final Map<UUID, GroupRank> players;
	private final String name;
	
	private Aegis parent;
	
	public Group(String name, UUID owner){
		id = UUID.randomUUID();
		this.name = name;
		players = new TreeMap<UUID, GroupRank>();
		addPlayer(owner);
		setRank(owner, GroupRank.OWNER);
	}

	public OfflinePlayer getOwner(){
		for (OfflinePlayer player : getPlayers()) if (isOwner(player)) return player;
		return null;
	}
	
	public List<OfflinePlayer> getPlayers(){
		List<OfflinePlayer> players = new ArrayList<OfflinePlayer>();
		for (UUID p : this.players.keySet()) players.add(Bukkit.getServer().getOfflinePlayer(p));
		return players;
	}
	
	public GroupRank getRank(OfflinePlayer player){
		return getRank(player.getUniqueId());
	}
	public GroupRank getRank(UUID id){
		return players.get(id);
	}
	
	public boolean isOwner(OfflinePlayer player){
		return isOwner(player.getUniqueId());
	}
	public boolean isOwner(UUID id){
		return isMember(id) && getRank(id)==GroupRank.OWNER;
	}
	
	public boolean isMember(OfflinePlayer player){
		return isMember(player.getUniqueId());
	}
	public boolean isMember(UUID id){
		return getRank(id)!=null;
	}
	
	public boolean isAdmin(OfflinePlayer player){
		return isAdmin(player.getUniqueId());
	}
	public boolean isAdmin(UUID id){
		return isMember(id) && (isOwner(id) || getRank(id)==GroupRank.ADMINISTRATOR);
	}
	
	public void addPlayer(OfflinePlayer player){
		addPlayer(player.getUniqueId());
	}
	public void addPlayer(UUID player){
		players.put(player, DEFAULT_RANK);
	}
	
	public void removePlayer(OfflinePlayer player){
		removePlayer(player.getUniqueId());
	}
	public void removePlayer(UUID id){
		players.remove(id);
	}
	
	public void setRank(OfflinePlayer player, GroupRank rank){
		setRank(player.getUniqueId(), rank);
	}
	public void setRank(UUID player, GroupRank rank){
		if (isMember(player)) players.put(player, rank);
	}
	
	public void promotePlayer(OfflinePlayer player){
		promotePlayer(player.getUniqueId());
	}
	public void promotePlayer(UUID id){
		if (isMember(id)) setRank(id, GroupRank.ADMINISTRATOR);
	}
	
	public void demotePlayer(OfflinePlayer player){
		demotePlayer(player.getUniqueId());
	}
	public void demotePlayer(UUID id){
		if (isMember(id)) setRank(id, GroupRank.MEMBER);
	}
	
	public void clearPlayers(){
		OfflinePlayer owner = getOwner();
		players.clear();
		players.put(owner.getUniqueId(), GroupRank.OWNER);
	}
	
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public UUID getUniqueID() {
		return id;
	}

	@Override
	public void setName(String arg0) {
		// Nothing
	}

	@Override
	public Aegis getParent() {
		return parent;
	}

	@Override
	public void setParent(Parent arg0) {
		// Nothing
	}
	
	@Override
	public boolean equals(Object obj){
		return obj instanceof Group && ((Group) obj).getUniqueID().equals(getUniqueID());
	}
	
	public void setParent(Aegis aegis) {
		this.parent = aegis;
	}

}
