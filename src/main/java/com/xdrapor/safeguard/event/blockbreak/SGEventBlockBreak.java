package com.xdrapor.safeguard.event.blockbreak;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;

import com.xdrapor.safeguard.checks.SGCheck;
import com.xdrapor.safeguard.checks.blockbreak.SGCheckReach;
import com.xdrapor.safeguard.checks.blockbreak.SGCheckSpeed;
import com.xdrapor.safeguard.event.SGEventListener;

public class SGEventBlockBreak extends SGEventListener 
{

	@EventHandler(priority = EventPriority.LOWEST)
	public void checkBlockDamage(BlockDamageEvent event)
	{
		// This event does not produce a check, it only sets the data for the SGPlayer instance.
		if (safeGuard.sgPlayerManager.isTracking(event.getPlayer())) {
			safeGuard.sgPlayerManager.getPlayer(event.getPlayer().getName()).setLastBlockHitTime(System.currentTimeMillis());	
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void checkBlockBreak(BlockBreakEvent event)
	{
		// This event produces both data for a check and executes the actual checks.
		if (safeGuard.sgPlayerManager.isTracking(event.getPlayer())) {
			safeGuard.sgPlayerManager.getPlayer(event.getPlayer().getName()).setLastBlockBreakTime(System.currentTimeMillis());
			
			for(SGCheck sgCheck : sgChecks)
			{
				if (safeGuard.sgPlayerManager.isTracking(event.getPlayer())) {
					sgCheck.runCheck(event, safeGuard.sgPlayerManager.getPlayer(event.getPlayer().getName()));	
				}
			}
		}
	}
	
	@Override
	public void loadChecks()
	{
		sgChecks.add(new SGCheckReach());
		sgChecks.add(new SGCheckSpeed());
	}
}
