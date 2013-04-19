package com.xdrapor.safeguard.core.logging;

/**
 * An exception that is thrown due to logging activities.
 * 
 * @author IchBinMude
 *
 */
public class SGLogConfigurationException extends Exception 
{
	private static final long serialVersionUID = -5540492155425063187L;

	/** The Exception that caused this SGLogConfigurationException. */
    protected Throwable cause;

    /** Construct a new SGLogConfigurationException. */
    public SGLogConfigurationException()
    {
    	this.cause = null;
    }
    
    /** Construct a new SGLogConfigurationException with the given message. */
    public SGLogConfigurationException(String message) 
    {
    	super(message);
    }

    /** Construct a new SGLogConfigurationException based on the given Exception. */
    public SGLogConfigurationException(Throwable throwable)
    {
    	this(throwable, ((throwable == null) ? null : throwable.toString()));
    }
    
    /** Construct a new SGLogConfigurationException based on the given Exception with the given message. */
    public SGLogConfigurationException(Throwable cause, String message) 
    {
    	super(message);
    	this.cause = cause;
    }

    /** Returns the cause of this EventException. */
    public Throwable getCause()
    {
    	return cause;
    }
}