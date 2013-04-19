package com.xdrapor.safeguard.core.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import com.xdrapor.safeguard.core.ICore;

/** Construct a new SGLogFormatter. */
public class SGLogFormatter extends Formatter implements ICore {
	
	@Override
	public String format(LogRecord logRecord) {
		return new StringBuilder().append("<").append(logRecord.getLevel()).append('|').append(safeGuard.sgLogManager.getTimeStamp(sgDateFormat + ' ' + sgTimeFormat)).append("> ").append(logRecord.getMessage()).append(sgLineSeparator).toString();
	}
}
