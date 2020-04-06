package me.Allogeneous.PlaceItemsOnGroundRebuilt.Threads;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Allogeneous.PlaceItemsOnGroundRebuilt.PlaceItemsMain;
import me.Allogeneous.PlaceItemsOnGroundRebuilt.Files.PlaceItemsManager;

public class PlaceItemsDataSyncer extends BukkitRunnable{

	private PlaceItemsMain plugin;
	private CommandSender sender;
	private UUID target;
	private PlaceItemsManager manager;
	
	public PlaceItemsDataSyncer(PlaceItemsMain plugin, CommandSender sender, Player target, PlaceItemsManager manager) {
		this.plugin = plugin;
		this.sender = sender;
		this.target = target.getUniqueId();
		this.manager = manager;
	}
	
	@Override
	public void run() {
		sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("syncStart"), plugin.getLangString("pluginTag"), sender.getName()));
		if(!manager.confirmFileExistance(target)) {
			sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("syncNoFile"), plugin.getLangString("pluginTag"), sender.getName()));
			return;
		}else {
			int count = manager.getPlacementCountFromLocationData(target);	
			if(count != manager.getPlacements(target)) {
				int oldCount = manager.getPlacements(target);
				manager.setPlacements(target, count);
				sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("syncChange"), plugin.getLangString("pluginTag"), sender.getName(), count, oldCount, true));
			}else {
				sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("syncNoChange"), plugin.getLangString("pluginTag"), sender.getName()));
			}
			
		}
	}
}
