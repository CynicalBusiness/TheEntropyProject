package me.capit.tep.structure.aegis;

import me.capit.eapi.data.Child;
import me.capit.eapi.data.DataModel;
import me.capit.tep.exception.ParseException;
import me.capit.tep.structure.StructureDataParser;

public class AegisDataParser implements StructureDataParser {
	private static final long serialVersionUID = -5183503417378686080L;
	
	private double health; private int radius, entropy;
	private DataModel model;
	
	public AegisDataParser(Child child) throws ParseException {
		if(child == null || !(child instanceof DataModel) || !child.getName().equals("data"))
			throw new ParseException("Child is not a data model");
		model = (DataModel) child;
		
		try {
			health = Double.parseDouble(model.getAttribute("health").getValueString());
			radius = Integer.parseInt(model.getAttribute("radius").getValueString());
			entropy = Integer.parseInt(model.getAttribute("entropy").getValueString());
			
		} catch(IllegalArgumentException | NullPointerException e){
			throw new ParseException("Data Attribute not valid");
		}
	}

	public double getHealth(){
		return health;
	}
	
	public int getRadius(){
		return radius;
	}
	
	public int getEntropy(){
		return entropy;
	}

	@Override
	public DataModel getModel() {
		return model;
	}
	
}
