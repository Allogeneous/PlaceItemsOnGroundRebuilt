package me.Allogeneous.PlaceItemsOnGroundRebuilt.Files;

import java.math.BigInteger;
import java.util.UUID;

import org.bukkit.Location;

public final class PlaceItemsPhysicalLocation implements Comparable<PlaceItemsPhysicalLocation>{
	
	private final BigInteger treeID;
	
	private final String world;
	private final double x, y, z;
	
	
	public PlaceItemsPhysicalLocation(Location location) {
		this.world = location.getWorld().getName();
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		
		this.treeID = treeID(this.world, this.x, this.y, this.z);
	}
	
	public PlaceItemsPhysicalLocation(String world, double x, double y, double z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.treeID = treeID(this.world, this.x, this.y, this.z);
	}
	
	private final BigInteger treeID(String world, double x, double y, double z) {
		return new BigInteger(UUID.nameUUIDFromBytes((world + "" + x + "" + y + "" + z).getBytes()).toString().replaceAll("-", ""), 16);
	}
	
	
	public final BigInteger getTreeID() {
		return this.treeID;
	}

	public String getWorld() {
		return world;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}


	@Override
	public String toString() {
		return this.getWorld() + "," + this.getX() + "," + this.getY() + "," + this.getZ();
	}
	
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof PlaceItemsPhysicalLocation) {
			PlaceItemsPhysicalLocation second = (PlaceItemsPhysicalLocation) o;
			return this.getTreeID().compareTo(second.getTreeID()) == 0;
		}
		return false;
		
	}

	@Override
	public int compareTo(PlaceItemsPhysicalLocation second) {
		return this.getTreeID().compareTo(second.getTreeID());
	}
}
