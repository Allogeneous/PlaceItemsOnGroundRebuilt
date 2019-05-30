package me.Allogeneous.PlaceItemsOnGroundRebuilt;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class PlaceItemsEvents implements Listener{
	
	private PlaceItemsManager manager;
	private PlaceItemsVersionSensativeMethods versionHandler;
	
	public PlaceItemsEvents(PlaceItemsManager manager, PlaceItemsVersionSensativeMethods versionHandler){
		this.manager = manager;
		this.versionHandler = versionHandler;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		manager.makeNewPlayerFile(e.getPlayer());
		manager.checkPlayerCapOnJoin(e.getPlayer());
		manager.updateUsername(e.getPlayer());
	}

	@EventHandler
	public void onPlayerPlace(PlayerInteractEvent e){
		if(e.getHand() == EquipmentSlot.HAND) {
			Player p = e.getPlayer();
			
			if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR) {
				return;
			}
			
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK && isBlockey(p.getInventory().getItemInMainHand())) {
				if(manager.containsPhysical(e.getClickedBlock().getLocation())) {
					e.setCancelled(true);
					return;
				}
			}
			
			if(manager.getPlaceToggled(p) && p.isSneaking() && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getBlockFace() == BlockFace.UP){
				
				e.setCancelled(true);
				
				if(!p.hasPermission("placeitems.place")) {
					p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "You do not have permission to place items!");
					return;
				}
				if(manager.containsPhysical(e.getClickedBlock().getLocation())){
					p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You cannot place more than one item on a block!");
					return;
				}
				
				if(!isValidPlace(e.getClickedBlock().getType())) {
					p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You cannot place an item on that block!");
					return;
				}
				
				if(PlaceItemsUtils.isSlab(e.getClickedBlock().getType()) || PlaceItemsUtils.isStairs(e.getClickedBlock().getType())) {
					if(!versionHandler.isValidSlabOrStair(e.getClickedBlock())) {
						p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You cannot place an item on that block!");
						return;
					}
				}
				
				Location checkAbove = e.getClickedBlock().getLocation().add(0, 1, 0);
				if(!PlaceItemsUtils.isPlaceIn(checkAbove.getBlock().getType())) {
					p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You cannot place an item in " + ChatColor.YELLOW + checkAbove.getBlock().getType() + ChatColor.RED + "!");
					return;
				}
				
				if(manager.getHasCustomPlaceCap(p)) {
					if(manager.getMaxPlacements(p) != PlaceItemsManager.UNLIMITED && manager.getMaxPlacements(p) <= manager.getPlacements(p)){
						p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You have reached your placement cap!");
						return;
					}
				}else {
					if(PlaceItemsConfig.getDefaultPlaceCap() != PlaceItemsManager.UNLIMITED) {
						if(PlaceItemsConfig.getDefaultPlaceCap() <= manager.getPlacements(p)){
							p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You have reached your placement cap!");
							return;
						}
					}
				}
				
			}else{
				return;
			}

			if(isBlockey(p.getInventory().getItemInMainHand())){
				ArmorStand a = (ArmorStand) p.getWorld().spawnEntity(e.getClickedBlock().getLocation().add(0.5, -0.35, 0.5), EntityType.ARMOR_STAND);

				a.setVisible(false);
				a.setGravity(false);
				a.setBasePlate(false);
				a.setInvulnerable(true);
				a.setHelmet(new ItemStack(p.getInventory().getItemInMainHand()));
				a.setHeadPose(PlaceItemsUtils.calcBlockArmorStandHeadPos(p.getEyeLocation()));
				manager.getPlacedItemLinkedLocations().add(new PlaceItemsLinkedLocation(p.getUniqueId(), e.getClickedBlock().getLocation(), a.getLocation()));
				if(p.getInventory().getItemInMainHand().getAmount() == 1) {
					p.getInventory().setItemInMainHand(null);
				}else {
					p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
				}
				updateArea(e.getClickedBlock().getLocation().clone().add(0.0, 2.0, 0.0));
				manager.setPlacements(p, manager.getPlacements(p) + 1);
				p.updateInventory();
			}else if(versionHandler.isItemey((p.getInventory().getItemInMainHand()))){
				ArmorStand a = (ArmorStand) p.getWorld().spawnEntity(PlaceItemsUtils.getBestArmorStandItemRelitiveToLocation(PlaceItemsUtils.getCardinalDirection(p.getLocation()), e.getClickedBlock().getLocation()), EntityType.ARMOR_STAND);
				
				a.setVisible(false);
				a.setGravity(false);
				a.setBasePlate(false);
				a.setInvulnerable(true);
				a.setHelmet(new ItemStack(p.getInventory().getItemInMainHand()));
				a.setHeadPose(PlaceItemsUtils.calcItemArmorStandHeadPos(p.getEyeLocation()));
				manager.getPlacedItemLinkedLocations().add(new PlaceItemsLinkedLocation(p.getUniqueId(), e.getClickedBlock().getLocation(), a.getLocation()));
				p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
				if(p.getInventory().getItemInMainHand().getAmount() == 1) {
					p.getInventory().setItemInMainHand(null);
				}else {
					p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
				}
				updateArea(e.getClickedBlock().getLocation().clone().add(0.0, 2.0, 0.0));
				manager.setPlacements(p, manager.getPlacements(p) + 1);
				p.updateInventory();
			}else {
				p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.RED + "You cannot place " + ChatColor.YELLOW + p.getInventory().getItemInMainHand().getType() + ChatColor.RED + "!");
			}
		}
	}
	
	private void updateArea(Location location){
		if(location.getBlock().getType() == Material.AIR){
			location.getBlock().setType(Material.GLASS);
			location.getBlock().setType(Material.AIR);
		}
	
	}
	
	
	
	private boolean isValidPlace(Material type) {
		if(!PlaceItemsUtils.isBlackListedPlaceItem(type)) {
			if(type.isBlock() && type.isSolid()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isBlockey(ItemStack item){
		if(!PlaceItemsUtils.isBlacklisted(item.getType()) && !PlaceItemsUtils.isItemLikeBlock(item.getType())){
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
				if(manager.containsProp(a.getLocation())){
					e.setCancelled(true);
					Player p = e.getPlayer();
					if(!p.hasPermission("placeitems.take")) {
						p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "You do not have permission to take items!");
						return;
					}
					ItemStack hem = a.getHelmet();
					hem.setAmount(1);
					if(p.getInventory().firstEmpty() != -1){
						p.getInventory().addItem(hem);
					}else{
						p.getWorld().dropItemNaturally(e.getRightClicked().getLocation().clone().add(0, 1, 0), hem);
					}
					manager.setPlacements(manager.getFromProp(a.getLocation()).getPlacer(), manager.getPlacements(manager.getFromProp(a.getLocation()).getPlacer()) - 1);
					manager.removeProp(a.getLocation());
					a.remove();
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerTakeBreak(BlockBreakEvent e){
		if(e.isCancelled()) {
			return;
		}
		if(manager.containsPhysical(e.getBlock().getLocation())){
			Player p = e.getPlayer();
			if(!p.hasPermission("placeitems.take")) {
				p.sendMessage(ChatColor.BLUE + "[PlaceItems] " + ChatColor.DARK_RED + "You do not have permission to take items!");
				return;
			}
			Location look = manager.getFromPhysical(e.getBlock().getLocation()).getPropLoc();
			Collection<Entity> armorstand = look.getWorld().getNearbyEntities(look, 0.01, 0.01, 0.01);
			
			ArmorStand a = null;
			
			for(Entity entity : armorstand){
				if(entity instanceof ArmorStand){
					a = (ArmorStand) entity;
					break;
				}
			}
			
			if(a != null){
				if(!a.isVisible() && !a.hasBasePlate() && !a.hasGravity() && a.isInvulnerable()){
					if(manager.containsProp(a.getLocation())){
						ItemStack hem = a.getHelmet();
						hem.setAmount(1);
						p.getWorld().dropItemNaturally(e.getBlock().getLocation(), hem);
						manager.setPlacements(manager.getFromProp(a.getLocation()).getPlacer(), manager.getPlacements(manager.getFromProp(a.getLocation()).getPlacer()) - 1);
						manager.removeProp(a.getLocation());
						a.remove();
					}
				}
			}
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
}
