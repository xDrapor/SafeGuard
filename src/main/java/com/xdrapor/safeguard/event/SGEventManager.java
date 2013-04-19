package com.xdrapor.safeguard.event;

import org.bukkit.Bukkit;

import com.xdrapor.safeguard.core.ICore;
import com.xdrapor.safeguard.event.blockbreak.SGEventBlockBreak;
import com.xdrapor.safeguard.event.combat.SGEventCombat;
import com.xdrapor.safeguard.event.movement.SGEventMovement;
import com.xdrapor.safeguard.event.server.SGEventPlayerStatus;

/**
 * Handles Event registration.
 * 
 * @author IchBinMude
 *
 */
public class SGEventManager  implements ICore {

	/** Construct a new SGEventManager instance. */
	public SGEventManager() {
		
		this.loadListeners();
		this.registerListeners();
	}
	
	/** Populates the list of SafeGuard Event listeners. */
	public void loadListeners()
	{
		systemListeners.add(new SGEventPlayerStatus());
		systemListeners.add(new SGEventMovement());
		systemListeners.add(new SGEventBlockBreak());
		systemListeners.add(new SGEventCombat());
	}

	/** Registers all SafeGuard Event listeners. */
	public void registerListeners()
	{
		for(SGEventListener listener : systemListeners)
		{
			listener.loadChecks();
			Bukkit.getPluginManager().registerEvents(listener, Bukkit.getPluginManager().getPlugin("SafeGuard"));
		}
	}

	/** Destroys all SafeGuard Event listeners. */
	public void cleanUpListeners()
	{
		systemListeners.clear();
	}
}
