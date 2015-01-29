package me.capit.tep.structure;

import org.bukkit.ChatColor;

import me.capit.eapi.data.Child;
import me.capit.eapi.data.DataModel;
import me.capit.eapi.item.Parser;
import me.capit.tep.exception.ParseException;

public class MetaParser implements Parser {
	private static final long serialVersionUID = 732881062976087837L;
	
	private String name, displayName, description;
	private DataModel model;
	
	public MetaParser(Child child) throws ParseException {
		if (child==null || !(child instanceof DataModel) || !child.getName().equals("meta"))
			throw new ParseException("Child is not a meta model.");
		model = (DataModel) child;
		
		try {
			name = model.getAttribute("name").getValueString();
			displayName = model.getAttribute("display_name").getValueString();
			description = model.getAttribute("description").getValueString();
		} catch (NullPointerException e){
			throw new ParseException("Invalid meta model");
		}
	}

	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return ChatColor.translateAlternateColorCodes('&', description);
	}
	
	public String getRawDescription(){
		return description;
	}
	
	public String getDisplayName(){
		return ChatColor.translateAlternateColorCodes('&', displayName);
	}
	
	public String getRawDisplayName(){
		return displayName;
	}

	@Override
	public DataModel getModel() {
		return model;
	}
}
