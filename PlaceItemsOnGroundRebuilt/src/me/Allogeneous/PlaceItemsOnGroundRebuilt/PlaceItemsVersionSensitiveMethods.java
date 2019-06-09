package me.Allogeneous.PlaceItemsOnGroundRebuilt;


import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

public class PlaceItemsVersionSensitiveMethods {
	
	private String version;
	
	public PlaceItemsVersionSensitiveMethods(String version) {
		this.version = version;
	}
	
	public boolean isValidSlab(Block block, String blockFace) {
		if(!version.contains("1.9") && !version.contains("1.10") && !version.contains("1.11") && !version.contains("1.12")) {
			return validSlab1_13up(block, blockFace);
		}else {
			return validSlab1_12down(block, blockFace);
		}
	}
	
	public boolean isItemey(ItemStack item){
		if(!version.contains("1.9") && !version.contains("1.10") && !version.contains("1.11")) {
			if(version.contains("1.12") && !version.contains("1.12.2")) {
				return isItemey1_12_1down(item);
			}
			return isItemey1_12_2up(item);
		}else {
			return isItemey1_12_1down(item);
		}
	}
	
	private boolean isItemey1_12_2up(ItemStack item){
		if(item.getType().isItem()){
			return true;
		}
		if(PlaceItemsUtils.isItemLikeBlock(item.getType())){
			return true;
		}
		return false;
	}
	
	private boolean isItemey1_12_1down(ItemStack item){
		if(!item.getType().isBlock()){
			return true;
		}
		if(PlaceItemsUtils.isItemLikeBlock(item.getType())){
			return true;
		}
		return false;
	}
	
	private boolean validSlab1_13up(Block block, String blockFace) {
		if(PlaceItemsUtils.isSlab(block.getType())){
			if(blockFace.equals(BlockFace.UP.toString())) {
				return block.getBlockData().getAsString().contains("type=top") || block.getBlockData().getAsString().contains("type=double");
			}else if(blockFace.equals(BlockFace.NORTH.toString()) || blockFace.equals(BlockFace.SOUTH.toString()) || blockFace.equals(BlockFace.WEST.toString()) || blockFace.equals(BlockFace.EAST.toString())) {
				return block.getBlockData().getAsString().contains("type=double");
			}else if(blockFace.equals(BlockFace.DOWN.toString())) {
				return block.getBlockData().getAsString().contains("type=bottom") || block.getBlockData().getAsString().contains("type=double");
			}
			
		}
		return false;
	}
	
	private boolean validSlab1_12down(Block block, String blockFace) {
		if(PlaceItemsUtils.isSlab(block.getType())){
			if(blockFace.equals(BlockFace.UP.toString())) {
				return block.getState().getData().toString().contains("inverted");
			}else if(blockFace.equals(BlockFace.DOWN.toString())) {
				return !block.getState().getData().toString().contains("inverted");
			}
		}else if(PlaceItemsUtils.isLegacyDoubleSlab(block.getType())){
			return true;
		}
		return false;
	}
	
	public String getVersion() {
		return this.version;
	}
	
	public boolean isLegacy() {
		return(version.contains("1.9") || version.contains("1.10") || version.contains("1.11") || version.contains("1.12"));
	}

}
