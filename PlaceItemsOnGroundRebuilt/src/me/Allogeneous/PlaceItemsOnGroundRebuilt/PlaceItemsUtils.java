package me.Allogeneous.PlaceItemsOnGroundRebuilt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.util.EulerAngle;

import me.Allogeneous.PlaceItemsOnGroundRebuilt.Files.AdvancedPlaceItemsLinkedLocation;
import me.Allogeneous.PlaceItemsOnGroundRebuilt.Files.PlaceItemsManager;
import me.Allogeneous.PlaceItemsOnGroundRebuilt.Files.PlaceItemsPlayerPlaceLocation;

public class PlaceItemsUtils {
	
	public static boolean isBlacklisted(Material type, String blockFace){
		if(PlaceItemsConfig.getBlackListedItemsAll().contains(type.toString())) {
			return true;
		}
		switch(blockFace) {
		case "UP":
			if(PlaceItemsConfig.getBlackListedItemsTop().contains(type.toString())) {
				return true;
			}
			break;
		case "DOWN":
			if(PlaceItemsConfig.getBlackListedItemsBottom().contains(type.toString())) {
				return true;
			}
			break;
		case "NORTH":
		case "SOUTH":
		case "WEST":
		case "EAST":
			if(PlaceItemsConfig.getBlackListedItemsSides().contains(type.toString())) {
				return true;
			}
			break;
		default:
			return false;
		}
		return false;
	}
	
	public static boolean isBlackListedPlaceItem(Material type, String blockFace) {
		if(PlaceItemsConfig.getBlackListedPlaceItemsAll().contains(type.toString())) {
			return true;
		}
		switch(blockFace) {
		case "UP":
			if(PlaceItemsConfig.getBlackListedPlaceItemsTop().contains(type.toString())) {
				return true;
			}
			break;
		case "DOWN":
			if(PlaceItemsConfig.getBlackListedPlaceItemsBottom().contains(type.toString())) {
				return true;
			}
			break;
		case "NORTH":
		case "SOUTH":
		case "WEST":
		case "EAST":
			if(PlaceItemsConfig.getBlackListedPlaceItemsSides().contains(type.toString())) {
				return true;
			}
			break;
		default:
			return false;
		}
		return false;
	}
	
	public static boolean isItemLikeBlock(Material type){
		return PlaceItemsConfig.getItemLikeBlocks().contains(type.toString());
	}
	
	public static boolean isBlockLikeItem(Material type){
		return PlaceItemsConfig.getBlockLikeItems().contains(type.toString());
	}
	
	public static boolean isPlaceIn(Material type){
		return PlaceItemsConfig.getPlaceIn().contains(type.toString());
	}
	
	public static boolean isSlab(Material type){
		return PlaceItemsConfig.getSlabs().contains(type.toString());
	}
	
	public static boolean isStairs(Material type){
		return PlaceItemsConfig.getStairs().contains(type.toString());
	}
	
	public static boolean isLegacyDoubleSlab(Material type) {
		return PlaceItemsConfig.getLegacyDoubleSlabs().contains(type.toString());
	}

	public static EulerAngle calcBlockArmorStandHeadPos(Location location){
        return new EulerAngle(0, Math.toRadians(location.getYaw()) + Math.PI, 0);
	}
	
	public static EulerAngle calcItemArmorStandHeadPos(Location location){
		return new EulerAngle(Math.PI / 2, Math.toRadians(location.getYaw()), 0);
	}
	
	public static int getPropInexFromBlockFace(String blockFace) {
		switch(blockFace) {
			case "UP":
				return 0;
			case "DOWN":
				return 1;
			case "NORTH":
				return 2;
			case "SOUTH":
				return 3;
			case "WEST":
				return 4;
			case "EAST":
				return 5;
			default:
				return 0;
		}
	}
	
	public static BlockFace getCardinalDirection(Location location) {
        double rotation = (location.getYaw() - 180) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
         if (0 <= rotation && rotation < 22.5) {
            return BlockFace.NORTH;
        } else if (22.5 <= rotation && rotation < 67.5) {
            return BlockFace.NORTH_EAST;
        } else if (67.5 <= rotation && rotation < 112.5) {
            return BlockFace.EAST;
        } else if (112.5 <= rotation && rotation < 157.5) {
            return BlockFace.SOUTH_EAST;
        } else if (157.5 <= rotation && rotation < 202.5) {
            return BlockFace.SOUTH;
        } else if (202.5 <= rotation && rotation < 247.5) {
            return BlockFace.SOUTH_WEST;
        } else if (247.5 <= rotation && rotation < 292.5) {
            return BlockFace.WEST;
        } else if (292.5 <= rotation && rotation < 337.5) {
            return BlockFace.NORTH_WEST;
        } else if (337.5 <= rotation && rotation < 360.0) {
            return BlockFace.NORTH;
        } else {
            return null;
        }
    }
	
	
	
	public static int removeInRadiusAroundPlayer(Location location, PlaceItemsManager manager, int radius){
		
		int count = 0;
		Collection<Entity> entities = location.getWorld().getNearbyEntities(location, radius, radius, radius);
		
		for(Entity e : entities){
			if(e instanceof ArmorStand){
				if(manager.containsProp(getPotentialPhysicalLocations(e.getLocation()), e.getLocation())){
					PlaceItemsPlayerPlaceLocation pippl = manager.getPlayerPlaceFromProp(getPotentialPhysicalLocations(e.getLocation()), e.getLocation());
					manager.setPlacements(pippl.getPlacer(), manager.getPlacements(pippl.getPlacer()) - 1);
					manager.removeProp(getPotentialPhysicalLocations(e.getLocation()), e.getLocation());
					count++;
					e.remove();
				}
			}
		}
		return count;
	}
	
	public static int[] purgeInRadiusAroundPlayer(Location location, PlaceItemsManager manager, int radius){
		
		int[] removes = new int[]{0, 0};
		
		Collection<Entity> entities = location.getWorld().getNearbyEntities(location, radius, radius, radius);
		
		
		ArrayList<Block> blocks = getBlocks(location, radius);
		
		for(Block b : blocks) {
			if(manager.containsPhysical(b.getLocation())){
				AdvancedPlaceItemsLinkedLocation data = manager.getFromPhysical(b.getLocation());
				for(PlaceItemsPlayerPlaceLocation pippl : data.getProps()) {
					if(pippl == null) {
						continue;
					}
					manager.setPlacements(pippl.getPlacer(), manager.getPlacements(pippl.getPlacer()) - 1);
					manager.removePhysical(b.getLocation());
				}
				removes[0]++;
			}
		}
		
		for(Entity e : entities){
			if(e instanceof ArmorStand){
				e.remove();
				removes[1]++;
			}
		}
		return removes;
	}
	
	public static boolean placedItemsAreInRadius(Location location, PlaceItemsManager manager, int radius){
		
		Collection<Entity> entities = location.getWorld().getNearbyEntities(location, radius, radius, radius);
		
		for(Entity e : entities){
			if(e instanceof ArmorStand){
				if(manager.containsProp(getPotentialPhysicalLocations(e.getLocation()), e.getLocation())){
					return true;
				}
			}
		}
		return false;
	}
	
	public static Location[] getPotentialPhysicalLocations(Location l) {
		ArrayList<Block> blocks = getBlocks(l, 2);
		Location[] locations = new Location[blocks.size()];
		
		for(int i = 0; i < blocks.size(); i++) {
			locations[i] = blocks.get(i).getLocation();
		}
		
		return locations;
	}
	
	public static ArrayList<Block> getBlocks(Location location, int radius){
	     ArrayList<Block> blocks = new ArrayList<Block>();
	     for(double x = location.getX() - radius; x <= location.getX() + radius; x++){
	    	 for(double y = location.getY() - radius; y <= location.getY() + radius; y++){
	    		 for(double z = location.getZ() - radius; z <= location.getZ() + radius; z++){
	    			 Location loc = new Location(location.getWorld(), x, y, z);
	    			 blocks.add(loc.getBlock());
	    		 }
	    	 }
	     }
	     return blocks;
	}
	
	public static boolean makeFolder(File file){
		if(!file.exists() && (!file.mkdir())){
	    	return false;
	    }
		return true;
	}
	
	public static boolean createFile(File file){
		try{
			if(!file.exists()){
				file.createNewFile();
				return true;
		     }
			return true;
		}catch (Exception e){
		      e.printStackTrace();
		      return false;
		}
	}
	
	public static void saveFile(Object object, File file){
	    try{
	    	if(!file.exists()){
	    		file.createNewFile();
	    	}
	    	ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
	    	oos.writeObject(object);
	    	oos.flush();
	    	oos.close();
	    }catch (Exception e){
	      e.printStackTrace();
	    }
	 }
	 
	  public static Object loadFile(File file){
		  try{
			  ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			  Object object = ois.readObject();
			  ois.close();
			  return object;
		  }catch (Exception e) {}
	    return null;
	  }
}


