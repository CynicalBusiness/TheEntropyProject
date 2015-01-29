package me.capit.tep.structure;

import me.capit.eapi.data.Child;
import me.capit.eapi.data.Parent;

public interface LinkableStructure extends Structure, Child {

	public LinkedStructure getLink();
	public void setLink(LinkedStructure structure);
	
	@Override
	public default LinkedStructure getParent(){
		return getLink();
	}
	
	@Override
	public default void setParent(Parent parent){
		if (parent instanceof LinkedStructure) setLink((LinkedStructure) parent);
	}
	
}
