package com.xdrapor.safeguard.checks.blockbreak;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

import com.xdrapor.safeguard.checks.SGCheck;
import com.xdrapor.safeguard.core.permissions.SGPermissibleNodes;
import com.xdrapor.safeguard.player.SGPlayer;
import com.xdrapor.safeguard.utilities.SGCheckTag;
import com.xdrapor.safeguard.utilities.SGMovementUtil;

public class SGCheckReach extends SGCheck {

	private double blockDistance;
	
	@Override
	public String getDescription() {
		return "Disallows players from breaking blocks over illegal distances.";
	}

	@Override
	public void runCheck(Event event, SGPlayer player) 
	{
		BlockBreakEvent blockBreakEvent = (BlockBreakEvent)event;
		Player sgPlayer = player.getPlayer();

		// getDistance(n) Methods used from SGMovementUtil. If we need it more than this consider moving Methods from SGMovementUtil to SGCheck.
		this.blockDistance = Math.abs(SGMovementUtil.getDistanceX(blockBreakEvent.getBlock().getLocation(), sgPlayer.getLocation(), false)
									 + SGMovementUtil.getDistanceY(blockBreakEvent.getBlock().getLocation(), sgPlayer.getLocation(), false)
									 + SGMovementUtil.getDistanceZ(blockBreakEvent.getBlock().getLocation(), sgPlayer.getLocation(), false));
		
		if(sgPermissions.hasPermission(player, SGPermissibleNodes.BLOCK_REACH) || !sgConfig.isCheckEnabled(this))return;
		
		if(getReachDistance(sgPlayer) < this.blockDistance) {

			safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).addVL(SGCheckTag.BLOCK_REACH, this.blockDistance - getReachDistance(sgPlayer));

			if (safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).getVL(SGCheckTag.BLOCK_REACH) > sgConfig.getConfig().getDouble("checks.blockbreak.reach.buffer")) {
				
				publishCheck(getClass(), sgPlayer, SGCheckTag.BLOCK_REACH);
				blockBreakEvent.setCancelled(true);
				
			}
			
			return;
		}
		
		safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).reduceVL(SGCheckTag.BLOCK_REACH);
	}
}