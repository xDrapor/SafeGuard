package com.xdrapor.safeguard.checks.combat;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.xdrapor.safeguard.checks.SGCheck;
import com.xdrapor.safeguard.core.permissions.SGPermissibleNodes;
import com.xdrapor.safeguard.player.SGPlayer;
import com.xdrapor.safeguard.utilities.SGCheckTag;

public class SGCheckSpeed extends SGCheck {

	@Override
	public String getDescription() {
		return "Prevents a player from attacking an entity too quickly.";
	}

	@Override
	public void runCheck(Event event, SGPlayer player) {
		EntityDamageByEntityEvent eDBeEvent = (EntityDamageByEntityEvent)event;
		if(!(eDBeEvent.getDamager() instanceof Player))return;
		Player sgPlayer = (Player)eDBeEvent.getDamager();
		
		if(sgPermissions.hasPermission(player, SGPermissibleNodes.COMBAT_SPEED) || !sgConfig.isCheckEnabled(this))return;
		
		if (System.currentTimeMillis() - safeGuard.sgPlayerManager.getPlayer(sgPlayer.getPlayer().getName()).getLastHitTime() < 110) {
			safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).addVL(SGCheckTag.COMBAT_SPEED, System.currentTimeMillis() - safeGuard.sgPlayerManager.getPlayer(sgPlayer.getPlayer().getName()).getLastHitTime());
			safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).setLastHitTime(System.currentTimeMillis());
			if (safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).getVL(SGCheckTag.COMBAT_SPEED) > this.maxBuffer) {
				
				publishCheck(getClass(), sgPlayer, SGCheckTag.COMBAT_SPEED);
				
				eDBeEvent.setCancelled(true);				
				return;
			}
		} else {
			safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).setLastHitTime(System.currentTimeMillis());
			safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).reduceVL(SGCheckTag.COMBAT_SPEED);
			return;
		}

	}

}