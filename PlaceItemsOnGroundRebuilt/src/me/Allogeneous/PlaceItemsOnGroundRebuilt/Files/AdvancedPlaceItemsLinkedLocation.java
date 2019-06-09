package me.Allogeneous.PlaceItemsOnGroundRebuilt.Files;

import java.util.UUID;
import java.util.AbstractMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import me.Allogeneous.PlaceItemsOnGroundRebuilt.PlaceItemsUtils;

public class AdvancedPlaceItemsLinkedLocation{
	
	private PlaceItemsPlayerPlaceLocation[] props;
	
	public AdvancedPlaceItemsLinkedLocation(){
		this.props = new PlaceItemsPlayerPlaceLocation[6];
	}

	public void addProp(UUID placer, Location prop, String blockFace) {
		props[PlaceItemsUtils.getPropInexFromBlockFace(blockFace)] = new PlaceItemsPlayerPlaceLocation(placer, prop);
		
	}
	
	public PlaceItemsPlayerPlaceLocation[] getProps() {
		return props;
	}

	public void setProps(PlaceItemsPlayerPlaceLocation[] props) {
		this.props = props;
	}
	
	@Deprecated
	public void setPropLoc(PlaceItemsPlayerPlaceLocation prop){
		props[PlaceItemsUtils.getPropInexFromBlockFace("UP")] = prop;
	}
	
	@Override
	public String toString(){
		
		StringBuilder propsString = new StringBuilder();
		
		for(int i = 0; i < props.length; i++) {
			if(props[i] == null) {
				propsString.append("null,null,null,null,");
			}else {
				propsString.append(props[i].getPlacer() + "," + props[i].getLocation().getX() + "," + props[i].getLocation().getY() + "," + props[i].getLocation().getZ() + ",");
			}
		}
		
		propsString.deleteCharAt(propsString.length() - 1);
		return propsString.toString(); 
			   
	}
	
	public static Entry<PlaceItemsPhysicalLocation, AdvancedPlaceItemsLinkedLocation> fromStringOld(String data) throws PlaceItemsInvalidLinkedLocationException{
		
		PlaceItemsPhysicalLocation keyLoad;
		AdvancedPlaceItemsLinkedLocation load = new AdvancedPlaceItemsLinkedLocation();
		
		String[] fields = data.split(",");
		if(fields.length == 9){
			try{
				if (Bukkit.getWorld(fields[1]) != null && Bukkit.getWorld(fields[5]) != null) {
					keyLoad = new PlaceItemsPhysicalLocation(Bukkit.getWorld(fields[1]).getName(), Double.parseDouble(fields[2]), Double.parseDouble(fields[3]), Double.parseDouble(fields[4]));
					load.addProp(UUID.fromString(fields[0]), new Location(Bukkit.getWorld(fields[5]), Double.parseDouble(fields[6]), Double.parseDouble(fields[7]), Double.parseDouble(fields[8])), "UP");
					return new AbstractMap.SimpleEntry<PlaceItemsPhysicalLocation, AdvancedPlaceItemsLinkedLocation>(keyLoad, load);
				}
				
			}catch(Exception e){
				throw new PlaceItemsInvalidLinkedLocationException("Well that String data wasn't correct...");
			}
		}
		return null;
	}
	
	public static Entry<PlaceItemsPhysicalLocation, AdvancedPlaceItemsLinkedLocation> fromString(String keyData, String valueData) throws PlaceItemsInvalidLinkedLocationException{
		
		PlaceItemsPhysicalLocation keyLoad;
		AdvancedPlaceItemsLinkedLocation load = new AdvancedPlaceItemsLinkedLocation();
		
		String[] keyFields = keyData.split(",");
		String[] valueFields = valueData.split(",");
		
		if(keyFields.length + valueFields.length == 28){
			try{
				if (Bukkit.getWorld(keyFields[0]) != null) {
					keyLoad = new PlaceItemsPhysicalLocation(Bukkit.getWorld(keyFields[0]).getName(), Double.parseDouble(keyFields[1]), Double.parseDouble(keyFields[2]), Double.parseDouble(keyFields[3]));
					
					if(!valueFields[0].equals("null")) {
						load.addProp(UUID.fromString(valueFields[0]), new Location(Bukkit.getWorld(keyFields[0]), Double.parseDouble(valueFields[1]), Double.parseDouble(valueFields[2]), Double.parseDouble(valueFields[3])), "UP");
					}
					if(!valueFields[4].equals("null")) {
						load.addProp(UUID.fromString(valueFields[4]), new Location(Bukkit.getWorld(keyFields[0]), Double.parseDouble(valueFields[5]), Double.parseDouble(valueFields[6]), Double.parseDouble(valueFields[7])), "DOWN");
					}
					if(!valueFields[8].equals("null")) {
						load.addProp(UUID.fromString(valueFields[8]), new Location(Bukkit.getWorld(keyFields[0]), Double.parseDouble(valueFields[9]), Double.parseDouble(valueFields[10]), Double.parseDouble(valueFields[11])), "NORTH");
					}
					
					if(!valueFields[12].equals("null")) {
						load.addProp(UUID.fromString(valueFields[12]), new Location(Bukkit.getWorld(keyFields[0]), Double.parseDouble(valueFields[13]), Double.parseDouble(valueFields[14]), Double.parseDouble(valueFields[15])), "SOUTH");
					}
					
					if(!valueFields[16].equals("null")) {
						load.addProp(UUID.fromString(valueFields[16]), new Location(Bukkit.getWorld(keyFields[0]), Double.parseDouble(valueFields[17]), Double.parseDouble(valueFields[18]), Double.parseDouble(valueFields[19])), "WEST");
					}
					
					if(!valueFields[20].equals("null")) {
						load.addProp(UUID.fromString(valueFields[20]), new Location(Bukkit.getWorld(keyFields[0]), Double.parseDouble(valueFields[21]), Double.parseDouble(valueFields[22]), Double.parseDouble(valueFields[23])), "EAST");
					}
					
					
					return new AbstractMap.SimpleEntry<PlaceItemsPhysicalLocation, AdvancedPlaceItemsLinkedLocation>(keyLoad, load);
				}
				
			}catch(Exception e){
				throw new PlaceItemsInvalidLinkedLocationException("Well that String data wasn't correct...");
			}
		}
		return null;
	}
	
}
