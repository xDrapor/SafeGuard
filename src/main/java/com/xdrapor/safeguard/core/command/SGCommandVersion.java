package com.xdrapor.safeguard.core.command;

import org.bukkit.ChatColor;

public class SGCommandVersion extends SGCommand {

	public SGCommandVersion() {
		this.name = "version";
		this.argumentCount = 0;
		this.usage = new StringBuilder().append(ChatColor.GOLD).append("/safeguard version").toString();
		this.description = "Displays the current version of SafeGuard";
	}
	
	@Override
	public boolean execute() {
		
		sendChatMessage(this.sender, new StringBuilder().append("This server is running ").append(ChatColor.DARK_BLUE).append(sgPrefix).append(ChatColor.RESET).append(" version ").append(ChatColor.GRAY).append(ChatColor.ITALIC).append(sgVersion).toString());

		return true;
	}

}
