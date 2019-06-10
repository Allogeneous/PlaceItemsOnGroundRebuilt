package me.Allogeneous.PlaceItemsOnGroundRebuilt.PlacementPositioningCases;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.EulerAngle;

public class BlockSidePositioningCases {
	
	public static Location getBestArmorStandRelitiveToRotationNumberItem(BlockFace blockFace, int rotation, Location location){
		if(blockFace == BlockFace.NORTH) {
			switch(rotation){
				case 0:
					return location.add(0.5, -1.65, -0.325);
				case 1:
					return location.add(0.0, -1.4, -0.325);
				case 2:
					return location.add(-0.25, -0.9, -0.325);
				case 3:
					return location.add(0.0, -0.4, -0.325);
				case 4:
					return location.add(0.5, -0.2, -0.325);
				case 5:
					return location.add(1.0, -0.4, -0.325);
				case 6:
					return location.add(1.25, -0.9, -0.325);
				case 7:
					return location.add(1.0, -1.4, -0.325);
				default:
					return location.add(0.5, -1.65, -0.325);
			}
		}else if(blockFace == BlockFace.SOUTH) {
			switch(rotation){
				case 0:
					return location.add(0.5, -1.65, 1.325);
				case 1:
					return location.add(1.0, -1.4, 1.325);
				case 2:
					return location.add(1.25, -0.9, 1.325);
				case 3:
					return location.add(1.0, -0.4, 1.325);
				case 4:
					return location.add(0.5, -0.2, 1.325);
				case 5:
					return location.add(0.0, -0.4, 1.325);
				case 6:
					return location.add(-0.25, -0.9, 1.325);
				case 7:
					return location.add(0.0, -1.4, 1.325);
				default:
					return location.add(0.5, -1.65, 1.325);
			}
		}else if(blockFace == BlockFace.WEST) {
			switch(rotation){
				case 0:
					return location.add(-0.325, -1.65, 0.5);
				case 1:
					return location.add(-0.325, -1.4, 1.0);
				case 2:
					return location.add(-0.325, -0.9, 1.25);
				case 3:
					return location.add(-0.325, -0.4, 1.0);
				case 4:
					return location.add(-0.325, -0.2, 0.5);
				case 5:
					return location.add(-0.325, -0.4, 0.0);
				case 6:
					return location.add(-0.325, -0.9, -0.25);
				case 7:
					return location.add(-0.325, -1.4, 0.0);
				default:
					return location.add(-0.325, -1.65, 0.5);
			}
		}else if(blockFace == BlockFace.EAST) {
			switch(rotation){
				case 0:
					return location.add(1.325, -1.65, 0.5);
				case 1:
					return location.add(1.325, -1.4, 0.0);
				case 2:
					return location.add(1.325, -0.9, -0.25);
				case 3:
					return location.add(1.325, -0.4, 0.0);
				case 4:
					return location.add(1.325, -0.2, 0.5);
				case 5:
					return location.add(1.325, -0.4, 1.0);
				case 6:
					return location.add(1.325, -0.9, 1.25);
				case 7:
					return location.add(1.325, -1.4, 1.0);
				default:
					return location.add(1.325, -1.65, 0.5);
			}
		}
		return location;
	}
	
	public static Location getBestArmorStandRelitiveToRotationNumberBlock(BlockFace blockFace, int rotation, Location location){
		if(blockFace == BlockFace.NORTH) {
			switch(rotation){
				case 0:
					return location.add(0.5, -1.15, -0.325);
				case 1:
					return location.add(0.345, -1.15, -0.325);
				case 2:
					return location.add(0.255, -0.95, -0.325);
				case 3:
					return location.add(0.345, -0.75, -0.325);
				case 4:
					return location.add(0.5, -0.7, -0.325);
				case 5:
					return location.add(0.65, -0.75, -0.325);
				case 6:
					return location.add(0.755, -0.95, -0.325);
				case 7:
					return location.add(0.65, -1.15, -0.325);
				default:
					return location.add(1.345, -1.15, -0.325);
			}
		}else if(blockFace == BlockFace.SOUTH) {
			switch(rotation){
				case 0:
					return location.add(0.5, -1.15, 1.325);
				case 1:
					return location.add(0.655, -1.15, 1.325);
				case 2:
					return location.add(0.745, -0.95, 1.325);
				case 3:
					return location.add(0.655, -0.75, 1.325);
				case 4:
					return location.add(0.5, -0.7, 1.325);
				case 5:
					return location.add(0.35, -0.75, 1.325);
				case 6:
					return location.add(0.245, -0.95, 1.325);
				case 7:
					return location.add(0.35, -1.15, 1.325);
				default:
					return location.add(0.5, -1.15, 1.325);
			}
		}else if(blockFace == BlockFace.WEST) {
			switch(rotation){
				case 0:
					return location.add(-0.325, -1.15, 0.5);
				case 1:
					return location.add(-0.325, -1.15, 0.655);
				case 2:
					return location.add(-0.325, -0.95, 0.745);
				case 3:
					return location.add(-0.325, -0.75, 0.655);
				case 4:
					return location.add(-0.325, -0.7, 0.5);
				case 5:
					return location.add(-0.325, -0.75, 0.35);
				case 6:
					return location.add(-0.325, -0.95, 0.245);
				case 7:
					return location.add(-0.325, -1.15, 0.35);
				default:
					return location.add(-0.325, -1.15, 0.5);
			}
		}else if(blockFace == BlockFace.EAST) {
			switch(rotation){
				case 0:
					return location.add(1.325, -1.15, 0.5);
				case 1:
					return location.add(1.325, -1.15, 0.345);
				case 2:
					return location.add(1.325, -1.15, 0.255);
				case 3:
					return location.add(1.325, -1.15, 0.345);
				case 4:
					return location.add(1.325, -1.15, 0.5);
				case 5:
					return location.add(1.325, -1.15, 0.65);
				case 6:
					return location.add(1.325, -1.15, 0.755);
				case 7:
					return location.add(1.325, -1.15, 0.65);
				default:
					return location.add(1.325, -1.15, 1.345);
			}
		}
		return location;
	}
	
	public static EulerAngle getBestArmorHeadPosRelitiveToRotationNumber(BlockFace blockFace, int rotation, Location location){
		if(blockFace == BlockFace.NORTH || blockFace == BlockFace.EAST) {
			switch(rotation){
				case 0:
					return new EulerAngle(0, Math.PI, 0);
				case 1:
					return new EulerAngle(0, Math.PI, Math.PI / 4);
				case 2:
					return new EulerAngle(0, Math.PI, Math.PI / 2);
				case 3:
					return new EulerAngle(0, Math.PI, (3*Math.PI) / 4);
				case 4:
					return new EulerAngle(0, Math.PI, Math.PI);
				case 5:
					return new EulerAngle(0, Math.PI, (5*Math.PI) / 4);
				case 6:
					return new EulerAngle(0, Math.PI, (3*Math.PI) / 2);
				case 7:
					return new EulerAngle(0, Math.PI, (7*Math.PI) / 4);
				default:
					return new EulerAngle(0, Math.PI, 0);
			}
		}else if(blockFace == BlockFace.SOUTH || blockFace == BlockFace.WEST) {
			switch(rotation){
				case 0:
					return new EulerAngle(0, 0, 0);
				case 1:
					return new EulerAngle(0, 0, (7*Math.PI) / 4);
				case 2:
					return new EulerAngle(0, 0, (3*Math.PI) / 2);
				case 3:
					return new EulerAngle(0, 0, (5*Math.PI) / 4);
				case 4:
					return new EulerAngle(0, 0, Math.PI);
				case 5:
					return new EulerAngle(0, 0, (3*Math.PI) / 4);
				case 6:
					return new EulerAngle(0, 0, Math.PI / 2);
				case 7:
					return new EulerAngle(0, 0, Math.PI / 4);
				default:
					return new EulerAngle(0, 0, 0);
			}
		}
		return new EulerAngle(0, 0, 0);
	}

}
