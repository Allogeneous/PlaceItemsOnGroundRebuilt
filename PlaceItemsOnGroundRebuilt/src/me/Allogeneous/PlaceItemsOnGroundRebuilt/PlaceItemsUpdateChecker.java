package me.Allogeneous.PlaceItemsOnGroundRebuilt;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.scheduler.BukkitRunnable;

public class PlaceItemsUpdateChecker extends BukkitRunnable{
	
	private PlaceItemsMain plugin;
	private final String version;
	
	public PlaceItemsUpdateChecker(PlaceItemsMain plugin, String version) {
		this.plugin = plugin;
		this.version = version;
	}

	@Override
	public void run() {
		URL urlVersion;
		URLConnection urlConnection;
		try {
			urlVersion = new URL("https://api.spigotmc.org/legacy/update.php?resource=67410");
			urlConnection = urlVersion.openConnection();
			BufferedReader versionReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String siteVersion = versionReader.readLine();
            if(siteVersion.equals(version)) {
            	plugin.getLogger().info("No updates found!");
            }else {
            	plugin.getLogger().info("Found an update! The current version is: " + siteVersion + "! Get it here, https://www.spigotmc.org/resources/place-items-on-ground-rebuilt-1-13-1-14.67410/");
                
            }
            versionReader.close();
		}catch(Exception e) {
			plugin.getLogger().warning("An error occured while checking for version updates!");
		}
	}

}
