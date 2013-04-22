package com.xdrapor.safeguard.event.movement;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import com.xdrapor.safeguard.checks.SGCheck;
import com.xdrapor.safeguard.checks.movement.SGCheckFall;
import com.xdrapor.safeguard.checks.movement.SGCheckFlight;
import com.xdrapor.safeguard.checks.movement.SGCheckInvalidMove;
import com.xdrapor.safeguard.checks.movement.SGCheckPackets;
import com.xdrapor.safeguard.checks.movement.SGCheckSpeed;
import com.xdrapor.safeguard.event.SGEventListener;
import com.xdrapor.safeguard.utilities.SGBlockUtil;
import com.xdrapor.safeguard.utilities.SGMovementUtil;

public class SGEventMovement extends SGEventListener {	
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void checkPlayerDeath(PlayerDeathEvent event) {
		// This event does not produce a check, it only sets the data for the SGPlayer instance.
		if (safeGuard.sgPlayerManager.isTracking(event.getEntity())) {
			SGMovementUtil.setSafeLocationSpawn(event.getEntity());
			safeGuard.sgPlayerManager.getPlayer(event.getEntity().getName()).resetFallingValues();
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void checkPlayerMovement(PlayerMoveEvent event) {
		
		// TODO: The cause of the NPE on join in air.
		if (safeGuard.sgPlayerManager.getPlayer(event.getPlayer().getName()).getSafeLocation() == null) {
			safeGuard.sgPlayerManager.getPlayer(event.getPlayer().getName()).setSafeLocation(SGBlockUtil.findClosestGroundToLocation(event.getPlayer()));
		}
		
		for(SGCheck sgCheck : sgChecks) {
			if (safeGuard.sgPlayerManager.isTracking(event.getPlayer())) {
				sgCheck.runCheck(event, safeGuard.sgPlayerManager.getPlayer(event.getPlayer().getName()));
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void checkToggleFlight(PlayerToggleFlightEvent event) {
		if(!event.isFlying()) {
			if (safeGuard.sgPlayerManager.isTracking(event.getPlayer())) {
				safeGuard.sgPlayerManager.getPlayer(event.getPlayer().getName()).setFlightStateTime(System.currentTimeMillis());
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void checkGamemodeChange(PlayerGameModeChangeEvent event) {
		if(event.getPlayer().getGameMode() == GameMode.CREATIVE) {
			if (safeGuard.sgPlayerManager.isTracking(event.getPlayer())) {
				safeGuard.sgPlayerManager.getPlayer(event.getPlayer().getName()).setFlightStateTime(System.currentTimeMillis());
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void checkPlayerTeleport(PlayerTeleportEvent event) {
		// This event does not produce a check, it only sets the data for the SGPlayer instance.
		if (safeGuard.sgPlayerManager.isTracking(event.getPlayer())) {
			
			if(event.getCause().equals(TeleportCause.UNKNOWN)) {
				if(event.getFrom().getBlock().getType().equals(Material.BED_BLOCK)) {
					safeGuard.sgPlayerManager.getPlayer(event.getPlayer().getName()).setSafeLocation(event.getTo());
				}
				
				return;
			}
			
			safeGuard.sgPlayerManager.getPlayer(event.getPlayer().getName()).setSafeLocation(event.getTo());
		}
	}

	@Override
	public void loadChecks() {
		sgChecks.add(new SGCheckFall());
		sgChecks.add(new SGCheckFlight());
		sgChecks.add(new SGCheckSpeed());
		sgChecks.add(new SGCheckInvalidMove());
		sgChecks.add(new SGCheckPackets());
	}
}
