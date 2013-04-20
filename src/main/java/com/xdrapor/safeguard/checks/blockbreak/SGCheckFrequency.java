
package com.xdrapor.safeguard.checks.blockbreak;

import org.bukkit.event.Event;
import com.xdrapor.safeguard.checks.SGCheck;
import com.xdrapor.safeguard.player.SGPlayer;

public class SGCheckFrequency extends SGCheck {

	@Override
	public String getDescription() {
		return "Checks creative mode break frequency";
	}

	@Override
	public void runCheck(Event event, SGPlayer player) {

		if(player == null || event == null)return;
	}

}
