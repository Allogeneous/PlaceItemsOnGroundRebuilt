package me.Allogeneous.PlaceItemsOnGroundRebuilt;

import org.bukkit.scheduler.BukkitRunnable;

public class PlaceItemsLocationAutoSaver extends BukkitRunnable{
	
	private PlaceItemsManager manager;
	
	public PlaceItemsLocationAutoSaver(PlaceItemsManager manager){
		this.manager = manager;
	}

	@Override
	public void run(){
		manager.saveLocationData();
	}
	
}
