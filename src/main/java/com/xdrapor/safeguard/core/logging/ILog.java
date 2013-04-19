package com.xdrapor.safeguard.core.logging;

/**
 * A simple logging interface abstract to logging APIs.
 * 
 * @author IchBinMude
 * 
 */
public interface ILog {

	/** Whether Info message logging is enabled. */
	public boolean isInfoEnabled();
	
	/** Whether Warn message logging is enabled. */
	public boolean isWarnEnabled();
	
	/** Whether Severe message logging is enabled. */
	public boolean isSevereEnabled();
	
	/** Whether Debug message logging is enabled. */
	public boolean isDebugEnabled();
	
	/** Log an Info message. */
	public void logInfo(final String message);
	
	/** Log an Info error event. */
	public void logInfo(final String message, Throwable t);
	
	/** Log a Warn message. */
	public void logWarn(final String message);
	
	/** Log a Warn error event. */
	public void logWarn(final String message, Throwable t);
	
	/** Log a Severe message. */
	public void logSevere(final String message);
	
	/** Log a Severe error event. */
	public void logSevere(final String message, Throwable t);
	
	/** Log a Debug message. */
	public void logDebug(final String message);
	
	/** Log a Debug error event. */
	public void logDebug(final String message, Throwable t);
}