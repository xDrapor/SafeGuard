package com.xdrapor.safeguard.core.command;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.xdrapor.safeguard.core.ICore;

public abstract class SGCommand implements ICore 
{

	/** The sender of this command. */
	protected CommandSender sender;
	
	/** The name of this command. */
	protected String name;
	
	/** The usage of this command. */
	protected String usage;
	
	/** The description used for help. */
	protected String description;
	
	/** The permission node needed to access this command. */
	protected String permission;
	
	/** Whether the console can execute this command. */
	protected boolean consoleExecute = false;
	
	/** The number of arguments this command accepts. */
	protected int argumentCount;
	
	/** The passed arguments for this command. */
	protected ArrayList<String> arguments = new ArrayList<String>();
	
	/** The Method called to parse this command (Called by the SGCommandManager instance). */
	public boolean parse(CommandSender sender, String commandLabel, String args[]) {
		
		if (!consoleExecute && !(sender instanceof Player)) {
			safeGuard.sgLogManager.getConsoleLogger().logSevere("You must be a player to execute this command.");
			return false;
		}

		// TODO: Set all permissions for commands.
		//if (!sender.hasPermission(this.permission)) { sendChatMessage(this.sender, ChatColor.RED + "You do not have permission to use this command."); }
		
		this.sender = sender;
		
		this.arguments.clear();
		
		for (String argument : args) { this.arguments.add(argument); }

		if (this.arguments.contains(this.name)) { this.arguments.remove(this.name); }
		
		if (arguments.size() > this.argumentCount) {		
			this.sendUsage();
			return true;
		}
		
		return execute();
	}
	
	/** The Method called after parsing an incoming command. */
	public abstract boolean execute();
	
	/** Returns the name of this command. */
	public String getName() {
		return name;
	}

	/** Returns the usage of this command. */
	public String getUsage() {
		return usage;
	}

	/** Returns the description for this command used in Help. */
	public String getDescription() {
		return description;
	}
	
	/** Sends the usage of this command to the CommandSender. */
	public void sendUsage()
	{
		sendChatMessage(this.sender, "Usage: " + this.usage);
	}
	
	/** Sends a message to the CommandSender of a command. */
	public static void sendChatMessage(CommandSender sender, String message)
	{
		for (String line : message.split("\n"))
		{
			sender.sendMessage(line);
		}
	}
}
