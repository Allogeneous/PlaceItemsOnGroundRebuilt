package me.Allogeneous.PlaceItemsOnGroundRebuilt;

import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class PlaceItemsMain extends JavaPlugin{

	private PlaceItemsManager manager;
	private PlaceItemsLocationAutoSaver autoSaver;
	private PlaceItemsVersionSensativeMethods versionHandler;
	
	@Override
	public void onEnable(){
		versionHandler = new PlaceItemsVersionSensativeMethods(Bukkit.getBukkitVersion()); 
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
		PlaceItemsConfig.setIncludeSlabs(getConfig().getBoolean("includeSlabs", false));
		PlaceItemsConfig.setIncludeStairs(getConfig().getBoolean("includeStairs", false));
		PlaceItemsConfig.setForceLegacy(getConfig().getBoolean("forceLegacy", true));
		
		if(versionHandler.isLegacy() && PlaceItemsConfig.isForceLegacy()) {
			getLogger().info("Legacy server version detected... loading proper config lists.");
			String[] blackListString = getConfig().getString("legacyBlacklistedItems").split(", ");
			PlaceItemsConfig.setBlackListedItems(new ArrayList<>(Arrays.asList(blackListString)));
			String[] stairs = getConfig().getString("legacyStairs").split(", ");
			PlaceItemsConfig.setStairs(new ArrayList<>(Arrays.asList(stairs)));
			String[] slabs = getConfig().getString("legacySlabs").split(", ");
			PlaceItemsConfig.setSlabs(new ArrayList<>(Arrays.asList(slabs)));
			String[] doubleSlabs = getConfig().getString("legacyDoubleSlabs").split(", ");
			PlaceItemsConfig.setLegacyDoubleSlabs(new ArrayList<>(Arrays.asList(doubleSlabs)));
			String[] blackListPlaceString = getConfig().getString("legacyBlacklistedPlaceItems").split(", ");
			PlaceItemsConfig.setBlackListedPlaceItems(new ArrayList<>(Arrays.asList(blackListPlaceString)));
			if(PlaceItemsConfig.isIncludeSlabs()) {
				PlaceItemsConfig.getBlackListedPlaceItems().addAll(PlaceItemsConfig.getSlabs());
				PlaceItemsConfig.getBlackListedPlaceItems().addAll(PlaceItemsConfig.getLegacyDoubleSlabs());
			}
			if(PlaceItemsConfig.isIncludeStairs()) {
				PlaceItemsConfig.getBlackListedPlaceItems().addAll(PlaceItemsConfig.getStairs());
			}
			String[] itemLikeString = getConfig().getString("legacyItemLikeBlocks").split(", ");
			PlaceItemsConfig.setItemLikeBlocks(new ArrayList<>(Arrays.asList(itemLikeString)));
			String[] blockLikeString = getConfig().getString("legacyBlockLikeItems").split(", ");
			PlaceItemsConfig.setBlockLikeItems(new ArrayList<>(Arrays.asList(blockLikeString)));
			String[] placeIn = getConfig().getString("placeIn").split(", ");
			PlaceItemsConfig.setPlaceIn(new ArrayList<>(Arrays.asList(placeIn)));
		}else {
			
			if(versionHandler.isLegacy() && !PlaceItemsConfig.isForceLegacy()) {
				getLogger().warning("Legacy server version detected... legacy lists are bypassed!");
			}
			
			String[] blackListString = getConfig().getString("blacklistedItems").split(", ");
			PlaceItemsConfig.setBlackListedItems(new ArrayList<>(Arrays.asList(blackListString)));
			String[] stairs = getConfig().getString("stairs").split(", ");
			PlaceItemsConfig.setStairs(new ArrayList<>(Arrays.asList(stairs)));
			String[] slabs = getConfig().getString("slabs").split(", ");
			PlaceItemsConfig.setSlabs(new ArrayList<>(Arrays.asList(slabs)));
			String[] blackListPlaceString = getConfig().getString("blacklistedPlaceItems").split(", ");
			PlaceItemsConfig.setBlackListedPlaceItems(new ArrayList<>(Arrays.asList(blackListPlaceString)));
			if(PlaceItemsConfig.isIncludeSlabs()) {
				PlaceItemsConfig.getBlackListedPlaceItems().addAll(PlaceItemsConfig.getSlabs());
			}
			if(PlaceItemsConfig.isIncludeStairs()) {
				PlaceItemsConfig.getBlackListedPlaceItems().addAll(PlaceItemsConfig.getStairs());
			}
			String[] itemLikeString = getConfig().getString("itemLikeBlocks").split(", ");
			PlaceItemsConfig.setItemLikeBlocks(new ArrayList<>(Arrays.asList(itemLikeString)));
			String[] blockLikeString = getConfig().getString("blockLikeItems").split(", ");
			PlaceItemsConfig.setBlockLikeItems(new ArrayList<>(Arrays.asList(blockLikeString)));
			String[] placeIn = getConfig().getString("placeIn").split(", ");
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
		if(getConfig().getInt("configVersion", 0) != 2){
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
