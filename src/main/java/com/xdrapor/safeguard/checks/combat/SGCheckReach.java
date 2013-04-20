package com.xdrapor.safeguard.checks.combat;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.xdrapor.safeguard.checks.SGCheck;
import com.xdrapor.safeguard.core.permissions.SGPermissibleNodes;
import com.xdrapor.safeguard.player.SGPlayer;
import com.xdrapor.safeguard.utilities.SGCheckTag;
import com.xdrapor.safeguard.utilities.SGMovementUtil;

public class SGCheckReach extends SGCheck {

	private double reachDistance;
	
	@Override
	public String getDescription() {
		return "Disallows players from attacking entities over illegal distances.";
	}

	@Override
	public void runCheck(Event event, SGPlayer player) {
	
		if(player == null || event == null)return;
		if(sgPermissions.hasPermission(player, SGPermissibleNodes.COMBAT_REACH) || !sgConfig.isCheckEnabled(this))return;
		
		EntityDamageByEntityEvent eDBeEvent = (EntityDamageByEntityEvent)event;
		if(!(eDBeEvent.getDamager() instanceof Player))return;
		
		Player sgPlayer = player.getPlayer();

		// getDistance(n) Methods used from SGMovementUtil. If we need it more than this consider moving Methods from SGMovementUtil to SGCheck.
		this.reachDistance = 		Math.abs(SGMovementUtil.getDistanceX(eDBeEvent.getEntity().getLocation(), sgPlayer.getLocation(), false)
									 + SGMovementUtil.getDistanceY(eDBeEvent.getEntity().getLocation(), sgPlayer.getLocation(), false)
									 + SGMovementUtil.getDistanceZ(eDBeEvent.getEntity().getLocation(), sgPlayer.getLocation(), false));
		
		double maxDistance = sgConfig.getConfig().getDouble("checks.combat_reach.distance");
		
		if(maxDistance < this.reachDistance) {

			safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).addVL(SGCheckTag.COMBAT_REACH, this.reachDistance - maxDistance);
			
			publishCheck(getClass(), sgPlayer, SGCheckTag.COMBAT_REACH);
			
			eDBeEvent.setCancelled(true);
			return;
		}
		
		safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).reduceVL(SGCheckTag.COMBAT_REACH);
	}
}