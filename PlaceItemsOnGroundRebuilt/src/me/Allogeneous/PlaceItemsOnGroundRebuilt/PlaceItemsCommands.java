package me.Allogeneous.PlaceItemsOnGroundRebuilt;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import me.Allogeneous.PlaceItemsOnGroundRebuilt.Files.PlaceItemsManager;
import me.Allogeneous.PlaceItemsOnGroundRebuilt.Threads.PlaceItemsDataSyncer;



public class PlaceItemsCommands implements CommandExecutor{
	
	private PlaceItemsManager manager;
	private PlaceItemsMain plugin;
	
	public PlaceItemsCommands(PlaceItemsMain plugin, PlaceItemsManager manager){
		this.plugin = plugin;
		this.manager = manager;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(commandLabel.equalsIgnoreCase("placeitems")){
			if(args.length > 0){
				String arg = args[0];
				if(arg.equalsIgnoreCase("help")) {
					if(sender.hasPermission("placeitems.help")){
						sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("helpMenuLine1"), plugin.getLangString("pluginTag"), sender.getName()));
						sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("helpMenuLine2"), plugin.getLangString("pluginTag"), sender.getName()));
						if(sender.hasPermission("placeitems.toggle")) {
							sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("helpMenuLine3"), plugin.getLangString("pluginTag"), sender.getName()));
						}
						if(sender.hasPermission("placeitems.rightclicktoggle")) {
							sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("helpMenuLine4"), plugin.getLangString("pluginTag"), sender.getName()));
						}
						if(sender.hasPermission("placeitems.siderotation")) {
							sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("helpMenuLine5"), plugin.getLangString("pluginTag"), sender.getName()));
						}
						if(sender.hasPermission("placeitems.set")) {
							sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("helpMenuLine6"), plugin.getLangString("pluginTag"), sender.getName()));
						}
						if(sender.hasPermission("placeitems.clear")) {
							sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("helpMenuLine7"), plugin.getLangString("pluginTag"), sender.getName()));
						}
						if(sender.hasPermission("placeitems.reload")) {
							sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("helpMenuLine8"), plugin.getLangString("pluginTag"), sender.getName()));
						}
						if(sender.hasPermission("placeitems.restorecap")) {
							sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("helpMenuLine9"), plugin.getLangString("pluginTag"), sender.getName()));
						}
						if(sender.hasPermission("placeitems.configname")) {
							sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("helpMenuLine10"), plugin.getLangString("pluginTag"), sender.getName()));
						}
						if(sender.hasPermission("placeitems.purge") || sender.hasPermission("placeitems.playerdata")) {
							sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("helpMenuLine11"), plugin.getLangString("pluginTag"), sender.getName()));
							if(sender.hasPermission("placeitems.playerdata")) {
								sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("helpMenuLine12"), plugin.getLangString("pluginTag"), sender.getName()));
								sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("helpMenuLine13"), plugin.getLangString("pluginTag"), sender.getName()));
							}
							if(sender.hasPermission("placeitems.purge")) {
								sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("helpMenuLine14"), plugin.getLangString("pluginTag"), sender.getName()));
								sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("helpMenuLine15"), plugin.getLangString("pluginTag"), sender.getName()));
							}
						}
						sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("helpMenuLine16"), plugin.getLangString("pluginTag"), sender.getName()));
					}else {
						sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("commandNoPermission"), plugin.getLangString("pluginTag"), sender.getName()));
					}
					return true;
				}
				
				if(arg.equalsIgnoreCase("toggle")){ 
					if(sender.hasPermission("placeitems.toggle")){
						if(sender instanceof Player) {
							Player p = (Player) sender;
							if(!manager.getPlaceToggled(p)){
								manager.setPlaceToggled(p, true);
								sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("toggleOn"), plugin.getLangString("pluginTag"), sender.getName()));
							}else{
								manager.setPlaceToggled(p, false);
								sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("toggleOff"), plugin.getLangString("pluginTag"), sender.getName()));
							}
						}else {
							sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("notAPlayer"), plugin.getLangString("pluginTag"), sender.getName()));
						}
					}else {
						sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("commandNoPermission"), plugin.getLangString("pluginTag"), sender.getName()));
					}
					return true;
				}
				
				if(arg.equalsIgnoreCase("rightclicktoggle")){ 
					if(sender.hasPermission("placeitems.rightclicktoggle")){
						if(sender instanceof Player) {
							Player p = (Player) sender;
							if(!manager.getRightClickPickupToggled(p)){
								manager.setRightClickPickupToggled(p, true);
								sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("rcToggleOn"), plugin.getLangString("pluginTag"), sender.getName()));
							}else{
								manager.setRightClickPickupToggled(p, false);
								sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("rcToggleOff"), plugin.getLangString("pluginTag"), sender.getName()));
							}
						}else {
							sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("notAPlayer"), plugin.getLangString("pluginTag"), sender.getName()));
						}
					}else {
						sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("commandNoPermission"), plugin.getLangString("pluginTag"), sender.getName()));
					}
					return true;
				}
					
				if(arg.equalsIgnoreCase("set")) {
					if(sender.hasPermission("placeitems.set")){
						if(args.length == 3){
						
							String name = args[1];
							String amount = args[2];
						
							Player target = Bukkit.getServer().getPlayer(name);
						
							if(target == null){
								sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("playerNotOnline"), plugin.getLangString("pluginTag"), sender.getName()));
								return true;
							}
						
							int numberAmount = 0;
							boolean unltd = false;
						
							try{
								numberAmount = Integer.parseInt(amount);
								if(numberAmount < 0){
									sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("setLessThanZero"), plugin.getLangString("pluginTag"), sender.getName()));
									return true;
								}
							}catch(Exception e){
								if(amount.length() == 1){
									char c = amount.charAt(0);
									if(c != PlaceItemsManager.UNLIMITED_CHAR_1 && c != PlaceItemsManager.UNLIMITED_CHAR_2){
										sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("setInvalidUnlimited"), plugin.getLangString("pluginTag"), sender.getName()));
										return true;
									}else{
										unltd = true;
										numberAmount = PlaceItemsManager.UNLIMITED;
									}
								}else{
									sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("setInvalidAmount"), plugin.getLangString("pluginTag"), sender.getName()));
									return true;
								}
							}
						
							manager.setMaxPlacements(target, numberAmount);
							manager.setHasCustomPlaceCap(target, true);
						
							for(PermissionAttachmentInfo attachmentInfo : target.getEffectivePermissions()) {
							      if(attachmentInfo.getPermission().startsWith("placeitems.cap.")) {
							    	  sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("setCapPermissionWarning"), plugin.getLangString("pluginTag"), sender.getName(), target.getName()));
							    	  break;
							      }
							}
							
							if(unltd) {
								sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("setCapUnlimited"), plugin.getLangString("pluginTag"), sender.getName(), target.getName()));
							}else {
								sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("setCapAmount"), plugin.getLangString("pluginTag"), sender.getName(), target.getName(), Integer.toString(numberAmount)));
							}
						}
					}else {
						sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("commandNoPermission"), plugin.getLangString("pluginTag"), sender.getName()));
					}
					return true;
				}
				
				if(arg.equalsIgnoreCase("siderotation")) {
					if (sender.hasPermission("placeitems.siderotation")){
						if(sender instanceof Player) {
							if(args.length == 2){
								Player player = (Player) sender;
								int rotation = 0;
								try {
									rotation = Integer.parseInt(args[1]);
									if(rotation < 0 || rotation > 7) {
										sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("siderotationInvalidNum"), plugin.getLangString("pluginTag"), sender.getName(), rotation));
										return true;
									}
								}catch(Exception e) {
									sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("siderotationInvalidNum"), plugin.getLangString("pluginTag"), sender.getName(), rotation));
									return true;
								}
								
								if(manager.getPlayerRotationPositions().containsKey(player.getUniqueId()) && rotation == 0) {
									manager.getPlayerRotationPositions().remove(player.getUniqueId());
								}else {
									manager.getPlayerRotationPositions().put(player.getUniqueId(), rotation);
								}
								
								sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("sideRotationSet"), plugin.getLangString("pluginTag"), sender.getName(), rotation));
							}
						}else {
							sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("notAPlayer"), plugin.getLangString("pluginTag"), sender.getName()));
						}
					}else {
						sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("commandNoPermission"), plugin.getLangString("pluginTag"), sender.getName()));
					}
					return true;
				}
				
				if(arg.equalsIgnoreCase("clear")) { 
					if (sender.hasPermission("placeitems.clear")){
						if(sender instanceof Player) {
							if(args.length == 2){
								String radius = args[1];
								int numberRadius = 0;
								try{
									numberRadius = Integer.parseInt(radius);
									if(numberRadius < 1 || numberRadius > 250){
										sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("clearInvalid"), plugin.getLangString("pluginTag"), sender.getName()));
										return true;
									}
								}catch(Exception e){
									sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("clearInvalidInt"), plugin.getLangString("pluginTag"), sender.getName()));
									return true;
								}
							
								Player p = (Player) sender;
							
								int count = PlaceItemsUtils.removeInRadiusAroundPlayer(p.getLocation(), manager, numberRadius);
								sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("clearValid"), plugin.getLangString("pluginTag"), sender.getName(), count, numberRadius));
							}
						}else {
							sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("notAPlayer"), plugin.getLangString("pluginTag"), sender.getName()));
						}
					}else {
						sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("commandNoPermission"), plugin.getLangString("pluginTag"), sender.getName()));
					}
					return true;
				}
					
				if(arg.equalsIgnoreCase("reload")) {
					if(sender.hasPermission("placeitems.reload")){
						if(args.length == 1){
							plugin.reloadTheConfigFile();
							plugin.getLangFile().loadLanguageFile();
							sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("reloadDone"), plugin.getLangString("pluginTag"), sender.getName()));
						}
					}else {
						sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("commandNoPermission"), plugin.getLangString("pluginTag"), sender.getName()));
					}
					return true;
				}
				
				if(arg.equalsIgnoreCase("restorecap")) {
					if (sender.hasPermission("placeitems.restore")){
						if(args.length == 2){
						
							String name = args[1];
						
							Player target = Bukkit.getServer().getPlayer(name);
						
							if(target == null){
								sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("playerNotOnline"), plugin.getLangString("pluginTag"), sender.getName()));
								return true;
							}
						
							manager.setMaxPlacements(target, 0);
							manager.setHasCustomPlaceCap(target, false);
							for(PermissionAttachmentInfo attachmentInfo : target.getEffectivePermissions()) {
							      if(attachmentInfo.getPermission().startsWith("placeitems.cap.")) {
							    	  sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("restoreCapWarning"), plugin.getLangString("pluginTag"), sender.getName(), target.getName()));
							    	  break;
							      }
							}
							sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("restoreCapDone"), plugin.getLangString("pluginTag"), sender.getName(), target.getName()));
						}
					}else {
						sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("commandNoPermission"), plugin.getLangString("pluginTag"), sender.getName()));
					}
					return true;
				}
				
				if(arg.equalsIgnoreCase("configname")) {
					if (sender.hasPermission("placeitems.configname")){
						if(sender instanceof Player) {
							if(args.length == 1){
						
								Player p = (Player) sender;
						
								if(p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() != Material.AIR) {
									sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("configNameHandItem"), plugin.getLangString("pluginTag"), sender.getName(), p.getInventory().getItemInMainHand()));
								}else {
									sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("configNameError"), plugin.getLangString("pluginTag"), sender.getName()));
								}
							}
						}
					}else {
						sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("commandNoPermission"), plugin.getLangString("pluginTag"), sender.getName()));
					}
					return true;
				}
				
				if(arg.equalsIgnoreCase("purge")) { 
					if (sender.hasPermission("placeitems.purge")){
						if(sender instanceof Player) {
							if(args.length == 2){
								String radius = args[1];
								int numberRadius = 0;
								try{
									numberRadius = Integer.parseInt(radius);
									if(numberRadius < 1 || numberRadius > 250){
										sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("purgeInvalid"), plugin.getLangString("pluginTag"), sender.getName()));
										return true;
									}
								}catch(Exception e){
									sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("purgeInvalidInt"), plugin.getLangString("pluginTag"), sender.getName()));
									return true;
								}
							
								Player p = (Player) sender;
							
								int[] removes = PlaceItemsUtils.purgeInRadiusAroundPlayer(p.getLocation(), manager, numberRadius);
								sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("purgeValid"), plugin.getLangString("pluginTag"), sender.getName(), removes[1], numberRadius, removes[0]));
							}
						}else {
							sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("notAPlayer"), plugin.getLangString("pluginTag"), sender.getName()));
						}
					}else {
						sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("commandNoPermission"), plugin.getLangString("pluginTag"), sender.getName()));
					}
					return true;
				}
				
				if(arg.equalsIgnoreCase("playerdata")){ 
					if(sender.hasPermission("placeitems.playerdata")) {
						if(args.length == 3) {
							if(args[1].equalsIgnoreCase("show")) {
								Player target = Bukkit.getPlayer(args[2]);
								if(target != null) {
									sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("playerDataLine1"), plugin.getLangString("pluginTag"), sender.getName()));
									sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("playerDataLine2"), plugin.getLangString("pluginTag"), sender.getName(), manager.getUUID(target).toString(), (byte) 0));
									sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("playerDataLine3"), plugin.getLangString("pluginTag"), sender.getName(), manager.getName(target), (byte) 1));
									sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("playerDataLine4"), plugin.getLangString("pluginTag"), sender.getName(), Integer.toString(manager.getRawMaxPlacements(target)), (byte) 2));
									sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("playerDataLine5"), plugin.getLangString("pluginTag"), sender.getName(), Boolean.toString(manager.getHasCustomPlaceCap(target)), (byte) 3));
									sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("playerDataLine6"), plugin.getLangString("pluginTag"), sender.getName(), Integer.toString(manager.getPlacements(target)), (byte) 4));
									sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("playerDataLine7"), plugin.getLangString("pluginTag"), sender.getName(), Boolean.toString(manager.getPlaceToggled(target)), (byte) 5));
									sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("playerDataLine8"), plugin.getLangString("pluginTag"), sender.getName(), Boolean.toString(manager.getRightClickPickupToggled(target)), (byte) 6));
									sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("playerDataLine9"), plugin.getLangString("pluginTag"), sender.getName()));
								}
							}
							if(args[1].equalsIgnoreCase("sync")) {
								Player target = Bukkit.getPlayer(args[2]);
								if(target != null) {
									PlaceItemsDataSyncer pids = new PlaceItemsDataSyncer(plugin, sender, target, manager);
									pids.runTaskAsynchronously(plugin);
									return true;
								}else {
									sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("playerNotOnline"), plugin.getLangString("pluginTag"), sender.getName()));
									return true;
								}
							}
						}
						
						if(args.length == 5) {
							if(args[1].equalsIgnoreCase("set")) {
								Player target = Bukkit.getPlayer(args[2]);
								if(target != null) {
									switch(args[3].toLowerCase()) {
										case "placecap":
											int placecap = 0;
											try {
												placecap = Integer.parseInt(args[4]);
												manager.setMaxPlacements(target, placecap);
												sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("placeCapSet"), plugin.getLangString("pluginTag"), sender.getName(), target.getName(), Integer.toString(placecap), (byte) 0));
											}catch(Exception e) {
												sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("placeCapError"), plugin.getLangString("pluginTag"), sender.getName()));
											}
											return true;
										case "hascustomplacecap":
											boolean hascustomplacecap = false;
											try {
												hascustomplacecap = Boolean.parseBoolean(args[4]);
												manager.setHasCustomPlaceCap(target, hascustomplacecap);
												sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("hasCustomPlaceCapSet"), plugin.getLangString("pluginTag"), sender.getName(), target.getName(), Boolean.toString(hascustomplacecap), (byte) 1));
											}catch(Exception e) {
												sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("hasCustomPlaceCapError"), plugin.getLangString("pluginTag"), sender.getName()));
											}
											return true;
										case "amountplaced":
											int amountplaced = 0;
											try {
												amountplaced = Integer.parseInt(args[4]);
												manager.setPlacements(target, amountplaced);
												sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("amountPlacedSet"), plugin.getLangString("pluginTag"), sender.getName(), target.getName(), Integer.toString(amountplaced), (byte) 2));
											}catch(Exception e) {
												sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("amountPlacedError"), plugin.getLangString("pluginTag"), sender.getName()));
											}
											return true;
										case "placetoggled":
											boolean placetoggled = false;
											try {
												placetoggled = Boolean.parseBoolean(args[4]);
												manager.setPlaceToggled(target, placetoggled);
												sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("placeToggledSet"), plugin.getLangString("pluginTag"), sender.getName(), target.getName(), Boolean.toString(placetoggled), (byte) 3));
											}catch(Exception e) {
												sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("placeToggledError"), plugin.getLangString("pluginTag"), sender.getName()));
											}
											return true;
										case "rightclickpickuptoggled":
											boolean rightclickpickuptoggled = false;
											try {
												rightclickpickuptoggled = Boolean.parseBoolean(args[4]);
												manager.setRightClickPickupToggled(target, rightclickpickuptoggled);
												sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("rightClickPickupToggledSet"), plugin.getLangString("pluginTag"), sender.getName(), target.getName(), Boolean.toString(rightclickpickuptoggled), (byte) 4));
											}catch(Exception e) {
												sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("rightClickPickupToggledError"), plugin.getLangString("pluginTag"), sender.getName()));
											}
											return true;
										default:
											return true;
									}
								}else {
									sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("playerNotOnline"), plugin.getLangString("pluginTag"), sender.getName()));
									return true;
								}
							}
						}
					}else {
						sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("commandNoPermission"), plugin.getLangString("pluginTag"), sender.getName()));
					}
				}
				
			}else {
				sender.sendMessage(plugin.getMessageParser().parse(plugin.getLangString("notACommand"), plugin.getLangString("pluginTag"), sender.getName()));
			}
			
		}
		
		
		return true;
	}

}


