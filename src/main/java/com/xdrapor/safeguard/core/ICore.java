package com.xdrapor.safeguard.core;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;

import com.xdrapor.safeguard.SafeGuard;
import com.xdrapor.safeguard.event.SGEventListener;

/**
 * A simple core interface used by all SafeGuard.
 * 
 * @author IchBinMude
 * @author xDrapor
 */
public interface ICore 
{
	/** The static instance of SafeGuard. */
	public SafeGuard safeGuard = (SafeGuard)Bukkit.getServer().getPluginManager().getPlugin("SafeGuard");
	
	/** The prefix used by SafeGuard. */
	public final String sgPrefix = "SafeGuard";
	
	/** The current build of SafeGuard. */
	public final String sgVersion = safeGuard.getVersion();
	
	/** The string separator used by SafeGuard. */
	public final char sgStringSeparator = ' ';
	
	/** The date format used by SafeGuard. */
	public final String sgDateFormat = "MM-dd-yyyy";
	
	/** The time format used by SafeGuard. */
	public final String sgTimeFormat = "HH:mm:ss";
	
	/** The line separator used by the System. */
	public final String sgLineSeparator = System.getProperty("line.separator");
	
	/** The working directory of SafeGuard. */
	public File sgDirectory = safeGuard.getDataFolder();
	
	/** The logging directory used by SafeGuard. */
	public File sgDirectoryLogs = new File(safeGuard.getDataFolder() + File.separator + "logs");
	
	/** The registered listeners of SafeGuard. */
	public ArrayList<SGEventListener> systemListeners = new ArrayList<SGEventListener>();
}
