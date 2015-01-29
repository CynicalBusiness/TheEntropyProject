package me.capit.tep.structure;

import me.capit.eapi.item.Parser;

public interface StructureParser extends Parser {

	public MetaParser getMeta();
	public StructureDataParser getData();
	public StructureMatrix getMatrix();
	
}
