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
				String lowerArg0 = args[0].toLowerCase();
				if(lowerArg0.startsWith("h")){
					return Arrays.asList(new String[]{"help"});
				}
				if(lowerArg0.startsWith("t")){
					return Arrays.asList(new String[]{"toggle"});
				}
				if(lowerArg0.startsWith("r")){
					return Arrays.asList(new String[]{"rightclicktoggle", "reload", "restorecap"});
				}
				if(lowerArg0.startsWith("s")){
					return Arrays.asList(new String[]{"siderotation", "set"});
				}
				if(lowerArg0.startsWith("c")){
					return Arrays.asList(new String[]{"clear"});
				}
				if(lowerArg0.startsWith("p")){
					return Arrays.asList(new String[]{"purge", "playerdata"});
				}
				return Arrays.asList(new String[]{"help", "toggle", "rightclicktoggle", "siderotation", "set", "clear", "reload", "restorecap", "configname", "purge", "playerdata"});
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
				if(args[0].equalsIgnoreCase("siderotation")) {
					return Arrays.asList(new String[]{"0", "1", "2", "3", "4", "5", "6", "7"});
				}
				if(args[0].equalsIgnoreCase("playerdata")) {
					return Arrays.asList(new String[]{"show", "sync", "set"});
				}
				
			}
			
			if(args.length == 3) {
				if(args[0].equalsIgnoreCase("set")) {
					return Arrays.asList(new String[]{"0", "1", "10", "100", "u"});
				}
				
				if(args[0].equalsIgnoreCase("playerdata")) {
					List<String> names = new ArrayList<>();
					for(Player p : Bukkit.getServer().getOnlinePlayers()) {
						names.add(p.getName());
					}
					return names;
				}
			}
			
			if(args.length == 4) {
				if(args[0].equalsIgnoreCase("playerdata") && args[1].equalsIgnoreCase("set")) {
					return Arrays.asList(new String[]{"placecap", "hascustomplacecap", "amountplaced", "placetoggled", "rightclickpickuptoggled"});
				}
			}
			
			if(args.length == 5) {
				if(args[0].equalsIgnoreCase("playerdata") && args[1].equalsIgnoreCase("set")) {
					switch(args[3].toLowerCase()) {
						case "placecap":
							return Arrays.asList(new String[]{"-1", "0", "1", "10", "100"});
						case "hascustomplacecap":
							return Arrays.asList(new String[]{"true", "false"});
						case "amountplaced":
							return Arrays.asList(new String[]{"0", "1", "10", "100"});
						case "placetoggled":
							return Arrays.asList(new String[]{"true", "false"});
						case "rightclickpickuptoggled":
							return Arrays.asList(new String[]{"true", "false"});
						default:
							return null;
					}
				}
			}
		}
		return null;
	}

}
