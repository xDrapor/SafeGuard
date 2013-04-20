package com.xdrapor.safeguard.core.configuration;

import org.bukkit.configuration.file.FileConfiguration;

import com.xdrapor.safeguard.checks.SGCheck;
import com.xdrapor.safeguard.core.ICore;

/**
 * Handles the configuration file for SafeGuard.
 * 
 * @author xDrapor
 *
 */
public class SGConfig implements ICore {
	
	/** Construct a new SGConfig instance. */
	public SGConfig() {
		saveConfig();
	}
	
	/** Saves the current configuration file. */
	public void saveConfig() {
		getConfig().options().copyDefaults(true);
		getConfig().options().header(sgPrefix + sgStringSeparator + sgVersion);
		getConfig().options().copyHeader(true);
		safeGuard.saveConfig();
    }

	/** Returns the configuration file. */
	public FileConfiguration getConfig() {
    	return safeGuard.getConfig();
    }

	/** Returns whether a check is enabled in the configuration file. */
	public boolean isCheckEnabled(SGCheck check) {
    	return getConfig().getBoolean("checks." + check.getClass().getPackage().getName().split("\\.")[4] + "_" + check.getClass().getSimpleName().replace("SG", "").replace("Check", "").toLowerCase() + ".enabled");
    }
}
