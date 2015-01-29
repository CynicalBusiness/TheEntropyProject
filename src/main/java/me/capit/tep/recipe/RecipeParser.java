package me.capit.tep.recipe;

import java.util.ArrayList;
import java.util.List;

import me.capit.eapi.data.Child;
import me.capit.eapi.data.DataFile;
import me.capit.eapi.data.DataModel;
import me.capit.eapi.item.Parser;
import me.capit.tep.exception.ParseException;

public class RecipeParser implements Parser {
	
	private static final long serialVersionUID = 8659298016145430493L;

	private final List<RecipeKeyParser> keys = new ArrayList<RecipeKeyParser>();
	private DataFile file;
	
	public RecipeParser(DataFile file) throws ParseException {
		if (file==null || !file.getName().equals("recipe")) throw new ParseException("Model not a recipe or is not valid.");
		
		this.file = file;
		Child kc = file.findFirstChild("keys");
		if (kc==null || !(kc instanceof DataModel)) throw new ParseException("Model lacks a key set!");
		DataModel keys = (DataModel) kc;
		for (Child child : keys.getChildren()){
			this.keys.add(new RecipeKeyParser(child));
		}
	}
	
	public List<RecipeKeyParser> getKeys(){
		return keys;
	}

	@Override
	public DataModel getModel() {
		DataModel model = new DataModel(file.getName());
		for (Child child : file.getChildren()) model.addChild(child);
		return model;
	}
	
}
