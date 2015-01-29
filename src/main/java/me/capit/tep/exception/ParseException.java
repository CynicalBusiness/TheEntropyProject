package me.capit.tep.exception;

public class ParseException extends Exception {
	private static final long serialVersionUID = 5188890591462592532L;
	
	public ParseException(String msg){
		super("Parser error: "+msg);
	}
}
