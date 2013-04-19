package com.xdrapor.safeguard.checks.blockbreak;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

import com.xdrapor.safeguard.checks.SGCheck;
import com.xdrapor.safeguard.core.permissions.SGPermissibleNodes;
import com.xdrapor.safeguard.player.SGPlayer;
import com.xdrapor.safeguard.utilities.SGBlockUtil;
import com.xdrapor.safeguard.utilities.SGCheckTag;

public class SGCheckSpeed extends SGCheck {

	@Override
	public String getDescription() {
		return "Prevents players from breaking a block too quickly";
	}

	@Override
	public void runCheck(Event event, SGPlayer player) {

		BlockBreakEvent blockBreakEvent = (BlockBreakEvent)event;
		Player sgPlayer = player.getPlayer();
		
		if(sgPermissions.hasPermission(player, SGPermissibleNodes.BLOCK_SPEED) || !sgConfig.isCheckEnabled(this))return;

		if(SGBlockUtil.getDurationVSTool(player, blockBreakEvent.getPlayer().getItemInHand(), blockBreakEvent.getBlock()) - SGBlockUtil.getDuration(sgPlayer) > 100) {
			safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).addVL(SGCheckTag.BLOCK_BREAKSPEED, SGBlockUtil.getDurationVSTool(player, blockBreakEvent.getPlayer().getItemInHand(), blockBreakEvent.getBlock()) - SGBlockUtil.getDuration(sgPlayer));

			if (safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).getVL(SGCheckTag.BLOCK_BREAKSPEED) > this.maxBuffer) {
				
				publishCheck(getClass(), sgPlayer, SGCheckTag.BLOCK_BREAKSPEED);
				blockBreakEvent.setCancelled(true);

			}
			return;
		} else {
			safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).reduceVL(SGCheckTag.BLOCK_BREAKSPEED);
			return;
		}
	}
}
