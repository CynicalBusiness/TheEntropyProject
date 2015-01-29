package me.capit.tep.structure;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

public class WorldStructureEntity {
	private final Structure structure;
	private final Hologram hologram;
	
	private String[] text;
	
	public WorldStructureEntity(Plugin instance, Structure structure, String... text){
		this.structure = structure;
		this.hologram = HologramsAPI.createHologram(instance, structure.getChestLocation());
		this.text = text;
		hologram.clearLines();
	}
	
	private void update(Map<String, Object> data){
		for (String line : getText(data)){
			hologram.appendTextLine(line);
		}
	}
	
	public void updateText(Map<String, Object> data){
		hologram.clearLines();
		update(data);
	}
	
	public void updateTextWithItem(ItemStack stack, Map<String, Object> data){
		hologram.clearLines();
		hologram.appendItemLine(stack);
		update(data);
	}
	
	public String[] getText(Map<String, Object> data){
		String[] t = new String[text.length];
		for (int i=0; i<t.length; i++){
			String line = text[i];
			Pattern pattern = Pattern.compile("\\%\\{(.[^\\}]*\\}");
			Matcher lm = pattern.matcher(line);
			StringBuffer sb = new StringBuffer(line.length());
			while (lm.find()){
				String dataID = lm.group(1);
				lm.appendReplacement(sb, Matcher.quoteReplacement(data.containsKey(dataID)
						? data.get(dataID).toString() : ChatColor.ITALIC+"null"));
			}
			lm.appendTail(sb);
			t[i] = sb.toString();
		}
		return t;
	}
	
	public String[] getRawText(){
		return text;
	}
	
	public Structure getStructure(){
		return structure;
	}
	
	public Hologram getHologram(){
		return hologram;
	}
}
