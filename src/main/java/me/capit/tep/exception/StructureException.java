package me.capit.tep.exception;

import me.capit.tep.structure.Structure;

public class StructureException extends Exception {
	private static final long serialVersionUID = 5188890591462592532L;
	
	public StructureException(Structure structure, String msg){
		super(structure.getName()+": "+msg);
	}
}
