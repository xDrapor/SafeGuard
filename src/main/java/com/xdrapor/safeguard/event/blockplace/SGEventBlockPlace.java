package com.xdrapor.safeguard.event.blockplace;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;

import com.xdrapor.safeguard.checks.SGCheck;
import com.xdrapor.safeguard.checks.blockplace.SGCheckReach;
import com.xdrapor.safeguard.checks.blockplace.SGCheckSpeed;
import com.xdrapor.safeguard.event.SGEventListener;

public class SGEventBlockPlace extends SGEventListener {
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void checkBlockPlace(BlockPlaceEvent event) {
		for(SGCheck sgCheck : sgChecks) {
			if (safeGuard.sgPlayerManager.isTracking(event.getPlayer())) {
				sgCheck.runCheck(event, safeGuard.sgPlayerManager.getPlayer(event.getPlayer().getName()));	
			}
		}			
	}

	@Override
	public void loadChecks() {
		sgChecks.add(new SGCheckReach());
		sgChecks.add(new SGCheckSpeed());
	}

}
