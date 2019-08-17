package me.Allogeneous.PlaceItemsOnGroundRebuilt;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachmentInfo;

import me.Allogeneous.PlaceItemsOnGroundRebuilt.Files.AdvancedPlaceItemsLinkedLocation;
import me.Allogeneous.PlaceItemsOnGroundRebuilt.Files.PlaceItemsManager;
import me.Allogeneous.PlaceItemsOnGroundRebuilt.Files.PlaceItemsPlayerPlaceLocation;
import me.Allogeneous.PlaceItemsOnGroundRebuilt.PlacementPositioningCases.BlockBottomPositioningCases;
import me.Allogeneous.PlaceItemsOnGroundRebuilt.PlacementPositioningCases.BlockSidePositioningCases;
import me.Allogeneous.PlaceItemsOnGroundRebuilt.PlacementPositioningCases.BlockTopPositioningCases;
import me.Allogeneous.PlaceItemsOnGroundRebuilt.PlacementPositioningCases.PlaceItemsSpecialCases;
import me.Allogeneous.PlaceItemsOnGroundRebuilt.mcMMO.McMMOPlacementEventManager;

public class PlaceItemsEvents implements Listener{
	
	private PlaceItemsMain plugin;
	private PlaceItemsManager manager;
	private PlaceItemsVersionSensitiveMethods versionHandler;
	private McMMOPlacementEventManager mcMMOpeh;
	
	public PlaceItemsEvents(PlaceItemsMain plugin, PlaceItemsManager manager, PlaceItemsVersionSensitiveMethods versionHandler){
		this.plugin = plugin;
		this.manager = manager;
		this.versionHandler = versionHandler;
		this.mcMMOpeh = new McMMOPlacementEventManager();
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		manager.makeNewPlayerFile(e.getPlayer());
		manager.updateUsername(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerPlace(PlayerInteractEvent e){
		Player p = e.getPlayer();
		
		if(e.getHand() != EquipmentSlot.HAND || !p.isSneaking() || !manager.getPlaceToggled(p)) {
			return;
		}
		
		if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR) {
			return;
		}
		

		if(e.getClickedBlock() == null) {
			return;
		}

		if(e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		
		if(PlaceItemsConfig.isStrictCompatibilityMode()) {
			if(p.getInventory().getItemInMainHand().hasItemMeta()) {
				if(p.getInventory().getItemInMainHand().getItemMeta().hasLore() || p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
					p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "Strict compatibilty mode is running! Blocks and items must not have custom names or lore!");
					return;
				}
			}
		}
		
		if(plugin.mcMMO) {
			boolean value = mcMMOpeh.handlePlacementDataPrefire(e.getClickedBlock());
			BlockPlaceEvent bpe = new BlockPlaceEvent(e.getClickedBlock(), e.getClickedBlock().getState(), e.getClickedBlock(), p.getInventory().getItemInMainHand(), p, true, EquipmentSlot.HAND);
			Bukkit.getServer().getPluginManager().callEvent(bpe);
			mcMMOpeh.handlePlacementDataPostfire(e.getClickedBlock(), value);
			if(bpe.isCancelled()) {
				return;
			}
		}else {
			BlockPlaceEvent bpe = new BlockPlaceEvent(e.getClickedBlock(), e.getClickedBlock().getState(), e.getClickedBlock(), p.getInventory().getItemInMainHand(), p, true, EquipmentSlot.HAND);
			Bukkit.getServer().getPluginManager().callEvent(bpe);
			if(bpe.isCancelled()) {
				return;
			}
		}

		if(isBlockey(p.getInventory().getItemInMainHand())) {
			if(manager.containsPropWithPhysicalBlockFace(e.getClickedBlock().getLocation(), e.getBlockFace())) {
				e.setCancelled(true);
				return;
			}
		}

		switch(e.getBlockFace().toString()) {
		case "UP":
			if(!PlaceItemsConfig.isAllowTopPlacing()) {
				p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "Placing items on the top of blocks has been disabled!");
				return;
			}
			if(!p.hasPermission("placeitems.place")) {
				if(!p.hasPermission("placeitems.place.top")) {
					p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "You do not have permission to place items on the top of blocks!");
					return;
				}
			}
			break;
		case "DOWN":
			if(!PlaceItemsConfig.isAllowBottomPlacing()) {
				p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "Placing items on the bottom of blocks has been disabled!");
				return;
			}
			if(!p.hasPermission("placeitems.place")) {
				if(!p.hasPermission("placeitems.place.bottom")) {
					p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "You do not have permission to place items on the bottom of blocks!");
					return;
				}
			}
			break;
		case "NORTH":
		case "SOUTH":
		case "WEST":
		case "EAST":
			if(!PlaceItemsConfig.isAllowSidePlacing()) {
				p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "Placing items on the sides of blocks has been disabled!");
				return;
			}
			if(!p.hasPermission("placeitems.place")) {
				if(!p.hasPermission("placeitems.place.sides")) {
					p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "You do not have permission to place items on the sides of blocks!");
					return;
				}
			}
			break;
		default:
			p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "Error: Unknown blockface!");
			e.setCancelled(true);
			return;
		}



		if(PlaceItemsUtils.isBlacklisted(p.getInventory().getItemInMainHand().getType(), e.getBlockFace().toString())) {
			p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You cannot place " + ChatColor.YELLOW + p.getInventory().getItemInMainHand().getType() + ChatColor.RED + "!");
			e.setCancelled(true);
			return;
		}


		if(manager.containsPropWithPhysicalBlockFace(e.getClickedBlock().getLocation(), e.getBlockFace())){
			p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You cannot place more than one item on one side of a block!");
			e.setCancelled(true);
			return;
		}


		if(!isValidPlace(e.getClickedBlock().getType(), e.getBlockFace())) {
			p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You cannot place an item on that block!");
			e.setCancelled(true);
			return;
		}

		if(PlaceItemsUtils.isSlab(e.getClickedBlock().getType())) {
			if(!versionHandler.isValidSlab(e.getClickedBlock(), e.getBlockFace().toString())) {
				p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You cannot place an item on that block!");
				e.setCancelled(true);
				return;
			}
		}

		if(!handlePermissionCheck(p)) {
			return;
		}
		

		e.setCancelled(true);
		longPlacementCaseMethod(p, e.getClickedBlock(), e.getBlockFace());

		
	}
	
	private void updateArea(Location location){
		if(location.getBlock().getType() == Material.AIR){
			location.getBlock().setType(Material.GLASS);
			location.getBlock().setType(Material.AIR);
		}
	
	}
	
	private boolean isValidPlace(Material type, BlockFace blockFace) {
		if(!PlaceItemsUtils.isBlackListedPlaceItem(type, blockFace.toString())) {
			if(type.isBlock() && type.isSolid()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isBlockey(ItemStack item){
		if(!PlaceItemsUtils.isItemLikeBlock(item.getType())){
			if(item.getType().isBlock()){
				return true;
			}
			if(PlaceItemsUtils.isBlockLikeItem(item.getType())){
				return true;
			}
		}
		return false;
	}
	
	@EventHandler
	public void onPlayerTakeClick(PlayerInteractAtEntityEvent e){
		if(e.isCancelled()) {
			return;
		}
		if(e.getRightClicked() instanceof ArmorStand){
			ArmorStand a = (ArmorStand) e.getRightClicked();
			if(!a.isVisible() && !a.hasBasePlate() && !a.hasGravity() && a.isInvulnerable()){
				if(manager.containsProp(PlaceItemsUtils.getPotentialPhysicalLocations(a.getLocation()), a.getLocation())){
					e.setCancelled(true);
					Player p = e.getPlayer();
					if(!manager.getRightClickPickupToggled(p)) {
						return;
					}
					if(!p.hasPermission("placeitems.take")) {
						p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "You do not have permission to take items!");
						return;
					}
					
					
					PlaceItemsOnGroundBreakEvent bbe = new PlaceItemsOnGroundBreakEvent(manager.getFromProp(PlaceItemsUtils.getPotentialPhysicalLocations(a.getLocation()), a.getLocation()).getBlock(), e.getPlayer());
					Bukkit.getServer().getPluginManager().callEvent(bbe);
					
					if(bbe.isCancelled()) {
						return;
					}
					
					ItemStack hem = a.getHelmet();
					hem.setAmount(1);
					if(p.getInventory().firstEmpty() != -1){
						p.getInventory().addItem(hem);
					}else{
						p.getWorld().dropItemNaturally(e.getRightClicked().getLocation().clone().add(0, 1, 0), hem);
					}
					
					PlaceItemsPlayerPlaceLocation pippl = manager.getPlayerPlaceFromProp(PlaceItemsUtils.getPotentialPhysicalLocations(a.getLocation()), a.getLocation());
					
					manager.setPlacements(pippl.getPlacer(), manager.getPlacements(pippl.getPlacer()) - 1);
					manager.removeProp(PlaceItemsUtils.getPotentialPhysicalLocations(a.getLocation()), a.getLocation());
					a.remove();
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerTakeBreak(BlockBreakEvent e){
		if(e instanceof PlaceItemsOnGroundBreakEvent) {
			return;
		}
		if(e.isCancelled()) {
			return;
		}
		
		
		if(manager.containsPhysical(e.getBlock().getLocation())){
			
			Player p = e.getPlayer();
			if(!p.hasPermission("placeitems.take")) {
				e.setCancelled(true);
				p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "You do not have permission to take items!");
				return;
			}
			
			AdvancedPlaceItemsLinkedLocation physical = manager.getFromPhysical(e.getBlock().getLocation());
			
			for(int i = 0; i < physical.getProps().length; i++) {
				if(physical.getProps()[i] == null) {
					continue;
				}
				Collection<Entity> armorstand = e.getBlock().getLocation().getWorld().getNearbyEntities(physical.getProps()[i].getLocation(), 0.001, 0.001, 0.001);
				ArmorStand a = null;
				
				for(Entity entity : armorstand){
					if(entity instanceof ArmorStand){
						if(entity.getLocation().getWorld().equals(physical.getProps()[i].getLocation().getWorld()) && entity.getLocation().getX() == physical.getProps()[i].getLocation().getX() && entity.getLocation().getY() == physical.getProps()[i].getLocation().getY() && entity.getLocation().getZ() == physical.getProps()[i].getLocation().getZ()) {
							a = (ArmorStand) entity;
							break;
						}
					}
				}
				
				if(a != null){
					if(!a.isVisible() && !a.hasBasePlate() && !a.hasGravity() && a.isInvulnerable()){
						ItemStack hem = a.getHelmet();
						hem.setAmount(1);
						p.getWorld().dropItemNaturally(e.getBlock().getLocation(), hem);
						PlaceItemsPlayerPlaceLocation pippl = manager.getPlayerPlaceFromProp(PlaceItemsUtils.getPotentialPhysicalLocations(a.getLocation()), a.getLocation());
						manager.setPlacements(pippl.getPlacer(), manager.getPlacements(pippl.getPlacer()) - 1);
						a.remove();
					}
				}
			}
			manager.removePhysical(e.getBlock().getLocation());
		}
	}

	@EventHandler
	public void onPistonPushPlacedItem(BlockPistonExtendEvent e) {
		if(PlaceItemsUtils.placedItemsAreInRadius(e.getBlock().getLocation(), manager, 2)) {
			e.setCancelled(true);
			return;
		}
		
		for(Block b : e.getBlocks()) {
			if(PlaceItemsUtils.placedItemsAreInRadius(b.getLocation(), manager, 2)) {
				e.setCancelled(true);
				return;
			}
		}
	}
	
	private void longPlacementCaseMethod(Player p, Block clickedBlock, BlockFace blockFace) {
			if(blockFace == BlockFace.UP) {
				Location check = clickedBlock.getLocation().add(0, 1, 0);
				if(!PlaceItemsUtils.isPlaceIn(check.getBlock().getType())) {
					p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You cannot place an item in " + ChatColor.YELLOW + check.getBlock().getType() + ChatColor.RED + "!");
					return;
				}
				if(isBlockey(p.getInventory().getItemInMainHand())){
					if(PlaceItemsSpecialCases.isSpecialCases2(p.getInventory().getItemInMainHand().getType())) {
						ArmorStand a = createArmorStand(clickedBlock.getLocation().add(0.5, -0.6, 0.5), blockFace);
						if(a == null) {
							p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "That orientation position is already taken! Try repositioning the item.");
							return;
						}
						a.setHelmet(new ItemStack(p.getInventory().getItemInMainHand()));
						a.setHeadPose(BlockTopPositioningCases.calcBlockArmorStandHeadPosSpecialCases2(p.getEyeLocation()));
						manager.addNew(p.getUniqueId(), clickedBlock.getLocation(), a.getLocation(), blockFace);
					}else {
						ArmorStand a = createArmorStand(clickedBlock.getLocation().add(0.5, -0.35, 0.5), blockFace);
						if(a == null) {
							p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "That orientation position is already taken! Try repositioning the item.");
							return;
						}
						a.setHelmet(new ItemStack(p.getInventory().getItemInMainHand()));
						a.setHeadPose(PlaceItemsUtils.calcBlockArmorStandHeadPos(p.getEyeLocation()));
						manager.addNew(p.getUniqueId(), clickedBlock.getLocation(), a.getLocation(), blockFace);
					}
				}else if(versionHandler.isItemey((p.getInventory().getItemInMainHand()))){
					if(PlaceItemsSpecialCases.isSpecialCases1(p.getInventory().getItemInMainHand().getType())) {
						ArmorStand a = createArmorStand(BlockTopPositioningCases.getBestArmorStandItemRelitiveToLocationSpecialCases1(PlaceItemsUtils.getCardinalDirection(p.getLocation()), clickedBlock.getLocation()), blockFace);
						if(a == null) {
							p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "That orientation position is already taken! Try repositioning the item.");
							return;
						}
						a.setHelmet(new ItemStack(p.getInventory().getItemInMainHand()));
						a.setHeadPose(PlaceItemsSpecialCases.calcItemArmorStandHeadPosSpecialCases1(p.getEyeLocation()));
						manager.addNew(p.getUniqueId(), clickedBlock.getLocation(), a.getLocation(), blockFace);
					}else {
						ArmorStand a = createArmorStand(BlockTopPositioningCases.getBestArmorStandItemRelitiveToLocation(PlaceItemsUtils.getCardinalDirection(p.getLocation()), clickedBlock.getLocation()), blockFace);
						if(a == null) {
							p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "That orientation position is already taken! Try repositioning the item.");
							return;
						}
						a.setHelmet(new ItemStack(p.getInventory().getItemInMainHand()));
						a.setHeadPose(PlaceItemsUtils.calcItemArmorStandHeadPos(p.getEyeLocation()));
						manager.addNew(p.getUniqueId(), clickedBlock.getLocation(), a.getLocation(), blockFace);
					}
				}else {
					return;
				}	
			}else if(blockFace == BlockFace.DOWN) {
				Location check = clickedBlock.getLocation().add(0, -1, 0);
				if(!PlaceItemsUtils.isPlaceIn(check.getBlock().getType())) {
					p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You cannot place an item in " + ChatColor.YELLOW + check.getBlock().getType() + ChatColor.RED + "!");
					return;
				}
				if(isBlockey(p.getInventory().getItemInMainHand())){
					ArmorStand a = createArmorStand(clickedBlock.getLocation().add(0.5, -2.0, 0.5), blockFace);
					if(a == null) {
						p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "That orientation position is already taken! Try repositioning the item.");
						return;
					}
					a.setHelmet(new ItemStack(p.getInventory().getItemInMainHand()));
					a.setHeadPose(PlaceItemsUtils.calcBlockArmorStandHeadPos(p.getEyeLocation()));
					manager.addNew(p.getUniqueId(), clickedBlock.getLocation(), a.getLocation(), blockFace);	
				}else if(versionHandler.isItemey((p.getInventory().getItemInMainHand()))){
					if(PlaceItemsSpecialCases.isSpecialCases1(p.getInventory().getItemInMainHand().getType())) {
						ArmorStand a = createArmorStand(BlockBottomPositioningCases.getBestArmorStandItemRelitiveToLocationSpecialCases1(PlaceItemsUtils.getCardinalDirection(p.getLocation()), clickedBlock.getLocation()), blockFace);
						if(a == null) {
							p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "That orientation position is already taken! Try repositioning the item.");
							return;
						}
						a.setHelmet(new ItemStack(p.getInventory().getItemInMainHand()));
						a.setHeadPose(PlaceItemsSpecialCases.calcItemArmorStandHeadPosSpecialCases1(p.getEyeLocation()));
						manager.addNew(p.getUniqueId(), clickedBlock.getLocation(), a.getLocation(), blockFace);
					}else {
						ArmorStand a = createArmorStand(BlockBottomPositioningCases.getBestArmorStandItemRelitiveToLocation(PlaceItemsUtils.getCardinalDirection(p.getLocation()), clickedBlock.getLocation()), blockFace);
						if(a == null) {
							p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "That orientation position is already taken! Try repositioning the item.");
							return;
						}
						a.setHelmet(new ItemStack(p.getInventory().getItemInMainHand()));
						a.setHeadPose(PlaceItemsUtils.calcItemArmorStandHeadPos(p.getEyeLocation()));
						manager.addNew(p.getUniqueId(), clickedBlock.getLocation(), a.getLocation(), blockFace);
					}
				}else {
					return;
				}	
			}else if(blockFace == BlockFace.NORTH) {
				Location check = clickedBlock.getLocation().add(0, 0, -1);
				if(!PlaceItemsUtils.isPlaceIn(check.getBlock().getType())) {
					p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You cannot place an item in " + ChatColor.YELLOW + check.getBlock().getType() + ChatColor.RED + "!");
					return;
				}
				if(isBlockey(p.getInventory().getItemInMainHand())){
					ArmorStand a = createArmorStand(BlockSidePositioningCases.getBestArmorStandRelitiveToRotationNumberBlock(blockFace, manager.getSideRotation(p), clickedBlock.getLocation()), blockFace);
					if(a == null) {
						p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "That orientation position is already taken! Try repositioning the item.");
						return;
					}
					a.setHelmet(new ItemStack(p.getInventory().getItemInMainHand()));
					a.setHeadPose(BlockSidePositioningCases.getBestArmorHeadPosRelitiveToRotationNumber(blockFace, manager.getSideRotation(p), clickedBlock.getLocation()));
					manager.addNew(p.getUniqueId(), clickedBlock.getLocation(), a.getLocation(), blockFace);	
				}else if(versionHandler.isItemey((p.getInventory().getItemInMainHand()))){
					ArmorStand a = createArmorStand(BlockSidePositioningCases.getBestArmorStandRelitiveToRotationNumberItem(blockFace, manager.getSideRotation(p), clickedBlock.getLocation()), blockFace);
					if(a == null) {
						p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "That orientation position is already taken! Try repositioning the item.");
						return;
					}
					a.setHelmet(new ItemStack(p.getInventory().getItemInMainHand()));
					a.setHeadPose(BlockSidePositioningCases.getBestArmorHeadPosRelitiveToRotationNumber(blockFace, manager.getSideRotation(p), clickedBlock.getLocation()));
					manager.addNew(p.getUniqueId(), clickedBlock.getLocation(), a.getLocation(), blockFace);
				}else {
					return;
				}	
			}else if(blockFace == BlockFace.SOUTH) {
				Location check = clickedBlock.getLocation().add(0, 0, 1);
				if(!PlaceItemsUtils.isPlaceIn(check.getBlock().getType())) {
					p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You cannot place an item in " + ChatColor.YELLOW + check.getBlock().getType() + ChatColor.RED + "!");
					return;
				}
				if(isBlockey(p.getInventory().getItemInMainHand())){
					ArmorStand a = createArmorStand(BlockSidePositioningCases.getBestArmorStandRelitiveToRotationNumberBlock(blockFace, manager.getSideRotation(p), clickedBlock.getLocation()), blockFace);
					if(a == null) {
						p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "That orientation position is already taken! Try repositioning the item.");
						return;
					}
					a.setHeadPose(BlockSidePositioningCases.getBestArmorHeadPosRelitiveToRotationNumber(blockFace, manager.getSideRotation(p), clickedBlock.getLocation()));
					a.setHelmet(new ItemStack(p.getInventory().getItemInMainHand()));
					manager.addNew(p.getUniqueId(), clickedBlock.getLocation(), a.getLocation(), blockFace);	
				}else if(versionHandler.isItemey((p.getInventory().getItemInMainHand()))){
					ArmorStand a = createArmorStand(BlockSidePositioningCases.getBestArmorStandRelitiveToRotationNumberItem(blockFace, manager.getSideRotation(p), clickedBlock.getLocation()), blockFace);
					if(a == null) {
						p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "That orientation position is already taken! Try repositioning the item.");
						return;
					}
					a.setHeadPose(BlockSidePositioningCases.getBestArmorHeadPosRelitiveToRotationNumber(blockFace, manager.getSideRotation(p), clickedBlock.getLocation()));
					a.setHelmet(new ItemStack(p.getInventory().getItemInMainHand()));
					manager.addNew(p.getUniqueId(), clickedBlock.getLocation(), a.getLocation(), blockFace);
				}else {
					return;
				}	
			}else if(blockFace == BlockFace.WEST) {
				Location check = clickedBlock.getLocation().add(-1, 0, 0);
				if(!PlaceItemsUtils.isPlaceIn(check.getBlock().getType())) {
					p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You cannot place an item in " + ChatColor.YELLOW + check.getBlock().getType() + ChatColor.RED + "!");
					return;
				}
				if(isBlockey(p.getInventory().getItemInMainHand())){
					ArmorStand a = createArmorStand(BlockSidePositioningCases.getBestArmorStandRelitiveToRotationNumberBlock(blockFace, manager.getSideRotation(p), clickedBlock.getLocation()), blockFace);
					if(a == null) {
						p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "That orientation position is already taken! Try repositioning the item.");
						return;
					}
					a.setHelmet(new ItemStack(p.getInventory().getItemInMainHand()));
					a.setHeadPose(BlockSidePositioningCases.getBestArmorHeadPosRelitiveToRotationNumber(blockFace, manager.getSideRotation(p), clickedBlock.getLocation()));
					manager.addNew(p.getUniqueId(), clickedBlock.getLocation(), a.getLocation(), blockFace);	
				}else if(versionHandler.isItemey((p.getInventory().getItemInMainHand()))){
					ArmorStand a = createArmorStand(BlockSidePositioningCases.getBestArmorStandRelitiveToRotationNumberItem(blockFace, manager.getSideRotation(p), clickedBlock.getLocation()), blockFace);
					if(a == null) {
						p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "That orientation position is already taken! Try repositioning the item.");
						return;
					}
					a.setHelmet(new ItemStack(p.getInventory().getItemInMainHand()));
					a.setHeadPose(BlockSidePositioningCases.getBestArmorHeadPosRelitiveToRotationNumber(blockFace, manager.getSideRotation(p), clickedBlock.getLocation()));
					manager.addNew(p.getUniqueId(), clickedBlock.getLocation(), a.getLocation(), blockFace);
				}else {
					return;
				}	
			}else if(blockFace == BlockFace.EAST) {
				Location check = clickedBlock.getLocation().add(1, 0, 0);
				if(!PlaceItemsUtils.isPlaceIn(check.getBlock().getType())) {
					p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You cannot place an item in " + ChatColor.YELLOW + check.getBlock().getType() + ChatColor.RED + "!");
					return;
				}
				if(isBlockey(p.getInventory().getItemInMainHand())){
					ArmorStand a = createArmorStand(BlockSidePositioningCases.getBestArmorStandRelitiveToRotationNumberBlock(blockFace, manager.getSideRotation(p), clickedBlock.getLocation()), blockFace);
					if(a == null) {
						p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "That orientation position is already taken! Try repositioning the item.");
						return;
					}
					a.setHelmet(new ItemStack(p.getInventory().getItemInMainHand()));
					a.setHeadPose(BlockSidePositioningCases.getBestArmorHeadPosRelitiveToRotationNumber(blockFace, manager.getSideRotation(p), clickedBlock.getLocation()));
					manager.addNew(p.getUniqueId(), clickedBlock.getLocation(), a.getLocation(), blockFace);	
				}else if(versionHandler.isItemey((p.getInventory().getItemInMainHand()))){
					ArmorStand a = createArmorStand(BlockSidePositioningCases.getBestArmorStandRelitiveToRotationNumberItem(blockFace, manager.getSideRotation(p), clickedBlock.getLocation()), blockFace);
					if(a == null) {
						p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "That orientation position is already taken! Try repositioning the item.");
						return;
					}
					a.setHelmet(new ItemStack(p.getInventory().getItemInMainHand()));
					a.setHeadPose(BlockSidePositioningCases.getBestArmorHeadPosRelitiveToRotationNumber(blockFace, manager.getSideRotation(p), clickedBlock.getLocation()));
					manager.addNew(p.getUniqueId(), clickedBlock.getLocation(), a.getLocation(), blockFace);
				}else {
					return;
				}	
			}
			
			
			if(p.getInventory().getItemInMainHand().getAmount() == 1) {
				p.getInventory().setItemInMainHand(null);
			}else {
				p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
			}
			updateArea(clickedBlock.getLocation().clone().add(0.0, 2.0, 0.0));
			manager.setPlacements(p, manager.getPlacements(p) + 1);
			p.updateInventory();	
		
		}
	
	private ArmorStand createArmorStand(Location location, BlockFace blockFace) {
		Collection<Entity> near = location.getWorld().getNearbyEntities(location, 0.001, 0.001, 0.001);
		
		if(!near.isEmpty()) {
			for(Entity entity : near){
				if(entity instanceof ArmorStand){
					if(location.getX() == entity.getLocation().getX() && location.getY() == entity.getLocation().getY() && location.getZ() == entity.getLocation().getZ()) {
						return null;
					}
				}
			}
		}
		
		if(blockFace == BlockFace.WEST || blockFace == BlockFace.EAST) {
			location.setYaw(90f);
		}
		
		
		ArmorStand a = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
		a.setVisible(false);
		a.setGravity(false);
		a.setBasePlate(false);
		a.setInvulnerable(true);
		return a;
	}
	
	private boolean handlePermissionCheck(Player p) {
		if(PlaceItemsConfig.getBlacklistedWorlds().contains(p.getWorld().getName()) && !p.hasPermission("placeitems.worldblacklistbypass")) {
			p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You cannot place items in this world!");
			return false;
		}
		for(PermissionAttachmentInfo attachmentInfo : p.getEffectivePermissions()) {
		      if(attachmentInfo.getPermission().startsWith("placeitems.cap.")) {
		    	  String permission = attachmentInfo.getPermission();
		    	  String value = permission.substring(permission.lastIndexOf(".") + 1);
		    	  int maxPlacements = 0;
		    	  
		    	  if(!value.equalsIgnoreCase("" + PlaceItemsManager.UNLIMITED_CHAR_1)) {
		    		  try {
		    			  maxPlacements = Integer.parseInt(value);
		    		  }catch(Exception e) {
		    			  p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You have an error with your permission, using default settings!");
		    			  break;
		    		  }
		    		if(maxPlacements != PlaceItemsManager.UNLIMITED && maxPlacements <= manager.getPlacements(p)){
		  				p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You have reached your placement cap!");
		  				return false;
		  			}else {
		  				return true;
		  			}
		    	  }else {
		    		  return true;
		    	  }
		      }
		   }
		
		if(manager.getHasCustomPlaceCap(p)) {
			if(manager.getMaxPlacements(p) != PlaceItemsManager.UNLIMITED && manager.getMaxPlacements(p) <= manager.getPlacements(p)){
				p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You have reached your placement cap!");
				return false;
			}
		}else {
			if(PlaceItemsConfig.getDefaultPlaceCap() != PlaceItemsManager.UNLIMITED) {
				if(PlaceItemsConfig.getDefaultPlaceCap() <= manager.getPlacements(p)){
					p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You have reached your placement cap!");
					return false;
				}
			}
		}
		return true;
	}
}
