package com.xdrapor.safeguard.core.logging;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import com.xdrapor.safeguard.core.ICore;

/**
 * The log manager used by SafeGuard.
 * 
 * @author IchBinMude
 *
 */
public class SGLogManager implements ICore 
{
	
	/** The static instance of console logger used by SafeGuard. */
	public static ILog sgConsoleLogger;
	
	/** The static instance of file logger used by SafeGuard. */
	public static ILog sgFileLogger;
	
	/** Whether the file logger was setup correctly. */
	public boolean isFileLoggerSetup = false;
	
	/** The FileHandler used for file logging. */
	public FileHandler fileHandler;
	
	/** Construct a new SGLogManager instance. */
	public SGLogManager() 
	{
	
		sgConsoleLogger = SGLogger.getInstance("Minecraft");
		sgFileLogger	= SGLogger.getInstance("SafeGuard");
		
		try
		{
			
			this.isFileLoggerSetup = this.setupFileLogger();
			
		} 
		catch (SGLogConfigurationException e)
		{
			sgConsoleLogger.logSevere(e.getMessage(), e);
		}
	}

	/** Construct the file logger. */
	private boolean setupFileLogger() throws SGLogConfigurationException
	{
		
		Logger fileLogger = ((SGLogger)sgFileLogger).getLogger();

		try {

			if (!sgDirectory.exists()) {
				sgDirectory.mkdir();
			}
			
			if (!sgDirectoryLogs.exists())  {
				sgDirectoryLogs.mkdir();
			}
			
			fileHandler = new FileHandler(sgDirectoryLogs + File.separator + sgPrefix + "-" + this.getTimeStamp(sgDateFormat) + "-%g" + ".log", 1000000, 7, true);
			fileHandler.setFormatter(new SGLogFormatter());

			fileLogger.setUseParentHandlers(false);
			fileLogger.addHandler(fileHandler);
			
			return true;
			
		} catch (SecurityException e) {
			throw new SGLogConfigurationException("Failed to setup file logging. | " + e.getMessage());
		} catch (IOException e) {
			throw new SGLogConfigurationException("Failed to setup file logging. | " + e.getMessage());
		}
	}
	
	/** Returns the console logger instance used by SafeGuard. */
	public final ILog getConsoleLogger() 
	{
		return sgConsoleLogger;
	}
	
	/** Returns the file logger instance used by SafeGuard. */
	public final ILog getFileLogger() 
	{
		return sgFileLogger;
	}
	
	/** Returns a time-stamp used for log file generation. */
	public final String getTimeStamp(String format)
	{
		Calendar calendar = Calendar.getInstance();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		
		return simpleDateFormat.format(calendar.getTime());
	}
}