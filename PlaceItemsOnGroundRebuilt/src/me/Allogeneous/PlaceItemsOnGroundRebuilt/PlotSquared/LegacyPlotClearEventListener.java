package me.Allogeneous.PlaceItemsOnGroundRebuilt.PlotSquared;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.plotsquared.bukkit.events.PlotClearEvent;
import com.plotsquared.bukkit.events.PlotDeleteEvent;

import me.Allogeneous.PlaceItemsOnGroundRebuilt.PlaceItemsMain;
import me.Allogeneous.PlaceItemsOnGroundRebuilt.Files.PlaceItemsManager;

public class LegacyPlotClearEventListener implements Listener{
	
	private PlaceItemsMain plugin;
	private PlaceItemsManager manager;
	
	public LegacyPlotClearEventListener(PlaceItemsMain plugin, PlaceItemsManager manager) {
		this.plugin = plugin;
		this.manager = manager;
	}

	@EventHandler
	public void onPlotClear(PlotClearEvent e) {
		LegacyPlotSquaredPlotClear lpspc = new LegacyPlotSquaredPlotClear(e.getWorld(), e.getPlot(), manager);
		lpspc.runTaskAsynchronously(plugin);
	}
	
	@EventHandler
	public void onPlotDelete(PlotDeleteEvent e) {
		LegacyPlotSquaredPlotClear lpspc = new LegacyPlotSquaredPlotClear(e.getWorld(), e.getPlot(), manager);
		lpspc.runTaskAsynchronously(plugin);
	}
}
