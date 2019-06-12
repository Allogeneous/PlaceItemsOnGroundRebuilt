package me.Allogeneous.PlaceItemsOnGroundRebuilt;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import me.Allogeneous.PlaceItemsOnGroundRebuilt.Files.PlaceItemsManager;



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
						sender.sendMessage(ChatColor.AQUA + "====================" + ChatColor.BLUE + "PlaceItems Help" + ChatColor.AQUA + "====================");
						sender.sendMessage(ChatColor.YELLOW  +"/placeitems help" + ChatColor.AQUA + " - displays a list of commands");
						if(sender.hasPermission("placeitems.toggle")) {
							sender.sendMessage(ChatColor.YELLOW  +"/placeitems toggle" + ChatColor.AQUA + " - toggles the ability for a player to sneak and place items on the ground");
						}
						if(sender.hasPermission("placeitems.rightclicktoggle")) {
							sender.sendMessage(ChatColor.YELLOW  +"/placeitems rightclicktoggle" + ChatColor.AQUA + " - toggles the ability for a player to pick up placed items by right clicking");
						}
						if(sender.hasPermission("placeitems.siderotation")) {
							sender.sendMessage(ChatColor.YELLOW  +"/placeitems siderotation <0-7>" + ChatColor.AQUA + " - set the position that items placed on their side will be rotated at.");
						}
						if(sender.hasPermission("placeitems.set")) {
							sender.sendMessage(ChatColor.YELLOW  +"/placeitems set <name> <amount>" + ChatColor.AQUA + " - sets the amount of items a player can set on the ground (use a \"u\" in place of the amount for unlimited)");
						}
						if(sender.hasPermission("placeitems.clear")) {
							sender.sendMessage(ChatColor.YELLOW  +"/placeitems clear <radius>" + ChatColor.AQUA + " - removes all placed items within a specified block radius (1 - 250) safely");
						}
						if(sender.hasPermission("placeitems.reload")) {
							sender.sendMessage(ChatColor.YELLOW  +"/placeitems reload" + ChatColor.AQUA + " - reloads the plugin's config file");
						}
						if(sender.hasPermission("placeitems.restorecap")) {
							sender.sendMessage(ChatColor.YELLOW  +"/placeitems restorecap <name>" + ChatColor.AQUA + " - sets the players placement cap back to the default placement cap");
						}
						if(sender.hasPermission("placeitems.configname")) {
							sender.sendMessage(ChatColor.YELLOW  +"/placeitems configname" + ChatColor.AQUA + " - get the name of the item in your hand that will be accepted by lists in the config file");
						}
						if(sender.hasPermission("placeitems.purge")) {
							sender.sendMessage(ChatColor.DARK_RED + "=====================" + ChatColor.RED + "Danger Zone" + ChatColor.DARK_RED + "=====================");
							sender.sendMessage(ChatColor.YELLOW  +"/placeitems purge <radius>" + ChatColor.AQUA + " - this command is designed to fix bugs if a location is not allowing an item to be placed on it");
							sender.sendMessage(ChatColor.RED  + "Warning: This command is similar to " + ChatColor.YELLOW  +"/placeitems clear <radius>" + ChatColor.RED + " (try using this command first), but if needed, this command will remove ALL ARMOR STANDS (even those not placed by this plugin) in the given radius. It will also purge the location data file of all place items data in the specified radius (1 - 250) blocks!");
						}
						sender.sendMessage(ChatColor.AQUA + "====================" + ChatColor.BLUE + "PlaceItems Help" + ChatColor.AQUA + "====================");
					}else {
						sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "You do not have permission to use that command!");
					}
					return true;
				}
					
				if(arg.equalsIgnoreCase("toggle")){ 
					if(sender.hasPermission("placeitems.toggle")){
						if(sender instanceof Player) {
							Player p = (Player) sender;
							if(!manager.getPlaceToggled(p)){
								manager.setPlaceToggled(p, true);
								p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.GREEN + "You can now place items!");
							}else{
								manager.setPlaceToggled(p, false);
								p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You can no longer place items!");
							}
						}else {
							sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You must be a player to send that command!");
						}
					}else {
						sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "You do not have permission to use that command!");
					}
					return true;
				}
				
				if(arg.equalsIgnoreCase("rightclicktoggle")){ 
					if(sender.hasPermission("placeitems.rightclicktoggle")){
						if(sender instanceof Player) {
							Player p = (Player) sender;
							if(!manager.getRightClickPickupToggled(p)){
								manager.setRightClickPickupToggled(p, true);
								p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.GREEN + "You can now right click to pickup items!");
							}else{
								manager.setRightClickPickupToggled(p, false);
								p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You can no longer right click to pickup items!");
							}
						}else {
							sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You must be a player to send that command!");
						}
					}else {
						sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "You do not have permission to use that command!");
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
								sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "That player is not online!");
								return true;
							}
						
							int numberAmount = 0;
							boolean unltd = false;
						
							try{
								numberAmount = Integer.parseInt(amount);
								if(numberAmount < 0){
									sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "Amount must be greater than 0!");
									return true;
								}
							}catch(Exception e){
								if(amount.length() == 1){
									char c = amount.charAt(0);
									if(c != PlaceItemsManager.UNLIMITED_CHAR_1 && c != PlaceItemsManager.UNLIMITED_CHAR_2){
										sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "Invalid cap number! Use " + ChatColor.YELLOW + " u " + ChatColor.RED + "for unlimited.");
										return true;
									}else{
										unltd = true;
										numberAmount = PlaceItemsManager.UNLIMITED;
									}
								}else{
									sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "Invalid cap number!");
									return true;
								}
							}
						
							manager.setMaxPlacements(target, numberAmount);
							manager.setHasCustomPlaceCap(target, true);
						
							for(PermissionAttachmentInfo attachmentInfo : target.getEffectivePermissions()) {
							      if(attachmentInfo.getPermission().startsWith("placeitems.cap.")) {
							    	  sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "Warning: "+ target.getName() + " already has a cap set via permissions! This command will update the file based placement cap but, the permission cap will override the file based one until it is removed!");
							    	  break;
							      }
							}
							
							if(unltd) {
								sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.AQUA + target.getName() + "'s file based item placement cap has been set to " + ChatColor.GREEN + "UNLIMITED!");
							}else {
								sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.AQUA + target.getName() + "'s file based item placement cap has been set to " + ChatColor.GREEN + numberAmount + "!");
							}
						}
					}else {
						sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "You do not have permission use that command!");
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
										sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "Invalid rotation number! Use the numbers " + ChatColor.YELLOW + "0 - 7" + ChatColor.RED + "!");
										return true;
									}
								}catch(Exception e) {
									sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "Invalid rotation number! Use the numbers " + ChatColor.YELLOW + "0 - 7" + ChatColor.RED + "!");
									return true;
								}
								
								if(manager.getPlayerRotationPositions().containsKey(player.getUniqueId()) && rotation == 0) {
									manager.getPlayerRotationPositions().remove(player.getUniqueId());
								}else {
									manager.getPlayerRotationPositions().put(player.getUniqueId(), rotation);
								}
								
								sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.AQUA + "Your side rotation position has been set to " + ChatColor.GREEN + rotation + ChatColor.AQUA + "!");
							}
						}else {
							sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You must be a player to send that command!");
						}
					}else {
						sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "You do not have permission use that command!");
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
										sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "Radius must be beteen 1 and 250");
										return true;
									}
								}catch(Exception e){
									sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "Radius must be an integer beteen 1 and 250");
									return true;
								}
							
								Player p = (Player) sender;
							
								int count = PlaceItemsUtils.removeInRadiusAroundPlayer(p.getLocation(), manager, numberRadius);
							
								p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.AQUA + "Cleareded " + ChatColor.GREEN + count + ChatColor.AQUA + " placed items in a " + ChatColor.GREEN + numberRadius + ChatColor.AQUA + " block radius!");
							}
						}else {
							sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You must be a player to send that command!");
						}
					}else {
						sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "You do not have permission use that command!");
					}
					return true;
				}
					
				if(arg.equalsIgnoreCase("reload")) {
					if(sender.hasPermission("placeitems.reload")){
						if(args.length == 1){
							plugin.reloadTheConfigFile();
							sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.AQUA + "Config file reloaded!");
						}
					}else {
						sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "You do not have permission use that command!");
					}
					return true;
				}
				
				if(arg.equalsIgnoreCase("restorecap")) {
					if (sender.hasPermission("placeitems.restore")){
						if(args.length == 2){
						
							String name = args[1];
						
							Player target = Bukkit.getServer().getPlayer(name);
						
							if(target == null){
								sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "That player is not online!");
								return true;
							}
						
							manager.setMaxPlacements(target, 0);
							manager.setHasCustomPlaceCap(target, false);
							for(PermissionAttachmentInfo attachmentInfo : target.getEffectivePermissions()) {
							      if(attachmentInfo.getPermission().startsWith("placeitems.cap.")) {
							    	  sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "Warning: "+ target.getName() + " already has a cap set via permissions! This command will reset the file based placement cap but, the permission cap will override the file based one until it is removed!");
							    	  break;
							      }
							}
							sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.AQUA + target.getName() + "'s file based placement cap has been reset to the default placement cap!");
						}
					}else {
						sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "You do not have permission use that command!");
					}
					return true;
				}
				
				if(arg.equalsIgnoreCase("configname")) {
					if (sender.hasPermission("placeitems.configname")){
						if(sender instanceof Player) {
							if(args.length == 1){
						
								Player p = (Player) sender;
						
								if(p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() != Material.AIR) {
									p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.AQUA + "Enter " + ChatColor.YELLOW + p.getInventory().getItemInMainHand().getType() + ChatColor.AQUA + " into the config file list.");
								}else {
									p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "Your hand is empty.");
								}
							}
						}
					}else {
						sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "You do not have permission use that command!");
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
										sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "Radius must be beteen 1 and 250");
										return true;
									}
								}catch(Exception e){
									sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "Radius must be an integer beteen 1 and 250");
									return true;
								}
							
								Player p = (Player) sender;
							
								int[] removes = PlaceItemsUtils.purgeInRadiusAroundPlayer(p.getLocation(), manager, numberRadius);
							
								p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.AQUA + "Purged " + ChatColor.GREEN + removes[0] + ChatColor.AQUA + " locations and " + ChatColor.GREEN + removes[1] + ChatColor.AQUA +  " armor stands in a "  + ChatColor.GREEN + numberRadius + ChatColor.AQUA + " block radius!");
							}
						}else {
							sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You must be a player to send that command!");
						}
					}else {
						sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "You do not have permission use that command!");
					}
					return true;
				}
				
			}else {
				sender.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.AQUA + "Try " + ChatColor.YELLOW  + "/placeitems help" + ChatColor.AQUA + " for help!");
			}
			
		}
		return true;
	}

}


