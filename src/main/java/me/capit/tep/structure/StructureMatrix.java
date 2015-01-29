package me.capit.tep.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.capit.eapi.data.Child;
import me.capit.eapi.data.DataModel;
import me.capit.eapi.item.MaterialParser;
import me.capit.eapi.math.Vector3;
import me.capit.tep.exception.ParseException;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class StructureMatrix implements Serializable {
	private static final long serialVersionUID = 1060523744586829162L;
	private final Vector3 dims,chestLoc;
	private final DataModel[][][] matrix;
	
	public StructureMatrix(Child child) throws ParseException {
		if (child==null || !(child instanceof DataModel))
			throw new ParseException("Matrix element is missing or not of correct type.");
		DataModel model = (DataModel) child;
		
		try {
			dims = new Vector3(
					Double.parseDouble(model.getAttribute("width").getValueString()),
					Double.parseDouble(model.getAttribute("height").getValueString()),
					Double.parseDouble(model.getAttribute("depth").getValueString()));
			chestLoc = new Vector3(
					Double.parseDouble(model.getAttribute("chestX").getValueString()),
					Double.parseDouble(model.getAttribute("chestY").getValueString()),
					Double.parseDouble(model.getAttribute("chestZ").getValueString()));
		} catch (NullPointerException | IllegalArgumentException e){
			throw new ParseException("Matrix definition is not valid.");
		}
		
		ParseException matrixErr = new ParseException("Matrix is not of correct size: "+dims.x+","+dims.y+","+dims.z);
		try {
			matrix = new DataModel[(int) dims.y][(int) dims.z][(int) dims.x];
			//for (int j = 0; j<dims.y; j++) for (int k = 0; k<dims.z; k++) for (int l = 0; l<dims.x; l++) 
			//	matrix[j][k][l] = (DataModel) ((DataModel) ((DataModel) model.getChildren().get(j)).getChildren().get(k)).getChildren().get(l);
			
			for (int j=0; j<dims.y; j++){
				DataModel[][] y = new DataModel[(int) dims.z][(int) dims.x];
				DataModel cy = (DataModel) model.getChildren().get(j);
				for (int k=0; k<dims.z; k++){
					DataModel[] z = new DataModel[(int) dims.x];
					DataModel cz = (DataModel) cy.getChildren().get(k);
					for (int l=0; l<dims.x; l++){
						DataModel cx = (DataModel) cz.getChildren().get(l);
						z[l] = cx;
					}
					y[k] = z;
				}
				matrix[j] = y;
			}
				
			if (!isValid(Material.CHEST, (byte) 0, chestLoc)) throw new ParseException("Chest is not in the correct location.");
		} catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException e){
			throw matrixErr;
		}
		if (glitchInMatrix()) throw matrixErr;

	}
	
	private boolean glitchInMatrix(){ // Huehuehue.
		for (DataModel[][] e2 : matrix) for (DataModel[] e1 : e2) for (DataModel e : e1) if (e==null) return true;
		return false;
	}
	
	public DataModel getElementAtPosition(Vector3 pos){
		return matrix[(int) pos.y][(int) pos.z][(int) pos.x];
	}
	
	public MaterialParser getParserAtPosition(Vector3 pos){
		DataModel ise = getElementAtPosition(pos);
		return new MaterialParser(ise.hasAttribute("material") ? ise.getAttribute("material").getValueString() : null);
	}
	
	public ItemStack[] getStacksAtPosition(Vector3 pos){
		return getParserAtPosition(pos).getStacks();
	}
	
	public boolean isValid(ItemStack stack, Vector3 pos){
		return getParserAtPosition(pos).isInput(stack, true);
	}
	public boolean isValid(Material mat, byte data, Vector3 pos){
		return isValid(new ItemStack(mat, 1, data), pos);
	}
	
	public List<Vector3> getLocationsOfMaterial(Material mat, int data){
		List<Vector3> ps = new ArrayList<Vector3>();
		for (int y = 0; y<dims.y; y++){ for (int z = 0; z<dims.z; z++){ for (int x = 0; x<dims.x; x++){
			Vector3 curpos = new Vector3(x,y,z);
			MaterialParser parser = getParserAtPosition(curpos);
			if (parser.size()==1 && parser.getStacks()[0].getType()==mat) ps.add(curpos);
		}}}
		return ps;
	}
	
	@SuppressWarnings("deprecation")
	public boolean isValid(Chest chest){
		Vector3 origin = getRelative(chestLoc.inverse(), chest);
		World world = chest.getWorld();
		for (int y = 0; y<dims.y; y++){ for (int z = 0; z<dims.z; z++){ for (int x = 0; x<dims.x; x++){
			Vector3 rel = getRelative(new Vector3(x,y,z), chest).add(origin).add(new Vector3(chest.getX(), chest.getY(), chest.getZ()));
			Block worldBlock = new Location(world, rel.x, rel.y, rel.z).getBlock();
			if (!isValid(worldBlock.getType(), worldBlock.getData(), new Vector3(x,y,z))) return false;
		}}}
		return true;
	}
	
	public Vector3 getDims(){
		return dims;
	}
	
	public Vector3 getChestLocation(){
		return chestLoc;
	}
	
	public static Vector3 getRelative(Vector3 vec, Chest chest){
		MaterialData cdata = chest.getData();
		switch(((org.bukkit.material.Chest) cdata).getFacing()){
		case NORTH:
			return new Vector3(-vec.x,vec.y,vec.z);
		case SOUTH:
			return new Vector3(vec.x,vec.y,-vec.z);
		case EAST:
			return new Vector3(-vec.z,vec.y,-vec.x);
		case WEST:
			return new Vector3(vec.z,vec.y,vec.x);
		default:
			return vec;
		}
	}
}
