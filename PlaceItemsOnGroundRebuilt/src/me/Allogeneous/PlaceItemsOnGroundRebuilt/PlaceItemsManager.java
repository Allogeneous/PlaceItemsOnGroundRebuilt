package me.Allogeneous.PlaceItemsOnGroundRebuilt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PlaceItemsManager {
	
	public static final int UNLIMITED = -1;
	public static final char UNLIMITED_CHAR_1 = 'u';
	public static final char UNLIMITED_CHAR_2 = 'U';
	
	private JavaPlugin plugin;
	private ArrayDeque<PlaceItemsLinkedLocation> placedItemLinkedLocations;
	
	public PlaceItemsManager(JavaPlugin plugin){
		placedItemLinkedLocations = new ArrayDeque<>();
		this.plugin = plugin;
		loadInitialData();
	}
	
	/*
	 * Loads and creates all core plug-in data files and folders for players and location data.
	 */
	
	@SuppressWarnings("unchecked")
	private final void loadInitialData(){
		if(!PlaceItemsUtils.makeFolder(plugin.getDataFolder())){
			plugin.getLogger().warning("Unable to make core plugin folder!");
		}
		if(!PlaceItemsUtils.makeFolder(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "data"))){
			plugin.getLogger().warning("Unable to make plugin data folder!");
		}
		if(!PlaceItemsUtils.makeFolder(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "data"+ File.separator +"players"))){
			plugin.getLogger().warning("Unable to make plugin players folder!");
		}else{
			for(Player p : Bukkit.getServer().getOnlinePlayers()){
				makeNewPlayerFile(p);
			}
		}
		
		LinkedList<String> serializableLinkedLocations;
		
		if(!PlaceItemsUtils.createFile(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "data", "Locations.dat"))){
			plugin.getLogger().warning("Unable to make plugin locations folder!");
		}else{
			try{
				serializableLinkedLocations = (LinkedList<String>) PlaceItemsUtils.loadFile(new File(plugin.getDataFolder().getAbsolutePath() + "\\data", "Locations.dat"));
				unserializeLocationData(serializableLinkedLocations);
			}catch(Exception e){
				serializableLinkedLocations = new LinkedList<>();
				unserializeLocationData(serializableLinkedLocations);
				plugin.getLogger().info("Location data not found... creating new data");
			}
		}
	}
	
	/*
	 * Converts String version of LinkedLocation back to a LinkedLocation and stores it back in the location managers list of locations
	 */
	
	private final void unserializeLocationData(LinkedList<String> data){
		for(String string : data){
			try {
				this.placedItemLinkedLocations.add(PlaceItemsLinkedLocation.fromString(string));
			} catch (PlaceItemsInvalidLinkedLocationException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Converts LinkedLocation to a String and stores it in the Locations.dat file
	 */
	
	public final void saveLocationData(){
		LinkedList<String> serializableLinkedLocations = new LinkedList<>();
		for(PlaceItemsLinkedLocation pill : this.placedItemLinkedLocations){
			serializableLinkedLocations.add(pill.toString());
		}
	
		PlaceItemsUtils.saveFile(serializableLinkedLocations, new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "data", "Locations.dat"));
	}
		
	/*
	 * Creates a new player yml file for a player when they join for the first time
	 */
	
	public void makeNewPlayerFile(Player p){
		
			File data = rawFile(p);
			if(!data.exists()){
				if(PlaceItemsUtils.createFile(data)){
					FileConfiguration newPlayerData = YamlConfiguration.loadConfiguration(data);
					newPlayerData.addDefault("uuid", p.getUniqueId().toString());
					newPlayerData.addDefault("name", p.getName());
					newPlayerData.addDefault("placeCap", 0);
					newPlayerData.addDefault("hasCustomPlaceCap", false);
					newPlayerData.addDefault("ammountPlaced", 0);
					newPlayerData.addDefault("placeToggled", false);
					newPlayerData.options().copyDefaults(true);
					try {
						newPlayerData.save(data);
					} catch (IOException e) {
						plugin.getLogger().info("Unable to save player config file for UUID: " + p.getUniqueId());
						e.printStackTrace();
					}
				
			}
		}
	}
	
	public void updateUsername(Player p) {
		FileConfiguration file = getDataFile(p);
		file.set("name", p.getName());
	}
	
	public boolean confirmFileExistance(UUID uuid){
		File data = rawFile(uuid);
		return data.exists();
		
	}
	
	public File rawFile(Player p){
		return new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "data" + File.separator + "players", p.getUniqueId().toString() + ".yml");
	}
	
	public File rawFile(UUID uuid){
		return new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "data" + File.separator + "players", uuid.toString() + ".yml");
	}
	
	public FileConfiguration getDataFile(Player p){
		File data = rawFile(p.getUniqueId());
		return YamlConfiguration.loadConfiguration(data);
	}
	
	public FileConfiguration getDataFile(UUID uuid){
		File data = rawFile(uuid);
		return YamlConfiguration.loadConfiguration(data);
	}
	
	public void setUUID(Player p){
		FileConfiguration file = getDataFile(p);
		file.set("uuid", p.getUniqueId().toString());
		try {
			file.save(rawFile(p));
		} catch (IOException e) {
			plugin.getLogger().info("Unable to save player config file for UUID: " + p.getUniqueId());
			e.printStackTrace();
		}
	}
	
	public void setUUID(UUID uuid){
		FileConfiguration file = getDataFile(uuid);
		file.set("uuid", uuid.toString());
		try {
			file.save(rawFile(uuid));
		} catch (IOException e) {
			plugin.getLogger().info("Unable to save player config file for UUID: " + uuid);
			e.printStackTrace();
		}
	}
	
	public UUID getUUID(Player p){
		FileConfiguration file = getDataFile(p);
		return UUID.fromString(file.getString("uuid", p.getUniqueId().toString()));
	}
	
	public UUID getUUID(UUID uuid){
		FileConfiguration file = getDataFile(uuid);
		return UUID.fromString(file.getString("uuid", ""));
	}
	
	public void setName(Player p){
		FileConfiguration file = getDataFile(p);
		file.set("name", p.getName());
		try {
			file.save(rawFile(p));
		} catch (IOException e) {
			plugin.getLogger().info("Unable to save player config file for UUID: " + p.getUniqueId());
			e.printStackTrace();
		}
	}
	
	public void setName(UUID uuid, String name){
		FileConfiguration file = getDataFile(uuid);
		file.set("name", name);
		try {
			file.save(rawFile(uuid));
		} catch (IOException e) {
			plugin.getLogger().info("Unable to save player config file for UUID: " + uuid);
			e.printStackTrace();
		}
	}
	
	public String getName(Player p){
		FileConfiguration file = getDataFile(p);
		return file.getString("name", p.getName());
	}
	
	public String getName(UUID uuid){
		FileConfiguration file = getDataFile(uuid);
		return file.getString("name", "");
	}
	
	public void setPlacements(Player p, int amount){
		if(amount < 0){
			amount = 0;
		}
		FileConfiguration file = getDataFile(p);
		file.set("ammountPlaced", amount);
		try {
			file.save(rawFile(p));
		} catch (IOException e) {
			plugin.getLogger().info("Unable to save player config file for UUID: " + p.getUniqueId());
			e.printStackTrace();
		}
	}
	
	public void setPlacements(UUID uuid, int amount){
		if(amount < 0){
			amount = 0;
		}
			FileConfiguration file = getDataFile(uuid);
			file.set("ammountPlaced", amount);
			try {
				file.save(rawFile(uuid));
			} catch (IOException e) {
				plugin.getLogger().info("Unable to save player config file for UUID: " + uuid);
				e.printStackTrace();
			}
	}
	
	public int getPlacements(Player p){
		FileConfiguration file = getDataFile(p);
		return file.getInt("ammountPlaced", 0);
	}
	
	public int getPlacements(UUID uuid){
		FileConfiguration file = getDataFile(uuid);
		return file.getInt("ammountPlaced", 0);
	}
	
	public void setMaxPlacements(Player p, int amount){
		
			FileConfiguration file = getDataFile(p);
			file.set("placeCap", amount);
			try {
				file.save(rawFile(p));
			} catch (IOException e) {
				plugin.getLogger().info("Unable to save player config file for UUID: " + p.getUniqueId().toString());
				e.printStackTrace();
			}
		
	}
	
	public void setMaxPlacements(UUID uuid, int amount){
		
			FileConfiguration file = getDataFile(uuid);
			file.set("placeCap", amount);
			try {
				file.save(rawFile(uuid));
			} catch (IOException e) {
				plugin.getLogger().info("Unable to save player config file for UUID: " + uuid.toString());
				e.printStackTrace();
			}
		
	}
	
	public void setHasCustomPlaceCap(UUID uuid, boolean hasCap){
		
			FileConfiguration file = getDataFile(uuid);
			file.set("hasCustomPlaceCap", hasCap);
			try {
				file.save(rawFile(uuid));
			} catch (IOException e) {
				plugin.getLogger().info("Unable to save player config file for UUID: " + uuid.toString());
				e.printStackTrace();
			}
		
	}
	
	public void setHasCustomPlaceCap(Player p, boolean hasCap){
		
			FileConfiguration file = getDataFile(p);
			file.set("hasCustomPlaceCap", hasCap);
			try {
				file.save(rawFile(p));
			} catch (IOException e) {
				plugin.getLogger().info("Unable to save player config file for UUID: " + p.getUniqueId().toString());
				e.printStackTrace();
			}
		
	}
	
	public boolean getHasCustomPlaceCap(Player p){
		FileConfiguration file = getDataFile(p);
		return file.getBoolean("hasCustomPlaceCap", false);
	}
	
	public boolean getHasCustomPlaceCap(UUID uuid){
		FileConfiguration file = getDataFile(uuid);
		return file.getBoolean("hasCustomPlaceCap", false);
	}
	
	public int getMaxPlacements(Player p){
		if(getHasCustomPlaceCap(p)) {
			FileConfiguration file = getDataFile(p);
			return file.getInt("placeCap", 0);
		}else {
			return PlaceItemsConfig.getDefaultPlaceCap();
		}
	}
	
	public int getMaxPlacements(UUID uuid){
		if(getHasCustomPlaceCap(uuid)) {
			FileConfiguration file = getDataFile(uuid);
			return file.getInt("placeCap", 0);
		}else {
			return PlaceItemsConfig.getDefaultPlaceCap();
		}
	}
	
	public void setPlaceToggled(Player p, boolean on){
		FileConfiguration file = getDataFile(p);
		file.set("placeToggled", on);
		try {
			file.save(rawFile(p));
		} catch (IOException e) {
			plugin.getLogger().info("Unable to save player config file for UUID: " + p.getUniqueId().toString());
			e.printStackTrace();
		}
	}
	
	public void setPlaceToggled(UUID uuid, boolean on){
		FileConfiguration file = getDataFile(uuid);
		file.set("placeToggled", on);
		try {
			file.save(rawFile(uuid));
		} catch (IOException e) {
			plugin.getLogger().info("Unable to save player config file for UUID: " + uuid.toString());
			e.printStackTrace();
		}
	}
	
	public boolean getPlaceToggled(Player p){
		FileConfiguration file = getDataFile(p);
		return file.getBoolean("placeToggled", false);
	}
	
	public boolean getPlaceToggled(UUID uuid){
		FileConfiguration file = getDataFile(uuid);
		return file.getBoolean("placeToggled", false);
	}
	
	public ArrayDeque<PlaceItemsLinkedLocation> getPlacedItemLinkedLocations() {
		return placedItemLinkedLocations;
	}

	public void setPlacedItemLinkedLocations(ArrayDeque<PlaceItemsLinkedLocation> placedItemLinkedLocations) {
		this.placedItemLinkedLocations = placedItemLinkedLocations;
	}
	
	public void removeAll(UUID uuid){
		Iterator<PlaceItemsLinkedLocation> pilli = this.placedItemLinkedLocations.iterator();
		while(pilli.hasNext()){
			PlaceItemsLinkedLocation pill = pilli.next();
			if(pill.getPlacer().equals(uuid)){
				pilli.remove();
			}
		}
	}
	
	public void removePhysical(Location physical){
		Iterator<PlaceItemsLinkedLocation> pilli = this.placedItemLinkedLocations.iterator();
		while(pilli.hasNext()){
			PlaceItemsLinkedLocation pill = pilli.next();
			if(pill.getPhysicalLoc().getWorld().getName().equals(physical.getWorld().getName()) && pill.getPhysicalLoc().getX() == physical.getX() && pill.getPhysicalLoc().getY() == physical.getY() && pill.getPhysicalLoc().getZ() == physical.getZ()){
				pilli.remove();
				return;
			}
		}
	}
	
	public void removeProp(Location prop){
		Iterator<PlaceItemsLinkedLocation> pilli = this.placedItemLinkedLocations.iterator();
		while(pilli.hasNext()){
			PlaceItemsLinkedLocation pill = pilli.next();
			if(pill.getPropLoc().getWorld().getName().equals(prop.getWorld().getName()) && pill.getPropLoc().getX() == prop.getX() && pill.getPropLoc().getY() == prop.getY() && pill.getPropLoc().getZ() == prop.getZ()){
				pilli.remove();
				return;
			}
		}
	}
	
	public boolean containsPlacer(UUID uuid){
		for(PlaceItemsLinkedLocation pill : this.placedItemLinkedLocations){
			if(pill.getPlacer().equals(uuid)){
				return true;
			}
		}
		return false;
	}
	
	public boolean containsPhysical(Location physical){
		for(PlaceItemsLinkedLocation pill : this.placedItemLinkedLocations){
			if(pill.getPhysicalLoc().getWorld().getName().equals(physical.getWorld().getName()) && pill.getPhysicalLoc().getX() == physical.getX() && pill.getPhysicalLoc().getY() == physical.getY() && pill.getPhysicalLoc().getZ() == physical.getZ()){
				return true;
			}
		}
		return false;
		
	}
	
	public boolean containsProp(Location prop){
		for(PlaceItemsLinkedLocation pill : this.placedItemLinkedLocations){
			if(pill.getPropLoc().getWorld().getName().equals(prop.getWorld().getName()) && pill.getPropLoc().getX() == prop.getX() && pill.getPropLoc().getY() == prop.getY() && pill.getPropLoc().getZ() == prop.getZ()){
				return true;
			}
		}
		return false;
	}
	
	public PlaceItemsLinkedLocation getFromPhysical(Location physical){
		for(PlaceItemsLinkedLocation pill : this.placedItemLinkedLocations){
			if(pill.getPhysicalLoc().getWorld().getName().equals(physical.getWorld().getName()) && pill.getPhysicalLoc().getX() == physical.getX() && pill.getPhysicalLoc().getY() == physical.getY() && pill.getPhysicalLoc().getZ() == physical.getZ()){
				return pill;
			}
		}
		return null;
		
	}
	
	public PlaceItemsLinkedLocation getFromProp(Location prop){
		for(PlaceItemsLinkedLocation pill : this.placedItemLinkedLocations){
			if(pill.getPropLoc().getWorld().getName().equals(prop.getWorld().getName()) && pill.getPropLoc().getX() == prop.getX() && pill.getPropLoc().getY() == prop.getY() && pill.getPropLoc().getZ() == prop.getZ()){
				return pill;
			}
		}
		return null;
	}

}
