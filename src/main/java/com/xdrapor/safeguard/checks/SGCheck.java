package com.xdrapor.safeguard.checks;

import net.minecraft.server.v1_5_R2.EntityPlayer;
import net.minecraft.server.v1_5_R2.MobEffectList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.xdrapor.safeguard.core.ICore;
import com.xdrapor.safeguard.core.configuration.SGConfig;
import com.xdrapor.safeguard.core.permissions.SGPermissibleNodes;
import com.xdrapor.safeguard.core.permissions.SGPermissibles;
import com.xdrapor.safeguard.player.SGPlayer;
import com.xdrapor.safeguard.utilities.SGBlockUtil;
import com.xdrapor.safeguard.utilities.SGCheckTag;

public abstract class SGCheck implements ICore {

	protected SGPermissibles sgPermissions = new SGPermissibles();
	protected SGConfig sgConfig = new SGConfig();

	/** The description of the check. */
	public abstract String getDescription();

	/** The Locations sent by the event. */
	protected Location to;
	protected Location from;

	/** The Method executed when our event is dispatched. */
	public abstract void runCheck(Event evt, SGPlayer player);

	// CREATE STATIC METHODS BELOW THAT WILL/COULD BE USED BY ALL SGCHECK INSTANCES THAT PERTAIN TO PLAYER.
	public static void publishCheck(Class<?> clazz, Player sgPlayer, SGCheckTag tag) {

		for(Player player : safeGuard.getServer().getOnlinePlayers()) {
			if (safeGuard.sgPermissions.hasPermission(safeGuard.sgPlayerManager.getPlayer(player.getName()), SGPermissibleNodes.INFO_ALERTS)) {
				player.sendMessage(getChatMessage(sgPlayer, tag));
			}
		}
		if (safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()) == null || safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).getVL(tag) <= 0) { return; }

		if (safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).getVL(tag) <= 10) {

			safeGuard.sgLogManager.getConsoleLogger().logInfo	(getLogMessage(sgPlayer, tag));
			safeGuard.sgLogManager.getFileLogger().logInfo		(getLogMessage(sgPlayer, tag));

		} else if (safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).getVL(tag) <= 20) {

			safeGuard.sgLogManager.getConsoleLogger().logWarn	(getLogMessage(sgPlayer, tag));
			safeGuard.sgLogManager.getFileLogger().logWarn		(getLogMessage(sgPlayer, tag));

		} else if (safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).getVL(tag) >= safeGuard.sgConfig.getConfig().getDouble("checks." + clazz.getPackage().getName().split("\\.")[4].toLowerCase() + "_" + clazz.getSimpleName().replace("SGCheck", "").toLowerCase() + ".maxvl")){

			safeGuard.sgLogManager.getConsoleLogger().logSevere (getLogMessage(sgPlayer, tag));
			safeGuard.sgLogManager.getFileLogger().logSevere	(getLogMessage(sgPlayer, tag));

			if(safeGuard.sgConfig.getConfig().getBoolean("checks." + clazz.getPackage().getName().split("\\.")[4].toLowerCase() + "_" + clazz.getSimpleName().replace("SGCheck", "").toLowerCase() + ".kick")) {
				Bukkit.broadcastMessage(sgPlayer.getDisplayName() + " was KICKED for " + tag.toString() + " violations.");
				sgPlayer.getPlayer().kickPlayer(safeGuard.sgConfig.getConfig().getString("checks." + clazz.getPackage().getName().split("\\.")[4].toLowerCase() + "_" + clazz.getSimpleName().replace("SGCheck", "").toLowerCase()  + ".kickmsg").replaceAll("(&([a-f0-9]))", "\u00A7$2"));
			}
		}
	}

	/** Returns the message to be logged to chat. */
	public static String getChatMessage(Player sgPlayer, SGCheckTag tag) {
		return new StringBuilder().append(ChatColor.DARK_BLUE).append("[SG]").append(ChatColor.RESET).append(' ')
				.append('(').append(ChatColor.GRAY).append(ChatColor.ITALIC).append(tag.toString()).append(ChatColor.RESET).append(')').append(' ')
				.append(ChatColor.GREEN).append(sgPlayer.getName()).append("'s").append(' ')
				.append(ChatColor.RESET).append("current VL:").append(' ')
				.append(ChatColor.DARK_RED).append(safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).getVLTruncated(tag)).append(ChatColor.RESET).append('.').toString();
	}

	/** Returns the message to be logged to console or file. */
	public static String getLogMessage(Player sgPlayer, SGCheckTag tag) {
		return new StringBuilder().append('(').append(tag.toString()).append(')').append(' ')
				.append(sgPlayer.getName()).append("'s").append(' ')
				.append("current VL:").append(' ')
				.append(safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).getVLTruncated(tag)).append('.').toString();
	}

	/** Returns the player speed. */
	public static double getPlayerSpeed(Player sgPlayer) { 
		if(onIce(sgPlayer) || !onGround(sgPlayer)) {
			return 2.5D;
		} else if(sgPlayer.isSneaking()) {
			return 0.16D;
		} else if(sgPlayer.isSprinting() && sgPlayer.getFoodLevel() > 5) {
			return  0.36D;
		} else if(sgPlayer.isBlocking()) {
			return 0.18D;
		} else if(inLiquid(sgPlayer)) {
			return 0.14D;
		} else if(isOnLadder(sgPlayer)) {
			return 0.18; 
		} else {
			return 0.25D;
		}
	}

	/** Returns the player reach distance. */
	public static double getReachDistance(Player sgPlayer) {
		return (sgPlayer.getGameMode() == GameMode.CREATIVE ? 7.5 : 6.5);
	}

	/** Returns the player speed amplifier. */
	public static float getSpeedAmplifier(EntityPlayer sgPlayerHandle) {
		return (sgPlayerHandle.hasEffect(MobEffectList.FASTER_MOVEMENT) ? (1.0F + 0.2F * (float) (sgPlayerHandle.getEffect(MobEffectList.FASTER_MOVEMENT).getAmplifier() + 1)) : 1.0F);
	}

	/** Returns the player jump amplifier. */
	public static float getJumpAmplifier(EntityPlayer sgPlayerHandle)	{
		return (sgPlayerHandle.hasEffect(MobEffectList.JUMP) ? (sgPlayerHandle.getEffect(MobEffectList.JUMP).getAmplifier() > 20 ? (1.5F * (float) (sgPlayerHandle.getEffect(MobEffectList.JUMP).getAmplifier() + 1)) : (1.2F * (float) (sgPlayerHandle.getEffect(MobEffectList.JUMP).getAmplifier() + 1))) : 1.0F);
	}

	/** Returns whether the player is in creative mode. */
	public static boolean isCreative(Player sgPlayer) {
		return (sgPlayer.getGameMode() == GameMode.CREATIVE || sgPlayer.getAllowFlight());
	}

	/** Returns whether the player is sprinting. */
	public static boolean isSprinting(Player sgPlayer) {
		return sgPlayer.isSprinting();
	}

	/** Returns whether the player is dead. */
	public static boolean isDead(Player sgPlayer) {
		return (sgPlayer.getHealth() <= 0 || sgPlayer.isDead());
	}

	/** Returns whether the player is on ice. */
	public static boolean onIce(Player sgPlayer) {
		return (sgPlayer.getEyeLocation().subtract(0,1.85,0).getBlock().getType().equals(Material.ICE) ? true : false);
	}

	/** Returns whether the player is in liquid. */
	public static boolean inLiquid(Player sgPlayer) {
		return (sgPlayer.getEyeLocation().subtract(0,1.85,0).getBlock().isLiquid() ? true : false);
	}

	/** Returns whether the player is on the ground. */
	public static boolean onGround(Player sgPlayer) {
		return ((sgPlayer.getLocation().subtract(0, 0.20D, 0).getBlock().isEmpty() && !isAboveStairs(sgPlayer) && !isOnFence(sgPlayer) && !isOnSnow(sgPlayer)) ? false : true);
	}

	/** Returns whether the player is above stairs. */
	public static boolean isAboveStairs(Player sgPlayer){
		final Block block = sgPlayer.getLocation().getBlock();
		final Block altBlock = sgPlayer.getLocation().add(0,0.5,0).getBlock();
		return SGBlockUtil.isStair(block.getRelative(BlockFace.NORTH))
				|| SGBlockUtil.isStair(block.getRelative(BlockFace.SOUTH))
				|| SGBlockUtil.isStair(block.getRelative(BlockFace.EAST))
				|| SGBlockUtil.isStair(block.getRelative(BlockFace.WEST))
				|| SGBlockUtil.isStair(block.getRelative(BlockFace.SOUTH_WEST))
				|| SGBlockUtil.isStair(block.getRelative(BlockFace.NORTH_WEST))
				|| SGBlockUtil.isStair(block.getRelative(BlockFace.SOUTH_EAST))
				|| SGBlockUtil.isStair(block.getRelative(BlockFace.NORTH_EAST))
				|| SGBlockUtil.isStair(altBlock.getRelative(BlockFace.NORTH))
				|| SGBlockUtil.isStair(altBlock.getRelative(BlockFace.SOUTH))
				|| SGBlockUtil.isStair(altBlock.getRelative(BlockFace.EAST))
				|| SGBlockUtil.isStair(altBlock.getRelative(BlockFace.WEST))
				|| SGBlockUtil.isStair(altBlock.getRelative(BlockFace.SOUTH_WEST))
				|| SGBlockUtil.isStair(altBlock.getRelative(BlockFace.NORTH_WEST))
				|| SGBlockUtil.isStair(altBlock.getRelative(BlockFace.SOUTH_EAST))
				|| SGBlockUtil.isStair(altBlock.getRelative(BlockFace.NORTH_EAST));
	}

	/** Returns whether the player is above stairs. */
	public static boolean isOnLadder(Player sgPlayer){
		final Block block = sgPlayer.getLocation().getBlock();
		final Block altBlock = sgPlayer.getLocation().add(0,0.5,0).getBlock();
		return SGBlockUtil.isLadder(block.getRelative(BlockFace.NORTH))
				|| SGBlockUtil.isLadder(block.getRelative(BlockFace.SOUTH))
				|| SGBlockUtil.isLadder(block.getRelative(BlockFace.EAST))
				|| SGBlockUtil.isLadder(block.getRelative(BlockFace.WEST))
				|| SGBlockUtil.isLadder(block.getRelative(BlockFace.SOUTH_WEST))
				|| SGBlockUtil.isLadder(block.getRelative(BlockFace.NORTH_WEST))
				|| SGBlockUtil.isLadder(block.getRelative(BlockFace.SOUTH_EAST))
				|| SGBlockUtil.isLadder(block.getRelative(BlockFace.NORTH_EAST))
				|| SGBlockUtil.isLadder(altBlock.getRelative(BlockFace.NORTH))
				|| SGBlockUtil.isLadder(altBlock.getRelative(BlockFace.SOUTH))
				|| SGBlockUtil.isLadder(altBlock.getRelative(BlockFace.EAST))
				|| SGBlockUtil.isLadder(altBlock.getRelative(BlockFace.WEST))
				|| SGBlockUtil.isLadder(altBlock.getRelative(BlockFace.SOUTH_WEST))
				|| SGBlockUtil.isLadder(altBlock.getRelative(BlockFace.NORTH_WEST))
				|| SGBlockUtil.isLadder(altBlock.getRelative(BlockFace.SOUTH_EAST))
				|| SGBlockUtil.isLadder(altBlock.getRelative(BlockFace.NORTH_EAST));
	}

	/** Returns whether the player is on a fence. */
	public static boolean isOnFence(Player sgPlayer){
		final Block block = sgPlayer.getLocation().subtract(0,1,0).getBlock();
		final Block blockOnJump = sgPlayer.getLocation().subtract(0,2,0).getBlock();
		return SGBlockUtil.isFence(block)
				|| SGBlockUtil.isFence(block.getRelative(BlockFace.NORTH))
				|| SGBlockUtil.isFence(block.getRelative(BlockFace.SOUTH))
				|| SGBlockUtil.isFence(block.getRelative(BlockFace.EAST))
				|| SGBlockUtil.isFence(block.getRelative(BlockFace.WEST))
				|| SGBlockUtil.isFence(block.getRelative(BlockFace.SOUTH_WEST))
				|| SGBlockUtil.isFence(block.getRelative(BlockFace.NORTH_WEST))
				|| SGBlockUtil.isFence(block.getRelative(BlockFace.SOUTH_EAST))
				|| SGBlockUtil.isFence(block.getRelative(BlockFace.NORTH_EAST))
				|| SGBlockUtil.isFence(blockOnJump.getRelative(BlockFace.NORTH))
				|| SGBlockUtil.isFence(blockOnJump.getRelative(BlockFace.SOUTH))
				|| SGBlockUtil.isFence(blockOnJump.getRelative(BlockFace.EAST))
				|| SGBlockUtil.isFence(blockOnJump.getRelative(BlockFace.WEST))
				|| SGBlockUtil.isFence(blockOnJump.getRelative(BlockFace.SOUTH_WEST))
				|| SGBlockUtil.isFence(blockOnJump.getRelative(BlockFace.NORTH_WEST))
				|| SGBlockUtil.isFence(blockOnJump.getRelative(BlockFace.SOUTH_EAST))
				|| SGBlockUtil.isFence(blockOnJump.getRelative(BlockFace.NORTH_EAST));
	}

	/** Returns whether the player is on snow. */
	public static boolean isOnSnow(Player sgPlayer){
		final Block block = sgPlayer.getLocation().subtract(0,1,0).getBlock();
		return SGBlockUtil.isSnow(block)
				|| SGBlockUtil.isSnow(block.getRelative(BlockFace.NORTH))
				|| SGBlockUtil.isSnow(block.getRelative(BlockFace.SOUTH))
				|| SGBlockUtil.isSnow(block.getRelative(BlockFace.EAST))
				|| SGBlockUtil.isSnow(block.getRelative(BlockFace.WEST))
				|| SGBlockUtil.isSnow(block.getRelative(BlockFace.SOUTH_WEST))
				|| SGBlockUtil.isSnow(block.getRelative(BlockFace.NORTH_WEST))
				|| SGBlockUtil.isSnow(block.getRelative(BlockFace.SOUTH_EAST))
				|| SGBlockUtil.isSnow(block.getRelative(BlockFace.NORTH_EAST));
	}
}