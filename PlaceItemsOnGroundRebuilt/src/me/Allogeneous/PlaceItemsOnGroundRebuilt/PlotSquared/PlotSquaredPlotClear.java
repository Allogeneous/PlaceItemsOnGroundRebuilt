package me.Allogeneous.PlaceItemsOnGroundRebuilt.PlotSquared;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.intellectualsites.plotsquared.plot.object.Plot;

import me.Allogeneous.PlaceItemsOnGroundRebuilt.Files.AdvancedPlaceItemsLinkedLocation;
import me.Allogeneous.PlaceItemsOnGroundRebuilt.Files.PlaceItemsManager;
import me.Allogeneous.PlaceItemsOnGroundRebuilt.Files.PlaceItemsPlayerPlaceLocation;

public class PlotSquaredPlotClear extends BukkitRunnable{
	
	String world;
	Plot plot;
	PlaceItemsManager manager;
	
	public PlotSquaredPlotClear(String world, Plot plot, PlaceItemsManager manager) {
		this.world = world;
		this.plot = plot;
		this.manager = manager;
	}
	
	@Override
	public void run() {
		
		int minX = 0;
		int maxX = 0;
		
		int minY = 0;
		int maxY = 256;
		
		int minZ = 0;
		int maxZ = 0;
		
		boolean first = true;
		
		for(com.github.intellectualsites.plotsquared.plot.object.Location location : plot.getAllCorners()) {
			if(first) {
				first = false;
				minX = location.getX();
				maxX = location.getX();
				
				minZ = location.getZ();
				maxZ = location.getZ();
			}else {
				if(location.getX() < minX) {
					minX = location.getX();
				}
				if(location.getX() > maxX) {
					maxX = location.getX();
				}
				if(location.getZ() < minZ) {
					minZ = location.getZ();
				}
				if(location.getZ() > maxZ) {
					maxZ = location.getZ();
				}
			}

		}
		
		for(int x = minX-1; x <= maxX; x++) {
			for(int y = minY; y <= maxY; y++) {
				for(int z = minZ-1; z <= maxZ; z++) {
					if(plot.getArea().contains(x, z)){
						Location physical = new Location(Bukkit.getWorld(world), x, y, z);
						if(manager.containsPhysical(physical)){
							AdvancedPlaceItemsLinkedLocation apill = manager.getFromPhysical(physical);
							for(PlaceItemsPlayerPlaceLocation pippl : apill.getProps()) {
								if(pippl != null) {
									manager.setPlacements(pippl.getPlacer(), manager.getPlacements(pippl.getPlacer()) - 1);
								}
							}
							manager.removePhysical(physical);
						}	
					}
				}
			}
		}
		
	}
	
	

}
