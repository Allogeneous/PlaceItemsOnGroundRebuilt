package me.Allogeneous.PlaceItemsOnGroundRebuilt;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class PlaceItemsLinkedLocation{

	private UUID placer;
	private Location physical, prop;
	
	public PlaceItemsLinkedLocation(){}
	
	public PlaceItemsLinkedLocation(UUID placer, Location physical, Location prop){
		this.placer = placer;
		this.physical = physical;
		this.prop = prop;
	}
	
	public UUID getPlacer(){
		return placer;
	}
	
	public void setPlacer(UUID placer){
		this.placer = placer;
	}
	
	public Location getPhysicalLoc(){
		return physical;
	}
	
	public void setPhysicalLoc(Location physical){
		this.physical = physical;
	}
	
	public Location getPropLoc(){
		return prop;
	}
	
	public void setPropLoc(Location prop){
		this.prop = prop;
	}
	
	@Override
	public String toString(){
		return placer.toString() + "," + physical.getWorld().getName() + "," + physical.getX() + "," + physical.getY() + "," + physical.getZ()  + "," + prop.getWorld().getName() + "," +prop.getX() + "," + prop.getY() + "," + prop.getZ();
	}
	
	public static PlaceItemsLinkedLocation fromString(String data) throws PlaceItemsInvalidLinkedLocationException{
		
		PlaceItemsLinkedLocation load = new PlaceItemsLinkedLocation();
		
		String[] fields = data.split(",");
		if(fields.length != 9){
			throw new PlaceItemsInvalidLinkedLocationException("9 fields were not found when attempting to parse this location!");
		}
		
		try{
			if (Bukkit.getWorld(fields[1]) != null && Bukkit.getWorld(fields[5]) != null) {
				load.setPlacer(UUID.fromString(fields[0]));
				load.setPhysicalLoc(new Location(Bukkit.getWorld(fields[1]), Double.parseDouble(fields[2]), Double.parseDouble(fields[3]), Double.parseDouble(fields[4])));
				load.setPropLoc(new Location(Bukkit.getWorld(fields[5]), Double.parseDouble(fields[6]), Double.parseDouble(fields[7]), Double.parseDouble(fields[8])));
				return load;
			}
			
		}catch(Exception e){
			throw new PlaceItemsInvalidLinkedLocationException("Well that String data wasn't correct...");
		}
		return null;
	}
}
