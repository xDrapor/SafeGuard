package com.xdrapor.safeguard.core.permissions;

import com.xdrapor.safeguard.player.SGPlayer;

/**
 * Handles permissions for SafeGuard.
 * 
 * @author xDrapor
 * @author IchBinMude - I changed your return on hasPermission :D (so much cleaner).
 *
 */
public class SGPermissibles {

	/** Returns whether a player has permission to bypass a check. */
	public boolean hasPermission(SGPlayer sgPlayer, String permission) {
		return (sgPlayer.getPlayer().hasPermission(permission) || sgPlayer.getPlayer().isOp());
	}
}
