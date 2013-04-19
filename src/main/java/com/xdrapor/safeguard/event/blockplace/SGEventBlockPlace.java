package com.xdrapor.safeguard.event.blockplace;

import com.xdrapor.safeguard.checks.blockplace.SGCheckReach;
import com.xdrapor.safeguard.checks.blockplace.SGCheckSpeed;
import com.xdrapor.safeguard.event.SGEventListener;

public class SGEventBlockPlace extends SGEventListener {

	@Override
	public void loadChecks() {
		sgChecks.add(new SGCheckReach());
		sgChecks.add(new SGCheckSpeed());
	}

}
