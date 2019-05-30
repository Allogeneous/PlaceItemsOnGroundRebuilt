package me.Allogeneous.PlaceItemsOnGroundRebuilt;


import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class PlaceItemsVersionSensativeMethods {
	
	private String version;
	
	public PlaceItemsVersionSensativeMethods(String version) {
		this.version = version;
	}
	
	public boolean isValidSlabOrStair(Block block) {
		if(version.contains("1.13") || version.contains("1.14")) {
			return validSlabOrStair1_13up(block);
		}else {
			return validSlabOrStair1_12down(block);
		}
	}
	
	public boolean isItemey(ItemStack item){
		if(version.contains("1.12") || version.contains("1.13") || version.contains("1.14")) {
			return isItemey1_12up(item);
		}else {
			return isItemey1_11down(item);
		}
	}
	
	private boolean isItemey1_12up(ItemStack item){
		if(!PlaceItemsUtils.isBlacklisted(item.getType())){
			if(item.getType().isItem()){
				return true;
			}
			if(PlaceItemsUtils.isItemLikeBlock(item.getType())){
				return true;
			}
		}
		return false;
	}
	
	private boolean isItemey1_11down(ItemStack item){
		if(!PlaceItemsUtils.isBlacklisted(item.getType())){
			if(!item.getType().isBlock()){
				return true;
			}
			if(PlaceItemsUtils.isItemLikeBlock(item.getType())){
				return true;
			}
		}
		return false;
	}
	
	private boolean validSlabOrStair1_13up(Block block) {
		if(PlaceItemsUtils.isStairs(block.getType())) {
			return block.getBlockData().getAsString().contains("half=top");
		}else if(PlaceItemsUtils.isSlab(block.getType())){
			return block.getBlockData().getAsString().contains("type=top") || block.getBlockData().getAsString().contains("type=double");
		}
	}
	
	private boolean validSlabOrStair1_12down(Block block) {
		if(PlaceItemsUtils.isStairs(block.getType())) {
			return block.getState().getData().toString().contains("inverted");
		}else if(PlaceItemsUtils.isSlab(block.getType())){
			return block.getState().getData().toString().contains("inverted");
		}else if(PlaceItemsUtils.isLegacyDoubleSab(block.getType())){
			return true;
		}
		return false;
	}
	
	public String getVersion() {
		return this.version;
	}
	
	public boolean isLegacy() {
		return !(this.getVersion().contains("1.13") || this.getVersion().contains("1.14"));
	}

}
