package com.xdrapor.safeguard.core.command;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.xdrapor.safeguard.core.ICore;

/**
 * Handles commands for SafeGuard.
 * 
 * @author IchBinMude
 *
 */
public class SGCommandManager implements CommandExecutor, ICore {

	/** The commands of SafeGuard. */
	public ArrayList<SGCommand> sgCommands = new ArrayList<SGCommand>();
	
	/** Construct a new SGCommandManager instance. */
	public SGCommandManager() {
		loadCommands();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[])
	{
		
		if (cmd.getName().equalsIgnoreCase("safeguard")) {
			
			if (args.length == 0) { args = new String[]{"version"}; }
			
			for (SGCommand command : sgCommands) {
				if (command.getName().equalsIgnoreCase(args[0])) {
					return command.parse(sender, commandLabel, args);
				}
			}
		}
		
		return false;
	}

	/** Create all sub-command instances. */
	public void loadCommands()
	{
		this.sgCommands.add(new SGCommandVersion());
		this.sgCommands.add(new SGCommandHelp());
		this.sgCommands.add(new SGCommandInfo());
	}
}
