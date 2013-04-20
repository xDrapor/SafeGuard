package com.xdrapor.safeguard.checks.movement;

import net.minecraft.server.v1_5_R2.IBlockAccess;

import org.bukkit.craftbukkit.v1_5_R2.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import com.xdrapor.safeguard.checks.SGCheck;
import com.xdrapor.safeguard.core.permissions.SGPermissibleNodes;
import com.xdrapor.safeguard.player.SGPlayer;
import com.xdrapor.safeguard.utilities.SGBlockUtil;
import com.xdrapor.safeguard.utilities.SGCheckTag;
import com.xdrapor.safeguard.utilities.SGMovementUtil;

public class SGCheckInvalidMove extends SGCheck {

	@Override
	public String getDescription() {
		return "Disallows invalid/illegal moves. (Sprinting backwards, standing still and sprinting, etc)";
	}

	@Override
	public void runCheck(Event event, SGPlayer player) {

		PlayerMoveEvent playerMoveEvent = (PlayerMoveEvent)event;

		Player sgPlayer = player.getPlayer();

		IBlockAccess iBlockAccess = ((CraftWorld)sgPlayer.getWorld()).getHandle();

		this.to = playerMoveEvent.getTo();
		this.from = playerMoveEvent.getFrom();

		if(sgPermissions.hasPermission(player, SGPermissibleNodes.MOVEMENT_INVALID) || !sgConfig.isCheckEnabled(this))return;

		//TODO: Allow moving out of blocks if head is free
		if(!SGBlockUtil.isPassable(sgPlayer, iBlockAccess, to.getBlockX(), to.getBlockY(), to.getBlockZ(), to.getBlock().getTypeId())){
			safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).addVL(SGCheckTag.MOVEMENT_INVALID, 20.0D);

			publishCheck(getClass(), sgPlayer, SGCheckTag.MOVEMENT_INVALID);

			playerMoveEvent.setTo(this.from);

			return;
		}

		if(isSprinting(sgPlayer)) {
			if(SGMovementUtil.getDistanceHorizontal(this.to, this.from) == 0.0 || player.isBackPedalling(to)) {
				safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).addVL(SGCheckTag.MOVEMENT_INVALID, 20.0D);

				publishCheck(getClass(), sgPlayer, SGCheckTag.MOVEMENT_INVALID);

				playerMoveEvent.setTo(this.from);

				return;
			}

			//Player did nothing wrong, reward them.
			safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).reduceVL(SGCheckTag.MOVEMENT_INVALID);
		}
	}
}