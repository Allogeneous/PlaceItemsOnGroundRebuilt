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

public class PlaceItemsUtils {
	
	public static boolean isBlacklisted(Material type){
		return PlaceItemsConfig.getBlackListedItems().contains(type.toString());
	}
	
	public static boolean isBlackListedPlaceItem(Material type) {
		return PlaceItemsConfig.getBlackListedPlaceItems().contains(type.toString());
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
	
	public static Location getBestArmorStandItemRelitiveToLocation(BlockFace blockFace, Location location){
		switch(blockFace){
			case NORTH:
				return location.add(0.5, -0.65, 1.25);
			case NORTH_EAST:
				return location.add(0, -0.65, 1);
			case EAST:
				return location.add(-0.25, -0.65, 0.5);
			case SOUTH_EAST:
				return location.add(0, -0.65, 0);
			case SOUTH:
				return location.add(0.5, -0.65, -0.25);
			case SOUTH_WEST:
				return location.add(1, -0.65, 0);
			case WEST:
				return location.add(1.25, -0.65, 0.5);
			case NORTH_WEST:
				return location.add(1.0, -0.65, 1.0);
			default:
				return location;
		
		}
	}
	
	public static int removeInRadiusAroundPlayer(Location location, PlaceItemsManager manager, int radius){
		
		int count = 0;
		Collection<Entity> entities = location.getWorld().getNearbyEntities(location, radius, radius, radius);
		
		for(Entity e : entities){
			if(e instanceof ArmorStand){
				if(manager.containsProp(e.getLocation())){
					manager.setPlacements(manager.getFromProp(e.getLocation()).getPlacer(), manager.getPlacements(manager.getFromProp(e.getLocation()).getPlacer()) - 1);
					manager.removeProp(e.getLocation());
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
				manager.setPlacements(manager.getFromPhysical(b.getLocation()).getPlacer(), manager.getPlacements(manager.getFromPhysical(b.getLocation()).getPlacer()) - 1);
				manager.removePhysical(b.getLocation());
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
				if(manager.containsProp(e.getLocation())){
					return true;
				}
			}
		}
		return false;
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
	    }
	    catch (Exception e){
	      e.printStackTrace();
	    }
	  }
	 
	  public static Object loadFile(File file){
	    try{
	      ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
	      Object object = ois.readObject();
	      ois.close();
	      return object;
	    }
	    catch (Exception e) {}
	    return null;
	  }
	  
	 
		
		
		

}


