package me.Allogeneous.PlaceItems.Lang;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.Allogeneous.PlaceItemsOnGroundRebuilt.PlaceItemsConfig;
import me.Allogeneous.PlaceItemsOnGroundRebuilt.PlaceItemsMain;

public class LanguageFile {
	
	private PlaceItemsMain plugin;
	
	private ArrayList<String> generalTags = new ArrayList<>((List<String>) Arrays.asList(new String[] {"<playerName>", "<tag>"}));
	private ArrayList<String> specificTags = new ArrayList<>((List<String>) Arrays.asList(new String[] {"<dataFileUuid>", "<dataFileName>", "<dataFilePlaceCap>", "<dataFileHasCustomPlaceCap>", "<dataFileAmountPlaced>", "<dataFilePlaceToggled>",
			"<dataFileRightClickPlaceToggled>", "<clickedBlocked>", "<purgeRadius>", "<targetPlayerName>", "<purgeLocationCount>", "<purgeArmorStandCount>", "<itemInHand>","<sideRotation>"}));
	
	private FileConfiguration langFileConfig;
	private File langFile;
	
	private int langVersion = 1;
	
	public LanguageFile(PlaceItemsMain plugin) {
		this.plugin = plugin;
	}
	
	public boolean loadLanguageFile() {
		if(createLangFile()) {
			reloadLangFile();
			verifyLangVersion();
			return true;
		}
		return false;
	}
	
	private void verifyLangVersion(){
		if(getLangFileData().getInt("version", 0) != langVersion){
			plugin.getLogger().info("Outdated lang file found, creating a new one and copying the old one...");
			try{
			      File file = new File(plugin.getDataFolder(), PlaceItemsConfig.getLanguageFile());
			      File lastConfig = new File(plugin.getDataFolder(), "last_" + PlaceItemsConfig.getLanguageFile());
			      if (file.exists()){
			    	if(lastConfig.exists()){
			    		lastConfig.delete();
			    	}
			    	file.renameTo(lastConfig);
			        file.delete();
			        saveDefaultLangFile();
			        plugin.getLogger().info("Lang file updated!");
			      }
			    }
			 catch (Exception ex) {
			    plugin.getLogger().info("Something went wrong creating the new lang file!");
			 }  
		}
	}
	
	public boolean createLangFile(){
		try{
			if(!plugin.getDataFolder().exists()){
				plugin.getDataFolder().mkdirs();
			}
			File file = new File(plugin.getDataFolder(), PlaceItemsConfig.getLanguageFile());
				if(!file.exists()){
					saveDefaultLangFile();
				}
		    }
		 catch (Exception ex) {
		    Bukkit.getLogger().warning("Error creating language file, shutting down plugin!");
		    return false;
		 }
		return true;
	}
	
	public FileConfiguration getLangFileData() {
	    if (langFileConfig == null) {
	        reloadLangFile();
	    }
	    return langFileConfig;
	}
	
	private void saveDefaultLangFile() {
	    if (langFile == null) {
	        langFile = new File(plugin.getDataFolder(), PlaceItemsConfig.getLanguageFile());
	    }
	    if (!langFile.exists()) {            
	         plugin.saveResource("plugin_msgs_en.yml", false);
	     }
	}
	
	private void reloadLangFile() {
	    if (langFile == null) {
	    	langFile = new File(plugin.getDataFolder(), PlaceItemsConfig.getLanguageFile());
	    }
	    langFileConfig = YamlConfiguration.loadConfiguration(langFile);

	    Reader defConfigStream = null;
		try {
			defConfigStream = new InputStreamReader(plugin.getResource(PlaceItemsConfig.getLanguageFile()), "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        langFileConfig.setDefaults(defConfig);
	    }
	}
	
	public String getPlayerNameTag() {
		return generalTags.get(0);
	}
	
	public String getPluginTag() {
		return generalTags.get(1);
	}
	
	public String getDataFileUuid() {
		return specificTags.get(0);
	}
	
	public String getDataFileName() {
		return specificTags.get(1);
	}
	
	public String getDataFilePlaceCap() {
		return specificTags.get(2);
	}
	
	public String getDataFileHasCustomPlaceCap() {
		return specificTags.get(3);
	}
	
	public String getDataFileAmountPlaced() {
		return specificTags.get(4);
	}
	
	public String getDataFilePlaceToggled() {
		return specificTags.get(5);
	}
	
	public String getDataFileRightClickPlaceToggled() {
		return specificTags.get(6);
	}
	
	public String getClickedBlock() {
		return specificTags.get(7);
	}
	
	public String getPurgeRadius() {
		return specificTags.get(8);
	}
	
	public String getTargetPlayerName() {
		return specificTags.get(9);
	}
	
	public String getPurgeLocationCount() {
		return specificTags.get(10);
	}
	
	public String getPurgeArmorStandCount() {
		return specificTags.get(11);
	}
	
	public String getItemInHand() {
		return specificTags.get(12);
	}
	
	public String getSideRotation() {
		return specificTags.get(13);
	}
}
