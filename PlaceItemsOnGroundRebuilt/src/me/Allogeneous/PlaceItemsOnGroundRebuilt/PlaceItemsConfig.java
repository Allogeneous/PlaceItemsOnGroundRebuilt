package me.Allogeneous.PlaceItemsOnGroundRebuilt;

import java.util.ArrayList;

public class PlaceItemsConfig {
	
	private static String languageFile;
	private static boolean useLocationAutoSave;
	private static boolean forceLegacy;
	private static int locationAutoSaveTime;
	private static int defaultPlaceCap;
	private static boolean allowTopPlacing;
	private static boolean allowSidePlacing;
	private static boolean allowBottomPlacing;
	private static boolean includeBlocksThatCanMoveOrDisappear;
	private static boolean strictCompatibilityMode;
	private static boolean repeatUpdateChecker;
	private static boolean bStats;
	private static int configVersion;
	
	private static boolean plotSquaredClear;

	private static ArrayList<String> blackListedItemsAll;
	private static ArrayList<String> blackListedItemsTop;
	private static ArrayList<String> blackListedItemsSides;
	private static ArrayList<String> blackListedItemsBottom;
	private static ArrayList<String> blackListedPlaceItemsAll;
	private static ArrayList<String> blackListedPlaceItemsTop;
	private static ArrayList<String> blackListedPlaceItemsSides;
	private static ArrayList<String> blackListedPlaceItemsBottom;
	private static ArrayList<String> itemLikeBlocks;
	private static ArrayList<String> blockLikeItems;
	private static ArrayList<String> placeIn;
	private static ArrayList<String> slabs;
	private static ArrayList<String> legacyDoubleSlabs;
	private static ArrayList<String> stairs;
	private static ArrayList<String> blacklistedWorlds;
	

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
	public static ArrayList<String> getLegacyDoubleSlabs() {
		return legacyDoubleSlabs;
	}
	public static void setLegacyDoubleSlabs(ArrayList<String> legacyDoubleSlabs) {
		PlaceItemsConfig.legacyDoubleSlabs = legacyDoubleSlabs;
	}
	public static boolean isForceLegacy() {
		return forceLegacy;
	}
	public static void setForceLegacy(boolean forceLegacy) {
		PlaceItemsConfig.forceLegacy = forceLegacy;
	}
	public static boolean isAllowTopPlacing() {
		return allowTopPlacing;
	}
	public static void setAllowTopPlacing(boolean allowTopPlacing) {
		PlaceItemsConfig.allowTopPlacing = allowTopPlacing;
	}
	public static boolean isAllowSidePlacing() {
		return allowSidePlacing;
	}
	public static void setAllowSidePlacing(boolean allowSidePlacing) {
		PlaceItemsConfig.allowSidePlacing = allowSidePlacing;
	}
	public static boolean isAllowBottomPlacing() {
		return allowBottomPlacing;
	}
	public static void setAllowBottomPlacing(boolean allowBottomPlacing) {
		PlaceItemsConfig.allowBottomPlacing = allowBottomPlacing;
	}
	public static ArrayList<String> getBlackListedItemsAll() {
		return blackListedItemsAll;
	}
	public static void setBlackListedItemsAll(ArrayList<String> blackListedItemsAll) {
		PlaceItemsConfig.blackListedItemsAll = blackListedItemsAll;
	}
	public static ArrayList<String> getBlackListedItemsTop() {
		return blackListedItemsTop;
	}
	public static void setBlackListedItemsTop(ArrayList<String> blackListedItemsTop) {
		PlaceItemsConfig.blackListedItemsTop = blackListedItemsTop;
	}
	public static ArrayList<String> getBlackListedItemsSides() {
		return blackListedItemsSides;
	}
	public static void setBlackListedItemsSides(ArrayList<String> blackListedItemsSides) {
		PlaceItemsConfig.blackListedItemsSides = blackListedItemsSides;
	}
	public static ArrayList<String> getBlackListedItemsBottom() {
		return blackListedItemsBottom;
	}
	public static void setBlackListedItemsBottom(ArrayList<String> blackListedItemsBottom) {
		PlaceItemsConfig.blackListedItemsBottom = blackListedItemsBottom;
	}
	public static ArrayList<String> getBlackListedPlaceItemsAll() {
		return blackListedPlaceItemsAll;
	}
	public static void setBlackListedPlaceItemsAll(ArrayList<String> blackListedPlaceItemsAll) {
		PlaceItemsConfig.blackListedPlaceItemsAll = blackListedPlaceItemsAll;
	}
	public static ArrayList<String> getBlackListedPlaceItemsTop() {
		return blackListedPlaceItemsTop;
	}
	public static void setBlackListedPlaceItemsTop(ArrayList<String> blackListedPlaceItemsTop) {
		PlaceItemsConfig.blackListedPlaceItemsTop = blackListedPlaceItemsTop;
	}
	public static ArrayList<String> getBlackListedPlaceItemsSides() {
		return blackListedPlaceItemsSides;
	}
	public static void setBlackListedPlaceItemsSides(ArrayList<String> blackListedPlaceItemsSides) {
		PlaceItemsConfig.blackListedPlaceItemsSides = blackListedPlaceItemsSides;
	}
	public static ArrayList<String> getBlackListedPlaceItemsBottom() {
		return blackListedPlaceItemsBottom;
	}
	public static void setBlackListedPlaceItemsBottom(ArrayList<String> blackListedPlaceItemsBottom) {
		PlaceItemsConfig.blackListedPlaceItemsBottom = blackListedPlaceItemsBottom;
	}
	public static boolean isIncludeBlocksThatCanMoveOrDisappear() {
		return includeBlocksThatCanMoveOrDisappear;
	}
	public static void setIncludeBlocksThatCanMoveOrDisappear(boolean includeBlocksThatCanMoveOrDisappear) {
		PlaceItemsConfig.includeBlocksThatCanMoveOrDisappear = includeBlocksThatCanMoveOrDisappear;
	}
	public static boolean isPlotSquaredClear() {
		return plotSquaredClear;
	}
	public static void setPlotSquaredClear(boolean plotSquaredClear) {
		PlaceItemsConfig.plotSquaredClear = plotSquaredClear;
	}
	public static ArrayList<String> getBlacklistedWorlds() {
		return blacklistedWorlds;
	}
	public static void setBlacklistedWorlds(ArrayList<String> blacklistedWorlds) {
		PlaceItemsConfig.blacklistedWorlds = blacklistedWorlds;
	}
	public static boolean isRepeatUpdateChecker() {
		return repeatUpdateChecker;
	}
	public static void setRepeatUpdateChecker(boolean repeatUpdateChecker) {
		PlaceItemsConfig.repeatUpdateChecker = repeatUpdateChecker;
	}
	public static boolean isStrictCompatibilityMode() {
		return strictCompatibilityMode;
	}
	public static void setStrictCompatibilityMode(boolean strictCompatibilityMode) {
		PlaceItemsConfig.strictCompatibilityMode = strictCompatibilityMode;
	}
	public static boolean isbStats() {
		return bStats;
	}
	public static void setbStats(boolean bStats) {
		PlaceItemsConfig.bStats = bStats;
	}
	public static String getLanguageFile() {
		return languageFile;
	}
	public static void setLanguageFile(String languageFile) {
		PlaceItemsConfig.languageFile = languageFile;
	}

}
