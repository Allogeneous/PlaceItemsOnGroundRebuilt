package me.Allogeneous.PlaceItemsOnGroundRebuilt;

import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.Allogeneous.PlaceItemsOnGroundRebuilt.Files.PlaceItemsLocationAutoSaver;
import me.Allogeneous.PlaceItemsOnGroundRebuilt.Files.PlaceItemsManager;


public class PlaceItemsMain extends JavaPlugin{

	private PlaceItemsManager manager;
	private PlaceItemsLocationAutoSaver autoSaver;
	private PlaceItemsVersionSensitiveMethods versionHandler;
	private int configVersion = 6;
	
	@Override
	public void onEnable(){
		versionHandler = new PlaceItemsVersionSensitiveMethods(Bukkit.getBukkitVersion()); 
		createConfig();
		verifyConfigVersion();
		loadConfig();
		
		
		manager = new PlaceItemsManager(this);
		Bukkit.getPluginManager().registerEvents(new PlaceItemsEvents(manager, versionHandler), this);
		this.getCommand("placeitems").setExecutor(new PlaceItemsCommands(this, manager));
		this.getCommand("placeitems").setTabCompleter(new PlaceItemsTabCompleter());
		if(PlaceItemsConfig.useLocationAutoSave()){
			autoSaver = new PlaceItemsLocationAutoSaver(manager);
			int time = PlaceItemsConfig.getLocationAutoSaveTime();
			if(time < 1){
				time = 1;
			}
			autoSaver.runTaskTimerAsynchronously(this, time * 1200, time * 1200);
		}
		
		PlaceItemsUpdateChecker updateChecker = new PlaceItemsUpdateChecker(this, this.getDescription().getVersion());
		updateChecker.runTaskTimerAsynchronously(this, 0, 60 * 1200);

	}
	
	@Override
	public void onDisable(){
		manager.saveLocationData();
	}
	
	protected void reloadTheConfigFile() {
		createConfig();
		reloadConfig();
		verifyConfigVersion();
		loadConfig();

		if(PlaceItemsConfig.useLocationAutoSave()){
			if(autoSaver != null) {
				autoSaver.cancel();
			}
			autoSaver = new PlaceItemsLocationAutoSaver(manager);
			int time = PlaceItemsConfig.getLocationAutoSaveTime();
			if(time < 1){
				time = 1;
			}
			autoSaver.runTaskTimerAsynchronously(this, time * 1200, time * 1200);
		}else {
			autoSaver = new PlaceItemsLocationAutoSaver(manager);
			autoSaver.cancel();
		}
		
		getLogger().info("Config file reloaded!");
	}
	
	private void loadConfig(){
		PlaceItemsConfig.setUseLocationAutoSave(getConfig().getBoolean("useLocationAutoSave", true));
		PlaceItemsConfig.setLocationAutoSaveTime(getConfig().getInt("locationAutoSaveTime", 1));
		PlaceItemsConfig.setDefaultPlaceCap(getConfig().getInt("defaultPlaceCap", 10));
		PlaceItemsConfig.setForceLegacy(getConfig().getBoolean("forceLegacy", true));
		PlaceItemsConfig.setAllowTopPlacing(getConfig().getBoolean("allowTopPlacing", true));
		PlaceItemsConfig.setAllowSidePlacing(getConfig().getBoolean("allowSidePlacing", true));
		PlaceItemsConfig.setAllowBottomPlacing(getConfig().getBoolean("allowBottomPlacing", true));
		PlaceItemsConfig.setIncludeBlocksThatCanMoveOrDisappear((getConfig().getBoolean("includeBlocksThatCanMoveOrDisappear", false)));
		
		if(versionHandler.isLegacy() && PlaceItemsConfig.isForceLegacy()) {
			getLogger().info("Legacy server version detected... loading proper config lists.");
			String[] blackListStringAll = getConfig().getString("legacyBlacklistedItemsAll", "").split(", ");
			PlaceItemsConfig.setBlackListedItemsAll(new ArrayList<>(Arrays.asList(blackListStringAll)));
			String[] blackListStringTop = getConfig().getString("legacyBlacklistedItemsTop", "").split(", ");
			PlaceItemsConfig.setBlackListedItemsTop(new ArrayList<>(Arrays.asList(blackListStringTop)));
			String[] blackListStringSides = getConfig().getString("legacyBlacklistedItemsSides", "").split(", ");
			PlaceItemsConfig.setBlackListedItemsSides(new ArrayList<>(Arrays.asList(blackListStringSides)));
			String[] blackListStringBottom = getConfig().getString("legacyBlacklistedItemsBottom", "").split(", ");
			PlaceItemsConfig.setBlackListedItemsBottom(new ArrayList<>(Arrays.asList(blackListStringBottom)));
			String[] stairs = getConfig().getString("legacyStairs", "").split(", ");
			PlaceItemsConfig.setStairs(new ArrayList<>(Arrays.asList(stairs)));
			String[] slabs = getConfig().getString("legacySlabs", "").split(", ");
			PlaceItemsConfig.setSlabs(new ArrayList<>(Arrays.asList(slabs)));
			String[] doubleSlabs = getConfig().getString("legacyDoubleSlabs", "").split(", ");
			PlaceItemsConfig.setLegacyDoubleSlabs(new ArrayList<>(Arrays.asList(doubleSlabs)));
			String[] blackListPlaceString = getConfig().getString("legacyBlacklistedPlaceItemsAll", "").split(", ");
			PlaceItemsConfig.setBlackListedPlaceItemsAll(new ArrayList<>(Arrays.asList(blackListPlaceString)));
			if(PlaceItemsConfig.isIncludeBlocksThatCanMoveOrDisappear()) {
				String[] blocksThatCanMoveOrDisappearString = getConfig().getString("legacyBlocksThatCanMoveOrDisappear", "").split(", ");
				PlaceItemsConfig.setBlackListedPlaceItemsAll(new ArrayList<>(Arrays.asList(blocksThatCanMoveOrDisappearString)));
			}
			String[] blackListPlaceStringTop = getConfig().getString("legacyBlacklistedPlaceItemsTop", "").split(", ");
			PlaceItemsConfig.setBlackListedPlaceItemsTop(new ArrayList<>(Arrays.asList(blackListPlaceStringTop)));
			String[] blackListPlaceStringSides = getConfig().getString("legacyBlacklistedPlaceItemsSides", "").split(", ");
			PlaceItemsConfig.setBlackListedPlaceItemsSides(new ArrayList<>(Arrays.asList(blackListPlaceStringSides)));
			String[] blackListPlaceStringBottom = getConfig().getString("legacyBlacklistedPlaceItemsBottom", "").split(", ");
			PlaceItemsConfig.setBlackListedPlaceItemsBottom(new ArrayList<>(Arrays.asList(blackListPlaceStringBottom)));
			String[] itemLikeString = getConfig().getString("legacyItemLikeBlocks", "").split(", ");
			PlaceItemsConfig.setItemLikeBlocks(new ArrayList<>(Arrays.asList(itemLikeString)));
			String[] blockLikeString = getConfig().getString("legacyBlockLikeItems", "").split(", ");
			PlaceItemsConfig.setBlockLikeItems(new ArrayList<>(Arrays.asList(blockLikeString)));
			String[] placeIn = getConfig().getString("placeIn", "").split(", ");
			PlaceItemsConfig.setPlaceIn(new ArrayList<>(Arrays.asList(placeIn)));
		}else {
			
			if(versionHandler.isLegacy() && !PlaceItemsConfig.isForceLegacy()) {
				getLogger().warning("Legacy server version detected... legacy lists are bypassed!");
			}
			
			String[] blackListStringAll = getConfig().getString("blacklistedItemsAll", "").split(", ");
			PlaceItemsConfig.setBlackListedItemsAll(new ArrayList<>(Arrays.asList(blackListStringAll)));
			String[] blackListStringTop = getConfig().getString("blacklistedItemsTop" , "").split(", ");
			PlaceItemsConfig.setBlackListedItemsTop(new ArrayList<>(Arrays.asList(blackListStringTop)));
			String[] blackListStringSides = getConfig().getString("blacklistedItemsSides", "").split(", ");
			PlaceItemsConfig.setBlackListedItemsSides(new ArrayList<>(Arrays.asList(blackListStringSides)));
			String[] blackListStringBottom = getConfig().getString("blacklistedItemsBottom", "").split(", ");
			PlaceItemsConfig.setBlackListedItemsBottom(new ArrayList<>(Arrays.asList(blackListStringBottom)));
			String[] stairs = getConfig().getString("stairs", "").split(", ");
			PlaceItemsConfig.setStairs(new ArrayList<>(Arrays.asList(stairs)));
			String[] slabs = getConfig().getString("slabs", "").split(", ");
			PlaceItemsConfig.setSlabs(new ArrayList<>(Arrays.asList(slabs)));
			String[] blackListPlaceStringAll = getConfig().getString("blacklistedPlaceItemsAll", "").split(", ");
			PlaceItemsConfig.setBlackListedPlaceItemsAll(new ArrayList<>(Arrays.asList(blackListPlaceStringAll)));
			if(PlaceItemsConfig.isIncludeBlocksThatCanMoveOrDisappear()) {
				String[] blocksThatCanMoveOrDisappearString = getConfig().getString("blocksThatCanMoveOrDisappear", "").split(", ");
				PlaceItemsConfig.setBlackListedPlaceItemsAll(new ArrayList<>(Arrays.asList(blocksThatCanMoveOrDisappearString)));
			}
			String[] blackListPlaceStringTop = getConfig().getString("blacklistedPlaceItemsTop", "").split(", ");
			PlaceItemsConfig.setBlackListedPlaceItemsTop(new ArrayList<>(Arrays.asList(blackListPlaceStringTop)));
			String[] blackListPlaceStringSides = getConfig().getString("blacklistedPlaceItemsSides", "").split(", ");
			PlaceItemsConfig.setBlackListedPlaceItemsSides(new ArrayList<>(Arrays.asList(blackListPlaceStringSides)));
			String[] blackListPlaceStringBottom = getConfig().getString("blacklistedPlaceItemsBottom", "").split(", ");
			PlaceItemsConfig.setBlackListedPlaceItemsBottom(new ArrayList<>(Arrays.asList(blackListPlaceStringBottom)));
			String[] itemLikeString = getConfig().getString("itemLikeBlocks", "").split(", ");
			PlaceItemsConfig.setItemLikeBlocks(new ArrayList<>(Arrays.asList(itemLikeString)));
			String[] blockLikeString = getConfig().getString("blockLikeItems", "").split(", ");
			PlaceItemsConfig.setBlockLikeItems(new ArrayList<>(Arrays.asList(blockLikeString)));
			String[] placeIn = getConfig().getString("placeIn", "").split(", ");
			PlaceItemsConfig.setPlaceIn(new ArrayList<>(Arrays.asList(placeIn)));
		}
		
		
	}
	
	private void createConfig(){
		try{
			if(!getDataFolder().exists()){
				getDataFolder().mkdirs();
			}
			File file = new File(getDataFolder(), "config.yml");
				if(!file.exists()){
					saveDefaultConfig();
				}
		    }
		 catch (Exception ex) {
		    Bukkit.getLogger().info("Error creating config file!");
		 }
	}
	
	private void verifyConfigVersion(){
		if(getConfig().getInt("configVersion", 0) != configVersion){
			getLogger().info("Invalid config file found, creating a new one and copying the old one...");
			try{
			      File file = new File(getDataFolder(), "config.yml");
			      File lastConfig = new File(getDataFolder(), "last_config.yml");
			      if (file.exists()){
			    	if(lastConfig.exists()){
			    		lastConfig.delete();
			    	}
			    	file.renameTo(lastConfig);
			        file.delete();
			        saveDefaultConfig();
			        getLogger().info("Config files updated!");
			      }
			    }
			 catch (Exception ex) {
			    getLogger().info("Something went wrong creating the new config file!");
			 }  
		}
	}
	
	
	
}
