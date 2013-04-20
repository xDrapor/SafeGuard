
package com.xdrapor.safeguard.checks.blockbreak;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

import com.xdrapor.safeguard.checks.SGCheck;
import com.xdrapor.safeguard.core.permissions.SGPermissibleNodes;
import com.xdrapor.safeguard.player.SGPlayer;
import com.xdrapor.safeguard.utilities.SGCheckTag;

public class SGCheckFrequency extends SGCheck {

	@Override
	public String getDescription() {
		return "Checks creative mode break frequency";
	}

	@Override
	public void runCheck(Event event, SGPlayer player) {
		if(player == null || event == null)return;
		if(sgPermissions.hasPermission(player, SGPermissibleNodes.BLOCK_FREQUENCY) || !sgConfig.isCheckEnabled(this))return;

		BlockBreakEvent blockBreakEvent = (BlockBreakEvent)event;
		Player sgPlayer = player.getPlayer();

		if(isCreative(sgPlayer)) {
			if(System.currentTimeMillis() - player.getLastBlockBrokenFreq() > 1000) {
				player.setLastBlockBrokenFreq(System.currentTimeMillis());
				player.incrementBlocksFreq();
				if(player.getBlocksFreq() > sgConfig.getConfig().getInt("checks.blockbreak_frequency.maxbps")) {
					blockBreakEvent.setCancelled(true);

					sgPlayer.sendMessage(sgConfig.getConfig().getString("checks.blockbreak_frequency.pendingmsg").replace("%c", String.valueOf(sgConfig.getConfig().getDouble("checks.blockbreak_frequency.cooldown"))).replaceAll("(&([a-f0-9]))", "\u00A7$2"));

					player.addVL(SGCheckTag.BLOCK_BREAKFREQUENCY, player.getBlocksFreq());

					publishCheck(getClass(), sgPlayer, SGCheckTag.BLOCK_BREAKFREQUENCY);
				} else {
					player.resetBlocksFreq();
				}
			} else {
				player.incrementBlocksFreq();
				if(player.getBlocksFreq() > sgConfig.getConfig().getInt("checks.blockbreak_frequency.maxbps")) {
					blockBreakEvent.setCancelled(true);

					sgPlayer.sendMessage(sgConfig.getConfig().getString("checks.blockbreak_frequency.pendingmsg").replace("%c", String.valueOf(sgConfig.getConfig().getDouble("checks.blockbreak_frequency.cooldown"))).replaceAll("(&([a-f0-9]))", "\u00A7$2"));

					player.addVL(SGCheckTag.BLOCK_BREAKFREQUENCY, player.getBlocksFreq());

					publishCheck(getClass(), sgPlayer, SGCheckTag.BLOCK_BREAKFREQUENCY);
				}
			}

			if(System.currentTimeMillis() - player.getLastBlockBreakTime() > (sgConfig.getConfig().getDouble("checks.blockbreak_frequency.cooldown") * 1000)) {
				player.resetBlocksFreq();
				sgPlayer.sendMessage(sgConfig.getConfig().getString("checks.break_frequency.cooldownmsg").replaceAll("(&([a-f0-9]))", "\u00A7$2"));
			}
		}
	}
}
