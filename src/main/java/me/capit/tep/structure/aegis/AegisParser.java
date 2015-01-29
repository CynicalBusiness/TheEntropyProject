package me.capit.tep.structure.aegis;

import me.capit.eapi.data.Child;
import me.capit.eapi.data.DataFile;
import me.capit.eapi.data.DataModel;
import me.capit.tep.exception.ParseException;
import me.capit.tep.structure.MetaParser;
import me.capit.tep.structure.StructureMatrix;
import me.capit.tep.structure.StructureParser;

public class AegisParser implements StructureParser  {

	private static final long serialVersionUID = 8528492591084635182L;
	
	private MetaParser meta;
	private AegisDataParser data;
	private StructureMatrix matrix;
	
	private DataFile file;
	
	public AegisParser(DataFile file) throws ParseException {
		if(file == null || !file.getName().equals("aegis")) throw new ParseException("Data file is not of aegis type");
	
		this.file = file;
		meta = new MetaParser(file.findFirstChild("meta"));
		data = new AegisDataParser(file.findFirstChild("data"));
		matrix = new StructureMatrix(file.findFirstChild("matrix"));
		
	}
	
	@Override
	public MetaParser getMeta(){
		return meta;
	}
	
	@Override
	public AegisDataParser getData(){
		return data;
	}
	
	@Override
	public StructureMatrix getMatrix(){
		return matrix;
	}

	@Override
	public DataModel getModel() {
		DataModel model = new DataModel(file.getName());
		for (Child child : file.getChildren()) model.addChild(child);
		return model;
	}
}
