package com.xdrapor.safeguard.core.command;

import org.bukkit.ChatColor;

import com.xdrapor.safeguard.utilities.SGCheckTag;

public class SGCommandInfo extends SGCommand {

	public SGCommandInfo() {
		this.name = "info";
		this.argumentCount = 1;
		this.usage = new StringBuilder().append(ChatColor.GOLD).append("/safeguard info <player>").toString();
		this.description = "Displays the current violations of a player.";
	}

	@Override
	public boolean execute() {
		
		if (this.arguments.size() == 0) { this.sendUsage(); return true; }
		
		StringBuilder sgInfoMessage = new StringBuilder()
			.append(ChatColor.YELLOW).append("--------- ").append(ChatColor.DARK_BLUE).append(sgPrefix).append(ChatColor.RESET).append(" Violations").append(ChatColor.YELLOW).append(" -------------------\n")
			.append(ChatColor.GREEN).append(this.arguments.get(0)).append("\n").append(ChatColor.RESET);
		
		if (safeGuard.sgPlayerManager.getPlayers().containsKey(this.arguments.get(0))) {

			for (SGCheckTag tag : SGCheckTag.values()) {
				sgInfoMessage.append(ChatColor.GRAY).append(tag.toString()).append(": ").append(ChatColor.DARK_RED).append(safeGuard.sgPlayerManager.getPlayer(this.arguments.get(0)).getVLTruncated(tag)).append("\n");
			}
			
			sendChatMessage(this.sender, sgInfoMessage.toString());
			
		} else {
			sendChatMessage(this.sender, ChatColor.RED + "The player " + this.arguments.get(0) + " cannot be found!");
		}
		
		return true;
	}
}
