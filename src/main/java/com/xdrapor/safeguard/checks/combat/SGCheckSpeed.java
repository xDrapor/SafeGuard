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
		
		
		//TODO: Begin long term tracking for constant speeds with no variation.
		if(player == null || event == null)return;
		if(sgPermissions.hasPermission(player, SGPermissibleNodes.COMBAT_SPEED) || !sgConfig.isCheckEnabled(this))return;
		
		EntityDamageByEntityEvent eDBeEvent = (EntityDamageByEntityEvent)event;
		Player sgPlayer = (Player)eDBeEvent.getDamager();
	
		if (System.currentTimeMillis() - player.getLastHitTime() < (1000 / sgConfig.getConfig().getInt("checks.combat_speed.maxhits"))) {
			player.addVL(SGCheckTag.COMBAT_SPEED, System.currentTimeMillis() - safeGuard.sgPlayerManager.getPlayer(sgPlayer.getPlayer().getName()).getLastHitTime());
			player.setLastHitTime(System.currentTimeMillis());
			if (player.getVL(SGCheckTag.COMBAT_SPEED) > sgConfig.getConfig().getDouble("checks.combat_speed.buffer")) {
				
				publishCheck(getClass(), sgPlayer, SGCheckTag.COMBAT_SPEED);
				
				eDBeEvent.setCancelled(true);				
				
				return;
			}
		} else {
			
			player.setLastHitTime(System.currentTimeMillis());
			player.reduceVL(SGCheckTag.COMBAT_SPEED);
			
			return;
		}
	}
}