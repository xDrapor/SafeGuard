package com.xdrapor.safeguard.event;

import java.util.ArrayList;

import org.bukkit.event.Listener;

import com.xdrapor.safeguard.checks.SGCheck;
import com.xdrapor.safeguard.core.ICore;

public abstract class SGEventListener implements Listener, ICore {

	/** An ArrayList of checks this listener performs. */
	public ArrayList<SGCheck> sgChecks = new ArrayList<SGCheck>();
	
	/** Load all the checks for the selected listener. */
	public abstract void loadChecks();
}
