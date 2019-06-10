package me.Allogeneous.PlaceItemsOnGroundRebuilt;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class PlaceItemsOnGroundBreakEvent extends BlockBreakEvent{
	
	public PlaceItemsOnGroundBreakEvent(Block theBlock, Player player) {
		super(theBlock, player);
	}
}
