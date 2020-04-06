package me.Allogeneous.PlaceItemsOnGroundRebuilt.Files;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.Allogeneous.PlaceItemsOnGroundRebuilt.PlaceItemsConfig;
import me.Allogeneous.PlaceItemsOnGroundRebuilt.PlaceItemsUtils;

public class PlaceItemsManager {
	
	public static final int UNLIMITED = -1;
	public static final char UNLIMITED_CHAR_1 = 'u';
	public static final char UNLIMITED_CHAR_2 = 'U';
	
	private JavaPlugin plugin;
	private TreeMap<PlaceItemsPhysicalLocation, AdvancedPlaceItemsLinkedLocation> placedItemLinkedLocations;
	private HashMap<UUID, Integer> playerRotationPositions;
	
	public PlaceItemsManager(JavaPlugin plugin){
		placedItemLinkedLocations = new TreeMap<PlaceItemsPhysicalLocation, AdvancedPlaceItemsLinkedLocation>();
		playerRotationPositions = new HashMap<UUID, Integer>();
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
		
		
		
		if(!PlaceItemsUtils.createFile(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "data", "Locations.dat"))){
			plugin.getLogger().warning("Unable to make plugin locations folder!");
		}else{
			try{
				if(PlaceItemsUtils.loadFile(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "data", "Locations.dat")) instanceof TreeMap<?, ?>) {
					TreeMap<String, String> serializableLinkedLocations;
					serializableLinkedLocations = (TreeMap<String, String>) PlaceItemsUtils.loadFile(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "data", "Locations.dat"));
					unserializeLocationData(serializableLinkedLocations);
				}else if(PlaceItemsUtils.loadFile(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "data", "Locations.dat")) instanceof LinkedList<?>) {
					LinkedList<String> serializableLinkedLocations;
					serializableLinkedLocations = (LinkedList<String>) PlaceItemsUtils.loadFile(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "data", "Locations.dat"));
					unserializeLocationDataOld(serializableLinkedLocations);
				}
			}catch(Exception e){
				TreeMap<String, String> serializableLinkedLocations;
				serializableLinkedLocations = (TreeMap<String, String>) PlaceItemsUtils.loadFile(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "data", "Locations.dat"));
				unserializeLocationData(serializableLinkedLocations);
				plugin.getLogger().info("Location data not found... creating new data");
			}
		}
	}
	
	/*
	 * Converts String version of LinkedLocation back to a LinkedLocation and stores it back in the location managers list of locations
	 */
	
	private final void unserializeLocationDataOld(LinkedList<String> data){
		for(String string : data){
			try {
				Entry<PlaceItemsPhysicalLocation, AdvancedPlaceItemsLinkedLocation> entry = AdvancedPlaceItemsLinkedLocation.fromStringOld(string);
				if(entry != null) {
					this.placedItemLinkedLocations.put(entry.getKey(), entry.getValue());
				}
			} catch (PlaceItemsInvalidLinkedLocationException e) {
				e.printStackTrace();
			}
		}
	}
	
	private final void unserializeLocationData(TreeMap<String, String> data){
		for(Entry<String, String> stringData : data.entrySet()){
			try {
				Entry<PlaceItemsPhysicalLocation, AdvancedPlaceItemsLinkedLocation> entry = AdvancedPlaceItemsLinkedLocation.fromString(stringData.getKey(), stringData.getValue());
				if(entry != null) {
					this.placedItemLinkedLocations.put(entry.getKey(), entry.getValue());
				}
			} catch (PlaceItemsInvalidLinkedLocationException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/*
	 * Converts LinkedLocation to a String and stores it in the Locations.dat file
	 */
	
	public final void saveLocationData(){
		TreeMap<String, String> serializableLinkedLocations = new TreeMap<String, String>();
		for(Entry<PlaceItemsPhysicalLocation, AdvancedPlaceItemsLinkedLocation> entry : this.placedItemLinkedLocations.entrySet()){
			serializableLinkedLocations.put(entry.getKey().getWorld() + "," + entry.getKey().getX() + "," + entry.getKey().getY() + "," + entry.getKey().getZ(), entry.getValue().toString());
		}
	
		PlaceItemsUtils.saveFile(serializableLinkedLocations, new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "data", "Locations.dat"));
	}
	
	/*public void debug(){
		System.out.println(this.placedItemLinkedLocations.entrySet().size());
		for(Entry<PlaceItemsPhysicalLocation, AdvancedPlaceItemsLinkedLocation> entry : this.placedItemLinkedLocations.entrySet()){
			System.out.println(entry.getKey().getWorld() + "," + entry.getKey().getX() + "," + entry.getKey().getY() + "," + entry.getKey().getZ());
		}
	}*/
		
	/*
	 * Creates a new player yml file for a player when they join for the first time
	 */
	
	public int getPlacementLocationsAmount() {
		return placedItemLinkedLocations.size();
	}
	
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
				newPlayerData.addDefault("rightClickPickupToggled", true);
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
	
	public int getRawMaxPlacements(Player p){
		FileConfiguration file = getDataFile(p);
		return file.getInt("placeCap", 0);
	}
	
	public int getRawMaxPlacements(UUID uuid){
		FileConfiguration file = getDataFile(uuid);
		return file.getInt("placeCap", 0);
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
	
	public void setRightClickPickupToggled(Player p, boolean on){
		FileConfiguration file = getDataFile(p);
		if(file.contains("rightClickPickupToggled")) {
			file.set("rightClickPickupToggled", on);
		}else {
			file.addDefault("rightClickPickupToggled", true);
			file.set("rightClickPickupToggled", on);
		}
		try {
			file.options().copyDefaults(true);
			file.save(rawFile(p));
		} catch (IOException e) {
			plugin.getLogger().info("Unable to save player config file for UUID: " + p.getUniqueId().toString());
			e.printStackTrace();
		}
	}
	
	public void setRightClickPickupToggled(UUID uuid, boolean on){
		FileConfiguration file = getDataFile(uuid);
		if(file.contains("rightClickPickupToggled")) {
			file.set("rightClickPickupToggled", true);
		}else {
			file.addDefault("rightClickPickupToggled", on);
			file.set("rightClickPickupToggled", on);
		}
		try {
			file.save(rawFile(uuid));
		} catch (IOException e) {
			plugin.getLogger().info("Unable to save player config file for UUID: " + uuid.toString());
			e.printStackTrace();
		}
	}
	
	public boolean getRightClickPickupToggled(Player p){
		FileConfiguration file = getDataFile(p);
		if(file.contains("rightClickPickupToggled")) {
			return file.getBoolean("rightClickPickupToggled", true);
		}else {
			file.addDefault("rightClickPickupToggled", true);
			file.set("rightClickPickupToggled", true);
			try {
				file.save(rawFile(p));
			} catch (IOException e) {
				plugin.getLogger().info("Unable to save player config file for UUID: " + p.getUniqueId().toString());
				e.printStackTrace();
			}
			return true;
		}
	}
	
	public boolean getRightClickPickupToggled(UUID uuid){
		FileConfiguration file = getDataFile(uuid);
		if(file.contains("rightClickPickupToggled")) {
			return file.getBoolean("rightClickPickupToggled", true);
		}else {
			file.addDefault("rightClickPickupToggled", true);
			file.set("rightClickPickupToggled", true);
			try {
				file.save(rawFile(uuid));
			} catch (IOException e) {
				plugin.getLogger().info("Unable to save player config file for UUID: " + uuid.toString());
				e.printStackTrace();
			}
			return true;
		}
	}
	
	public TreeMap<PlaceItemsPhysicalLocation, AdvancedPlaceItemsLinkedLocation> getPlacedItemLinkedLocations() {
		return placedItemLinkedLocations;
	}

	public void setPlacedItemLinkedLocations(TreeMap<PlaceItemsPhysicalLocation, AdvancedPlaceItemsLinkedLocation> placedItemLinkedLocations) {
		this.placedItemLinkedLocations = placedItemLinkedLocations;
	}
	
	public void addNew(UUID placer, Location physical, Location prop, BlockFace blockFace) {
		PlaceItemsPhysicalLocation rPhysical = new PlaceItemsPhysicalLocation(physical);
		if(placedItemLinkedLocations.containsKey(rPhysical)) {
			placedItemLinkedLocations.get(rPhysical).addProp(placer, prop, blockFace.toString());
		}else {
			AdvancedPlaceItemsLinkedLocation newAdvL = new AdvancedPlaceItemsLinkedLocation();
			newAdvL.addProp(placer, prop, blockFace.toString());
			placedItemLinkedLocations.put(rPhysical, newAdvL);
		}
	}
	
	public void removeAll(UUID uuid){
		Iterator<Entry<PlaceItemsPhysicalLocation, AdvancedPlaceItemsLinkedLocation>> pilli = this.placedItemLinkedLocations.entrySet().iterator();
		while(pilli.hasNext()){
			Entry<PlaceItemsPhysicalLocation, AdvancedPlaceItemsLinkedLocation> pill = pilli.next();
			int nullCount = 0;
			for(int i = 0; i < pill.getValue().getProps().length; i++) {
				if(pill.getValue().getProps()[i] == null) {
					nullCount++;
					continue;
				}
				if(pill.getValue().getProps()[i].getPlacer().equals(uuid)) {
					pill.getValue().getProps()[i] = null;
				}
				if(pill.getValue().getProps()[i] == null) {
					nullCount++;
				}
			}
			if(nullCount >= 6){
				pilli.remove();
			}
		}
	}
	
	public int getPlacementCountFromLocationData(UUID uuid){
		int count = 0;
		Iterator<Entry<PlaceItemsPhysicalLocation, AdvancedPlaceItemsLinkedLocation>> pilli = this.placedItemLinkedLocations.entrySet().iterator();
		while(pilli.hasNext()){
			Entry<PlaceItemsPhysicalLocation, AdvancedPlaceItemsLinkedLocation> pill = pilli.next();
			for(int i = 0; i < pill.getValue().getProps().length; i++) {
				if(pill.getValue().getProps()[i] != null) {
					if(pill.getValue().getProps()[i].getPlacer().equals(uuid)) {
						count++;
					}
				}
			}
		}
		return count;
	}
	
	public void removePhysical(Location physical){
		placedItemLinkedLocations.remove(new PlaceItemsPhysicalLocation(physical));
	}
	
	public void removeProp(Location[] potentialPhysicals, Location prop){
		
		for(Location check : potentialPhysicals) {
			PlaceItemsPhysicalLocation loc = new PlaceItemsPhysicalLocation(check);
			if(placedItemLinkedLocations.containsKey(loc)) {
				AdvancedPlaceItemsLinkedLocation apill = placedItemLinkedLocations.get(loc);
				int nullCount = 0;
				for(int i = 0; i < apill.getProps().length; i++) {
					if(apill.getProps()[i] == null) {
						nullCount++;
						continue;
					}
					if(apill.getProps()[i].getLocation().getWorld().getName().equals(prop.getWorld().getName()) && apill.getProps()[i].getLocation().getX() == prop.getX() && apill.getProps()[i].getLocation().getY() == prop.getY() && apill.getProps()[i].getLocation().getZ() == prop.getZ()){
						apill.getProps()[i] = null;
					}
					if(apill.getProps()[i] == null) {
						nullCount++;
					}
				}
				if(nullCount >= 6){
					placedItemLinkedLocations.remove(loc);
				}
			}
		}
	}
	
	public boolean containsPlacer(UUID uuid){
		for(AdvancedPlaceItemsLinkedLocation pill : this.placedItemLinkedLocations.values()){
			for(int i = 0; i < pill.getProps().length; i++) {
				if(pill.getProps()[i] == null) {
					continue;
				}
				if(pill.getProps()[i].getPlacer().equals(uuid)){
					return true;
				}
			}
			
		}
		return false;
	}
	
	public boolean containsPhysical(Location physical){
		return placedItemLinkedLocations.containsKey(new PlaceItemsPhysicalLocation(physical));
	}
	
	public boolean containsPropWithPhysicalBlockFace(Location physical, BlockFace blockFace){
		PlaceItemsPhysicalLocation loc = new PlaceItemsPhysicalLocation(physical);
		if(placedItemLinkedLocations.containsKey(loc)) {
			return placedItemLinkedLocations.get(loc).getProps()[PlaceItemsUtils.getPropInexFromBlockFace(blockFace.toString())] != null;
		}
		return false;
	}
	
	public boolean containsProp(Location[] potentialPhysicals, Location prop){
		for(Location check : potentialPhysicals) {
			PlaceItemsPhysicalLocation loc = new PlaceItemsPhysicalLocation(check);
			if(placedItemLinkedLocations.containsKey(loc)) {
				AdvancedPlaceItemsLinkedLocation apill = placedItemLinkedLocations.get(loc);
				for(int i = 0; i < apill.getProps().length; i++) {
					if(apill.getProps()[i] == null) {
						continue;
					}
					if(apill.getProps()[i].getLocation().getWorld().getName().equals(prop.getWorld().getName()) && apill.getProps()[i].getLocation().getX() == prop.getX() && apill.getProps()[i].getLocation().getY() == prop.getY() && apill.getProps()[i].getLocation().getZ() == prop.getZ()){
						return true;
					}
					
				}
			}
		}
		return false;
	}
	
	public AdvancedPlaceItemsLinkedLocation getFromPhysical(Location physical){
		PlaceItemsPhysicalLocation loc = new PlaceItemsPhysicalLocation(physical);
		if(placedItemLinkedLocations.containsKey(loc)) {
			return placedItemLinkedLocations.get(loc);
		}
		return null;
		
	}
	
	public Location getFromProp(Location[] potentialPhysicals, Location prop){
		for(Location check : potentialPhysicals) {
			if(placedItemLinkedLocations.containsKey(new PlaceItemsPhysicalLocation(check))) {
				return check;
			}
		}
		return null;
	}
	
	public PlaceItemsPlayerPlaceLocation getPlayerPlaceFromProp(Location[] potentialPhysicals, Location prop){
		for(Location check : potentialPhysicals) {
			PlaceItemsPhysicalLocation loc = new PlaceItemsPhysicalLocation(check);
			if(placedItemLinkedLocations.containsKey(loc)) {
				AdvancedPlaceItemsLinkedLocation apill = placedItemLinkedLocations.get(loc);
				for(int i = 0; i < apill.getProps().length; i++) {
					if(apill.getProps()[i] == null) {
						continue;
					}
					if(apill.getProps()[i].getLocation().getWorld().getName().equals(prop.getWorld().getName()) && apill.getProps()[i].getLocation().getX() == prop.getX() && apill.getProps()[i].getLocation().getY() == prop.getY() && apill.getProps()[i].getLocation().getZ() == prop.getZ()){
						return apill.getProps()[i];
					}
					
				}
			}
		}
		return null;
	}

	public HashMap<UUID, Integer> getPlayerRotationPositions() {
		return playerRotationPositions;
	}

	public void setPlayerRotationPositions(HashMap<UUID, Integer> playerRotationPositions) {
		this.playerRotationPositions = playerRotationPositions;
	}
	
	public int getSideRotation(Player player) {
		if(playerRotationPositions.containsKey(player.getUniqueId())) {
			return playerRotationPositions.get(player.getUniqueId());
		}
		return 0;
	}

}
