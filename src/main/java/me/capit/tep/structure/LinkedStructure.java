package me.capit.tep.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.capit.eapi.data.Child;
import me.capit.eapi.data.Parent;

public interface LinkedStructure extends Structure, Parent {

	public default List<LinkableStructure> getLinks(){
		List<LinkableStructure> links = new ArrayList<LinkableStructure>();
		for (Child child : getChildren()){
			if (child instanceof LinkableStructure) links.add((LinkableStructure) child);
		}
		return links;
	}
	
	public default void addChild(Child child){
		if (child instanceof LinkableStructure){
			getChildren().add(child);
		}
	}
	
	public default LinkableStructure getChild(UUID id){
		for (Child child : getChildren()) if (child.getUniqueID().equals(id) && (child instanceof LinkableStructure))
			return (LinkableStructure) child;
		return null;
	}
	
	public default void breakLinks(){
		for (Child structure : getChildren()) structure.setParent(null);
	}
	
}
