package me.capit.tep.recipe;

import me.capit.eapi.data.Child;
import me.capit.eapi.data.DataModel;
import me.capit.eapi.item.MaterialParser;
import me.capit.eapi.item.Parser;
import me.capit.tep.exception.ParseException;

public class RecipeKeyParser implements Parser {
	private static final long serialVersionUID = 305919823462521171L;

	private MaterialParser material;
	private char character;
	private int amount;
	
	private DataModel model;
	
	public RecipeKeyParser(Child child) throws ParseException {
		if (child==null || !(child instanceof DataModel) || !child.getName().equals("key"))
			throw new ParseException("The given key is not a valid recipe key.");
		model = (DataModel) child;
		
		try {
			material = new MaterialParser(model.getAttribute("material").getValueString());
			character = model.getAttribute("char").getValueString().charAt(0);
			amount = Integer.parseInt(model.getAttribute("amount").getValueString());
		} catch (IllegalArgumentException | NullPointerException e){
			throw new ParseException("The given recipe key contains invalid data.");
		}
	}
	
	public MaterialParser getMaterial(){
		return material;
	}
	
	public char getCharacter(){
		return character;
	}
	
	public int getAmount(){
		return amount;
	}

	@Override
	public DataModel getModel() {
		return model;
	}
}
