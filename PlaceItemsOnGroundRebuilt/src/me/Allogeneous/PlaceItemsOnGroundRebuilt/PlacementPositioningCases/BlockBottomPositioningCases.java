package me.Allogeneous.PlaceItemsOnGroundRebuilt.PlacementPositioningCases;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public class BlockBottomPositioningCases {
	
	public static Location getBestArmorStandItemRelitiveToLocation(BlockFace blockFace, Location location){
		switch(blockFace){
			case NORTH:
				return location.add(0.5, -1.75, 1.25);
			case NORTH_EAST:
				return location.add(0, -1.75, 1);
			case EAST:
				return location.add(-0.25, -1.75, 0.5);
			case SOUTH_EAST:
				return location.add(0, -1.75, 0);
			case SOUTH:
				return location.add(0.5, -1.75, -0.25);
			case SOUTH_WEST:
				return location.add(1, -1.75, 0);
			case WEST:
				return location.add(1.25, -1.75, 0.5);
			case NORTH_WEST:
				return location.add(1.0, -1.75, 1.0);
			default:
				return location;
		}
	}
	
	public static Location getBestArmorStandItemRelitiveToLocationSpecialCases1(BlockFace blockFace, Location location){
		switch(blockFace){
			case NORTH:
				return location.add(0.5, -1.75, 0.6);
			case NORTH_EAST:
				return location.add(0.5, -1.75, 0.6);
			case EAST:
				return location.add(0.4, -1.75, 0.5);
			case SOUTH_EAST:
				return location.add(0.4, -1.75, 0.5);
			case SOUTH:
				return location.add(0.5, -1.75, 0.4);
			case SOUTH_WEST:
				return location.add(0.5, -1.75, 0.4);
			case WEST:
				return location.add(0.6, -1.75, 0.5);
			case NORTH_WEST:
				return location.add(0.6, -1.75, 0.5);
			default:
				return location;
		}
	}
}
