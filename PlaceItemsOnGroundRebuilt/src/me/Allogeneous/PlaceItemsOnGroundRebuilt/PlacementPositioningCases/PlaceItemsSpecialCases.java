package me.Allogeneous.PlaceItemsOnGroundRebuilt.PlacementPositioningCases;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.EulerAngle;

public class PlaceItemsSpecialCases {
	public static final String[] SPECIAL_CASES_1 = {"LEAD", "LEASH", "BONE"};
	public static final String[] SPECIAL_CASES_2 = {"END_ROD"};
	
	public static final boolean isSpecialCases1(Material type) {
		for(String typeS1 : SPECIAL_CASES_1) {
			if(typeS1.equals(type.toString())) {
				return true;
			}
		}
		return false;
	}
	
	public static final boolean isSpecialCases2(Material type) {
		for(String typeS2 : SPECIAL_CASES_2) {
			if(typeS2.equals(type.toString())) {
				return true;
			}
		}
		return false;
	}
	
	public static EulerAngle calcItemArmorStandHeadPosSpecialCases1(Location location) {
		return new EulerAngle((3*Math.PI / 2), Math.toRadians(location.getYaw()) - Math.PI, 0);
	}
}
