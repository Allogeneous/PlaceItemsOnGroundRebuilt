package me.Allogeneous.PlaceItemsOnGroundRebuilt.PlotSquared;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.github.intellectualsites.plotsquared.bukkit.events.PlotClearEvent;
import com.github.intellectualsites.plotsquared.bukkit.events.PlotDeleteEvent;

import me.Allogeneous.PlaceItemsOnGroundRebuilt.PlaceItemsMain;
import me.Allogeneous.PlaceItemsOnGroundRebuilt.Files.PlaceItemsManager;

public class PlotClearEventListener implements Listener{
	
	private PlaceItemsMain plugin;
	private PlaceItemsManager manager;
	
	public PlotClearEventListener(PlaceItemsMain plugin, PlaceItemsManager manager) {
		this.plugin = plugin;
		this.manager = manager;
	}

	@EventHandler
	public void onPlotClear(PlotClearEvent e) {
		PlotSquaredPlotClear pspc = new PlotSquaredPlotClear(e.getWorld(), e.getPlot(), manager);
		pspc.runTaskAsynchronously(plugin);
	}
	
	@EventHandler
	public void onPlotDelete(PlotDeleteEvent e) {
		PlotSquaredPlotClear pspc = new PlotSquaredPlotClear(e.getWorld(), e.getPlot(), manager);
		pspc.runTaskAsynchronously(plugin);
	}
	
}
