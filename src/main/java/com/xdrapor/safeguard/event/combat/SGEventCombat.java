package com.xdrapor.safeguard.event.combat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.xdrapor.safeguard.checks.SGCheck;
import com.xdrapor.safeguard.checks.combat.SGCheckReach;
import com.xdrapor.safeguard.checks.combat.SGCheckSpeed;
import com.xdrapor.safeguard.event.SGEventListener;

public class SGEventCombat extends SGEventListener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void checkEntityAttack(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player) {
			for(SGCheck sgCheck : sgChecks) {
				if (safeGuard.sgPlayerManager.isTracking((Player)event.getDamager())) {
					sgCheck.runCheck(event, safeGuard.sgPlayerManager.getPlayer(((Player)event.getDamager()).getName()));	
				}
			}				
		}
	}
	
	@Override
	public void loadChecks() {
		sgChecks.add(new SGCheckReach());
		sgChecks.add(new SGCheckSpeed());
	}

}
