package com.xdrapor.safeguard.checks.blockplace;

import org.bukkit.event.Event;

import com.xdrapor.safeguard.checks.SGCheck;
import com.xdrapor.safeguard.player.SGPlayer;

public class SGCheckSpeed extends SGCheck {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void runCheck(Event event, SGPlayer player) {
		if(player == null || event == null)return;
	}
}
