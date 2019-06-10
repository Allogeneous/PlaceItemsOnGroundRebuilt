package me.Allogeneous.PlaceItemsOnGroundRebuilt.PlacementPositioningCases;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.EulerAngle;

public class BlockTopPositioningCases {
	
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
	
	public static Location getBestArmorStandItemRelitiveToLocationSpecialCases1(BlockFace blockFace, Location location){
		switch(blockFace){
			case NORTH:
				return location.add(0.5, -0.65, 0.6);
			case NORTH_EAST:
				return location.add(0.5, -0.65, 0.6);
			case EAST:
				return location.add(0.4, -0.65, 0.5);
			case SOUTH_EAST:
				return location.add(0.4, -0.65, 0.5);
			case SOUTH:
				return location.add(0.5, -0.65, 0.4);
			case SOUTH_WEST:
				return location.add(0.5, -0.65, 0.4);
			case WEST:
				return location.add(0.6, -0.65, 0.5);
			case NORTH_WEST:
				return location.add(0.6, -0.65, 0.5);
			default:
				return location;
		}
	}
	
	public static EulerAngle calcBlockArmorStandHeadPosSpecialCases2(Location location){
       return new EulerAngle((5*Math.PI/3) , Math.toRadians(location.getYaw()) + Math.PI, 0);
	}
	
}
