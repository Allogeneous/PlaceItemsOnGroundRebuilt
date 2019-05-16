package me.Allogeneous.PlaceItemsOnGroundRebuilt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class PlaceItemsTabCompleter implements TabCompleter{

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String commandLable, String[] args) {
		if(command.getName().equalsIgnoreCase("placeitems")){
			if(args.length == 1){
				return Arrays.asList(new String[]{"help", "toggle", "set", "clear", "reload", "restorecap", "configname", "purge"});
			}
			if(args.length == 2) {
				if(args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("restorecap")) {
					List<String> names = new ArrayList<>();
					for(Player p : Bukkit.getServer().getOnlinePlayers()) {
						names.add(p.getName());
					}
					return names;
				}
				if(args[0].equalsIgnoreCase("clear") || args[0].equalsIgnoreCase("purge")) {
					return Arrays.asList(new String[]{"1", "10", "100", "250"});
				}
			}
			
			if(args.length == 3) {
				if(args[0].equalsIgnoreCase("set")) {
					return Arrays.asList(new String[]{"0", "1", "10", "100", "u"});
				}
			}
		}
		return null;
	}

}
