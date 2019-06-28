package me.Allogeneous.PlaceItemsOnGroundRebuilt.mcMMO;

import org.bukkit.block.Block;

import com.gmail.nossr50.mcMMO;

public class McMMOPlacementEventManager {
	
	
	public McMMOPlacementEventManager() {}
	
	public boolean handlePlacementDataPrefire(Block block) {
		return mcMMO.getPlaceStore().isTrue(block);
	}
	
	public void handlePlacementDataPostfire(Block block, boolean value) {
		if(value) {
			mcMMO.getPlaceStore().setTrue(block);
		}else {
			mcMMO.getPlaceStore().setFalse(block);
		}
	}
	

}
