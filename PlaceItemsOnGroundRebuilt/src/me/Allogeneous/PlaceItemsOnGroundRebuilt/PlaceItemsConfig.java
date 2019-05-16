package me.Allogeneous.PlaceItemsOnGroundRebuilt;

import java.util.ArrayList;

public class PlaceItemsConfig {
	
	private static boolean useLocationAutoSave;
	private static boolean includeSlabs;
	private static boolean includeStairs;
	private static int locationAutoSaveTime;
	private static int defaultPlaceCap;
	private static int configVersion;

	private static ArrayList<String> blackListedItems;
	private static ArrayList<String> blackListedPlaceItems;
	private static ArrayList<String> itemLikeBlocks;
	private static ArrayList<String> blockLikeItems;
	private static ArrayList<String> placeIn;
	private static ArrayList<String> slabs;
	private static ArrayList<String> stairs;
	

	public static boolean useLocationAutoSave() {
		return useLocationAutoSave;
	}
	public static void setUseLocationAutoSave(boolean useAutoSave) {
		PlaceItemsConfig.useLocationAutoSave = useAutoSave;
	}
	public static int getLocationAutoSaveTime() {
		return locationAutoSaveTime;
	}
	public static void setLocationAutoSaveTime(int autoSaveTime) {
		PlaceItemsConfig.locationAutoSaveTime = autoSaveTime;
	}
	public static int getDefaultPlaceCap() {
		return defaultPlaceCap;
	}
	public static void setDefaultPlaceCap(int defaultPlaceCap) {
		PlaceItemsConfig.defaultPlaceCap = defaultPlaceCap;
	}
	public static ArrayList<String> getBlackListedItems() {
		return blackListedItems;
	}
	public static void setBlackListedItems(ArrayList<String> blackListedItems) {
		PlaceItemsConfig.blackListedItems = blackListedItems;
	}
	public static ArrayList<String> getItemLikeBlocks() {
		return itemLikeBlocks;
	}
	public static void setItemLikeBlocks(ArrayList<String> itemLikeBlocks) {
		PlaceItemsConfig.itemLikeBlocks = itemLikeBlocks;
	}
	public static ArrayList<String> getBlockLikeItems() {
		return blockLikeItems;
	}
	public static void setBlockLikeItems(ArrayList<String> blockLikeItems) {
		PlaceItemsConfig.blockLikeItems = blockLikeItems;
	}
	public static int getConfigVersion() {
		return configVersion;
	}
	public static void setConfigVersion(int configVersion) {
		PlaceItemsConfig.configVersion = configVersion;
	}
	public static ArrayList<String> getBlackListedPlaceItems() {
		return blackListedPlaceItems;
	}
	public static void setBlackListedPlaceItems(ArrayList<String> blackListedPlaceItems) {
		PlaceItemsConfig.blackListedPlaceItems = blackListedPlaceItems;
	}
	public static ArrayList<String> getPlaceIn() {
		return placeIn;
	}
	public static void setPlaceIn(ArrayList<String> placeIn) {
		PlaceItemsConfig.placeIn = placeIn;
	}
	public static ArrayList<String> getSlabs() {
		return slabs;
	}
	public static void setSlabs(ArrayList<String> slabs) {
		PlaceItemsConfig.slabs = slabs;
	}
	public static ArrayList<String> getStairs() {
		return stairs;
	}
	public static void setStairs(ArrayList<String> stairs) {
		PlaceItemsConfig.stairs = stairs;
	}
	public static boolean isIncludeSlabs() {
		return includeSlabs;
	}
	public static void setIncludeSlabs(boolean includeSlabs) {
		PlaceItemsConfig.includeSlabs = includeSlabs;
	}
	public static boolean isIncludeStairs() {
		return includeStairs;
	}
	public static void setIncludeStairs(boolean includeStairs) {
		PlaceItemsConfig.includeStairs = includeStairs;
	}
	

}
