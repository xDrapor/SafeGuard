package com.xdrapor.safeguard.checks.movement;

import org.bukkit.craftbukkit.v1_5_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import com.xdrapor.safeguard.checks.SGCheck;
import com.xdrapor.safeguard.core.permissions.SGPermissibleNodes;
import com.xdrapor.safeguard.player.SGPlayer;
import com.xdrapor.safeguard.utilities.SGCheckTag;
import com.xdrapor.safeguard.utilities.SGMovementUtil;

/**
 * Handles flight checks for SafeGuard.
 * 
 * @author xDrapor
 * @author IchBinMude
 *
 */
public class SGCheckFlight extends SGCheck 
{

	@Override
	public String getDescription() {
		return "Prevents unauthorized flight";
	}

	@Override
	public void runCheck(Event event, SGPlayer player)  {
		
		if(player == null || event == null)return;
		if(sgPermissions.hasPermission(player, SGPermissibleNodes.MOVEMENT_FLIGHT) || !sgConfig.isCheckEnabled(this) || isCreativeFlight(player.getPlayer()))return;
	
		PlayerMoveEvent playerMoveEvent = (PlayerMoveEvent)event;
		Player sgPlayer = player.getPlayer();
		
		this.to = playerMoveEvent.getTo();
		this.from = playerMoveEvent.getFrom();

		if(onGround(sgPlayer) || inLiquid(sgPlayer) || player.isOnLily()) {
			SGMovementUtil.setSafeLocation(sgPlayer);
			safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).setNextExpectedY(-1.0D);
			
			return;
		}

		// Handle going over height level.
		if (sgPlayer.getLocation().getY() >= sgPlayer.getWorld().getMaxHeight() - 0.75D) { playerMoveEvent.setTo(SGMovementUtil.getSafeLocation(sgPlayer)); }
		
		// EXTREMELY strict. Legitimate players will have no problems at all.
		if (!onGround(sgPlayer)) {
						
			if (safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).getNextExpectedY() == -1) {
				safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).setNextExpectedY(sgPlayer.getLocation().getY() + sgPlayer.getVelocity().getY());
				return;
			}
				
			// This buffer of 0.60D allows for jumping/edge detection/legitimate bunny hopping down hill sides/etc.
			if (Math.abs(sgPlayer.getLocation().getY() - safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).getNextExpectedY()) >= 0.60D) {
				
				safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).addVL(SGCheckTag.MOVEMENT_VERTICAL, Math.abs(sgPlayer.getVelocity().getY()) * 50);
				safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).addVL(SGCheckTag.MOVEMENT_DISTANCE, Math.abs(sgPlayer.getVelocity().getY()) * 50);
				
				publishCheck(getClass(), sgPlayer, SGCheckTag.MOVEMENT_VERTICAL);
				publishCheck(getClass(), sgPlayer, SGCheckTag.MOVEMENT_DISTANCE);
				
				if (safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()) == null) { return; }
				
				playerMoveEvent.setTo(SGMovementUtil.getSafeLocation(sgPlayer));
			}
			
			safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).setNextExpectedY(sgPlayer.getLocation().getY() + sgPlayer.getVelocity().getY());
			
			return;
		}
		
		if((SGMovementUtil.getSafeFalling(sgPlayer) && SGMovementUtil.getSafeDistanceX(sgPlayer, true) < 6 && SGMovementUtil.getSafeDistanceZ(sgPlayer, true) < 6) || (SGMovementUtil.getSafeDistanceVertical(sgPlayer) < ((double)getJumpAmplifier(((CraftPlayer)sgPlayer).getHandle()) * 1.25) && SGMovementUtil.getSafeDistanceHorizontal(sgPlayer) < 5)) {
			safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).reduceVL(SGCheckTag.MOVEMENT_VERTICAL);
			safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).reduceVL(SGCheckTag.MOVEMENT_DISTANCE);
			
			return;
		}

		if(SGMovementUtil.getSafeDistanceHorizontal(sgPlayer) > sgConfig.getConfig().getDouble("checks.movement_flight.buffer")) {
			safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).addVL(SGCheckTag.MOVEMENT_DISTANCE, SGMovementUtil.getSafeDistanceHorizontal(sgPlayer));

			publishCheck(getClass(), sgPlayer, SGCheckTag.MOVEMENT_DISTANCE);
			
			playerMoveEvent.setTo(SGMovementUtil.getSafeLocation(sgPlayer));
			
			return;
		}
	}
}