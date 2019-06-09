package me.Allogeneous.PlaceItemsOnGroundRebuilt.Files;

import java.util.UUID;

import org.bukkit.Location;

public class PlaceItemsPlayerPlaceLocation {
	
	private UUID placer;
	private Location location;
	
	public PlaceItemsPlayerPlaceLocation(UUID placer, Location location) {
		this.setPlacer(placer);
		this.setLocation(location);
	}

	public UUID getPlacer() {
		return placer;
	}

	public void setPlacer(UUID placer) {
		this.placer = placer;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	
	@Override
	public String toString() {
		return placer.toString() + "," + location.getX() + "," + location.getY() + "," + location.getZ();
	}
}
