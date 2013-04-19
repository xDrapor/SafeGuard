package com.xdrapor.safeguard.event.server;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.xdrapor.safeguard.event.SGEventListener;
import com.xdrapor.safeguard.player.SGPlayer;

public class SGEventPlayerStatus extends SGEventListener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onConnecting(PlayerJoinEvent event)
	{
		if (event.getPlayer() == null) { return; }
		
		if(!safeGuard.sgPlayerManager.getPlayers().containsKey(event.getPlayer().getName())) {
			safeGuard.sgPlayerManager.addPlayer(event.getPlayer().getName(), new SGPlayer(event.getPlayer().getName()));
			safeGuard.sgLogManager.getConsoleLogger().logInfo("Player" + sgStringSeparator + event.getPlayer().getDisplayName() + sgStringSeparator + "is now being tracked.");
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onDisconnecting(PlayerQuitEvent event)
	{
		if (event.getPlayer() == null) { return; }
		
		if(safeGuard.sgPlayerManager.getPlayers().containsKey(event.getPlayer().getName())) {
			// TODO: What to do. (The SGCheck VL the player was kicked for needs reset on kick(?) otherwise they can't rejoin the server.)
			safeGuard.sgPlayerManager.removePlayer(event.getPlayer().getName());
			safeGuard.sgLogManager.getConsoleLogger().logInfo("Player" + sgStringSeparator + event.getPlayer().getDisplayName() + sgStringSeparator + "is no longer being tracked. (Player Disconnected)");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onKick(PlayerKickEvent event)
	{
		if (event.getPlayer() == null) { return; }
		
		if(safeGuard.sgPlayerManager.getPlayers().containsKey(event.getPlayer().getName())) {
			// TODO: What to do. (The SGCheck VL the player was kicked for needs reset on kick(?) otherwise they can't rejoin the server.)
			safeGuard.sgPlayerManager.removePlayer(event.getPlayer().getName());
			safeGuard.sgLogManager.getConsoleLogger().logInfo("Player" + sgStringSeparator + event.getPlayer().getDisplayName() + sgStringSeparator + "is no longer being tracked. (Player Kicked by SafeGuard)");
		}
	}
	
	@Override
	public void loadChecks() {
		
	}
}
