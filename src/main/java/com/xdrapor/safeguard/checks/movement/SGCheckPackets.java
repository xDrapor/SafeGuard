package com.xdrapor.safeguard.checks.movement;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import com.xdrapor.safeguard.checks.SGCheck;
import com.xdrapor.safeguard.core.permissions.SGPermissibleNodes;
import com.xdrapor.safeguard.player.SGPlayer;
import com.xdrapor.safeguard.utilities.SGCheckTag;
import com.xdrapor.safeguard.utilities.SGMovementUtil;

public class SGCheckPackets extends SGCheck{

	private static final long comparator = 1/22;

	@Override
	public String getDescription() {
		return "Dissallows players from sending too many MovementPackets to bypass speed checks.";
	}

	@Override
	public void runCheck(Event event, SGPlayer player) {
		Player sgPlayer = player.getPlayer();
		PlayerMoveEvent evt = (PlayerMoveEvent)event;
		
		
		if(sgPermissions.hasPermission(player, SGPermissibleNodes.MOVEMENT_MOREPACKETS) || !sgConfig.isCheckEnabled(this))return;

		
		if(SGMovementUtil.getSafeLocation(player.getPlayer()) == null)
			SGMovementUtil.setSafeLocation(player.getPlayer());

		//Get System time
		long time = System.currentTimeMillis();
		
		//If the player's packet difference is less than
		//that of the legal amount, they've failed the check
		if(isTooMuch(time - player.getLastPacketTime())) {
			player.addVL(SGCheckTag.MOVEMENT_MOREPACKETS, packetDifference(player.getLastPacketTime()));
			player.setLastPacketTime(time);
			evt.setTo(player.getSafeLocation());
			publishCheck(getClass(), sgPlayer, SGCheckTag.MOVEMENT_MOREPACKETS);
			return;
		}else {
			player.reduceVL(SGCheckTag.MOVEMENT_MOREPACKETS);
			player.setLastPacketTime(time);
			return;
		}
		
	}
	
	private static boolean isTooMuch(long playerDif) {
		return (playerDif < comparator);
	}
	
	private static double packetDifference(long playerPacket) {
		return (double)System.currentTimeMillis() - playerPacket;
	}
}
