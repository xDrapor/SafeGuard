package com.xdrapor.safeguard.core.command;

import org.bukkit.ChatColor;

public class SGCommandHelp extends SGCommand {

	public SGCommandHelp() {
		this.name = "help";
		this.argumentCount = 1;
		this.usage = new StringBuilder().append(ChatColor.GOLD).append("/safeguard help").toString();
		this.description = "Displays help for SafeGuard commands.";
	}

	@Override
	public boolean execute() {
		
		// TODO: Recode with pagination.
		if (this.arguments.size() == 0)
		{
			StringBuilder sgHelpMessage = new StringBuilder()
				.append(ChatColor.YELLOW).append("--------- ").append(ChatColor.DARK_BLUE).append(sgPrefix).append(ChatColor.RESET).append(" Help: Index (1/1)").append(ChatColor.YELLOW).append(" ------------\n")
				.append(ChatColor.GRAY).append("Use /safeguard help <page> to get more help.\n").append(ChatColor.RESET);
			
			for (SGCommand command : safeGuard.sgCommandManager.sgCommands) {
				sgHelpMessage.append(command.getUsage()).append(": ").append(ChatColor.RESET).append(command.getDescription()).append("\n");
			}
			
			sendChatMessage(this.sender, sgHelpMessage.toString());
			
			return true;
			
		} else {
		
			return true;
		}
	}
}
