package me.Allogeneous.PlaceItemsOnGroundRebuilt.Threads;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Allogeneous.PlaceItemsOnGroundRebuilt.Files.PlaceItemsManager;

public class PlaceItemsDataSyncer extends BukkitRunnable{

	private CommandSender sender;
	private UUID target;
	private PlaceItemsManager manager;
	
	public PlaceItemsDataSyncer(CommandSender sender, Player target, PlaceItemsManager manager) {
		this.sender = sender;
		this.target = target.getUniqueId();
		this.manager = manager;
	}
	
	@Override
	public void run() {
		sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.AQUA + "Syncing Locations.dat placement data with player file placement data... this may take some time.");
		if(!manager.confirmFileExistance(target)) {
			sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "That player does not have a file!");
			return;
		}else {
			int count = manager.getPlacementCountFromLocationData(target);	
			if(count != manager.getPlacements(target)) {
				int oldCount = manager.getPlacements(target);
				manager.setPlacements(target, count);
				sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.GREEN + "Data is synced! Player placements went from " + ChatColor.RED + oldCount + ChatColor.GREEN + " to " + ChatColor.YELLOW + count + ChatColor.GREEN + ".");
			}else {
				sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.AQUA + "No changes were made!");
			}
			
		}
	}

}
