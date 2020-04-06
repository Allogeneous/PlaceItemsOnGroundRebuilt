package me.Allogeneous.PlaceItemsOnGroundRebuilt.Lang;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import me.Allogeneous.PlaceItemsOnGroundRebuilt.PlaceItemsMain;

public class MessageChatStringParser {
	
	public PlaceItemsMain plugin; 
	
	public MessageChatStringParser(PlaceItemsMain plugin) {
		this.plugin = plugin;
	}
	
	public String parse(String toParse, String tag, String playerName) {
		return toParse.replace(plugin.getLangFile().getPluginTag(), tag).replace(plugin.getLangFile().getPlayerNameTag(), playerName);
	}
	
	public String parse(String toParse, String tag, String playerName, String targetPlayerName) {
		return parse(toParse, tag, playerName).replace(plugin.getLangFile().getTargetPlayerName(), targetPlayerName);
	}
	
	public String parse(String toParse, String tag, String playerName, String targetPlayerName, String dataFilePlaceCap) {
		return parse(toParse, tag, playerName, targetPlayerName).replace(plugin.getLangFile().getDataFilePlaceCap(), dataFilePlaceCap);
	}
	
	public String parse(String toParse, String tag, String playerName, int sideRotation) {
		return parse(toParse, tag, playerName).replace(plugin.getLangFile().getSideRotation(), Integer.toString(sideRotation));
	}
	
	public String parse(String toParse, String tag, String playerName, int purgeArmorStandCount, int purgeRadius) {
		return parse(toParse, tag, playerName).replace(plugin.getLangFile().getPurgeArmorStandCount(), Integer.toString(purgeArmorStandCount)).replace(plugin.getLangFile().getPurgeRadius(), Integer.toString(purgeRadius));
	}
	
	public String parse(String toParse, String tag, String playerName, int purgeArmorStandCount, int purgeRadius, int purgeLocationCount) {
		return parse(toParse, tag, playerName, purgeArmorStandCount, purgeRadius).replace(plugin.getLangFile().getPurgeLocationCount(), Integer.toString(purgeLocationCount));
	}
	
	public String parse(String toParse, String tag, String playerName, String fileLineData, byte fileDataLine) {
		toParse = parse(toParse, tag, playerName);
		
		switch(fileDataLine) {
			case 0:
				toParse = toParse.replace(plugin.getLangFile().getDataFileUuid(), fileLineData);
				break;
			case 1:
				toParse = toParse.replace(plugin.getLangFile().getDataFileName(), fileLineData);
				break;
			case 2:
				toParse = toParse.replace(plugin.getLangFile().getDataFilePlaceCap(), fileLineData);
				break;
			case 3:
				toParse = toParse.replace(plugin.getLangFile().getDataFileHasCustomPlaceCap(), fileLineData);
				break;
			case 4:
				toParse = toParse.replace(plugin.getLangFile().getDataFileAmountPlaced(), fileLineData);
				break;
			case 5:
				toParse = toParse.replace(plugin.getLangFile().getDataFilePlaceToggled(), fileLineData);
				break;
			case 6:
				toParse = toParse.replace(plugin.getLangFile().getDataFileRightClickPlaceToggled(), fileLineData);
				break;
			default: 
				break;
		}
		
		return toParse;
		
	}
	
	public String parse(String toParse, String tag, String playerName, String targetPlayerName, String fileLineData, byte fileDataLine) {
		toParse = parse(toParse, tag, playerName, targetPlayerName);
		
		switch(fileDataLine) {
			case 0:
				toParse = toParse.replace(plugin.getLangFile().getDataFilePlaceCap(), fileLineData);
				break;
			case 1:
				toParse = toParse.replace(plugin.getLangFile().getDataFileHasCustomPlaceCap(), fileLineData);
				break;
			case 2:
				toParse = toParse.replace(plugin.getLangFile().getDataFileAmountPlaced(), fileLineData);
				break;
			case 3:
				toParse = toParse.replace(plugin.getLangFile().getDataFilePlaceToggled(), fileLineData);
				break;
			case 4:
				toParse = toParse.replace(plugin.getLangFile().getDataFileRightClickPlaceToggled(), fileLineData);
				break;
			default: 
				break;
		}
		
		return toParse;
	}
	
	public String parse(String toParse, String tag, String playerName, ItemStack item) {
		return parse(toParse, tag, playerName).replace(plugin.getLangFile().getItemInHand(), item.getType().toString());
	}
	
	public String parse(String toParse, String tag, String playerName, Block block) {
		return parse(toParse, tag, playerName).replace(plugin.getLangFile().getClickedBlock(), block.getType().toString());
	}
	
	public String parse(String toParse, String tag, String playerName, int syncToCount, int syncFromCount, boolean data) {
		return parse(toParse, tag, playerName).replace(plugin.getLangFile().getSyncFromCount(), Integer.toString(syncFromCount)).replace(plugin.getLangFile().getSyncToCount(), Integer.toString(syncToCount));
	}
}
