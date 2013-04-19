package com.xdrapor.safeguard.core.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.xdrapor.safeguard.core.ICore;

/**
 * An instance of Logger used by SafeGuard.
 * 
 * @author IchBinMude
 *
 */
public class SGLogger implements ILog, ICore
{
	
	/** The Logger instance. */
	private Logger logger;
	
	/** Construct a new Logger instance or return the instance of the named Logger. */
	public SGLogger(String name) 
	{
		logger = Logger.getLogger(name);
	}
	
	/** Whether Level.INFO logging is enabled. */
	public boolean isInfoEnabled() 
	{
		return logger.isLoggable(Level.INFO);
	}

	/** Whether Level.WARNING logging is enabled. */
	public boolean isWarnEnabled() 
	{
		return logger.isLoggable(Level.WARNING);
	}

	/** Whether Level.SEVERE logging is enabled. */
	public boolean isSevereEnabled() 
	{
		return logger.isLoggable(Level.SEVERE);
	}

	/** Whether Level.FINE logging is enabled. */
	public boolean isDebugEnabled() 
	{
		return logger.isLoggable(Level.FINE);
	}

	/** Logs a Level.INFO message to the logger. */
	public void logInfo(String message)
	{
		log(Level.INFO, message, null);
	}

	/** Logs a Level.INFO error message to the logger. */
	public void logInfo(String message, Throwable t)
	{
		log(Level.INFO, message, t);
	}

	/** Logs a Level.WARNING message to the logger. */
	public void logWarn(String message)
	{
		log(Level.WARNING, message, null);
	}

	/** Logs a Level.WARNING error message to the logger. */
	public void logWarn(String message, Throwable t)
	{
		log(Level.WARNING, message, t);
	}

	/** Logs a Level.SEVERE message to the logger. */
	public void logSevere(String message)
	{
		log(Level.SEVERE, message, null);
	}

	/** Logs a Level.SEVERE error message to the logger. */
	public void logSevere(String message, Throwable t)
	{
		log(Level.SEVERE, message, t);
	}

	/** Logs a Level.FINE message to the logger. */
	public void logDebug(String message) 
	{
		log(Level.FINE, message, null);
	}

	/** Logs a Level.FINE error message to the logger. */
	public void logDebug(String message, Throwable t)
	{
		log(Level.FINE, message, t);
	}
	
	/** Log the actual message. */
	public void log(Level level, String message, Throwable exception)
	{
		if (this.logger.isLoggable(level)) 
		{
			if (exception != null) {
				this.logger.log(level, "[" + sgPrefix + "]" + sgStringSeparator + message, exception);
			} 
			else 
			{
				this.logger.log(level, "[" + sgPrefix + "]" + sgStringSeparator + message);
			}
		}
	}
	
	/** Returns the logger for this SGLogger instance. */
	public Logger getLogger() 
	{
		return logger;
	}
	
	/** Returns an instance of SGLogger. */
	public static ILog getInstance(String name) 
	{
		return new SGLogger(name);
	}
}
