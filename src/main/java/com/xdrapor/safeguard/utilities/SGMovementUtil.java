package com.xdrapor.safeguard.utilities;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.xdrapor.safeguard.core.ICore;

/**
 * This is a static class that will not be instanced ever. The 'data' is already
 * instanced in the Event we are passed, no reason to create it again. <--
 * Rename once understood.
 * 
 * @author xDrapor
 * @author IchBinMude
 * @author Richard
 * 
 */
public class SGMovementUtil implements ICore {

	/** Returns the distance between two location X axis coordinates. */
	public static double getDistanceX(Location to, Location from,
			boolean absolute) {
		return (absolute ? (Math.abs(to.getX() - from.getX()))
				: (to.getX() - from.getX()));
	}

	/** Returns the distance between two location Y axis coordinates. */
	public static double getDistanceY(Location to, Location from,
			boolean absolute) {
		return (absolute ? (Math.abs(to.getY() - from.getY()))
				: (to.getY() - from.getY()));
	}

	/** Returns the distance between two location Z axis coordinates. */
	public static double getDistanceZ(Location to, Location from,
			boolean absolute) {
		return (absolute ? (Math.abs(to.getZ() - from.getZ()))
				: (to.getZ() - from.getZ()));
	}

	/** Returns the horizontal distance between two location coordinate sets. */
	public static double getDistanceHorizontal(Location to, Location from) {
		return (Math.sqrt((getDistanceX(to, from, true)
				* getDistanceX(to, from, true) + getDistanceZ(to, from, true)
				* getDistanceZ(to, from, true))));
	}

	/** Returns the vertical distance between two location coordinate sets. */
	public static double getDistanceVertical(Location to, Location from) {
		return (Math.sqrt(getDistanceY(to, from, true)
				* getDistanceY(to, from, true)));
	}

	/** Returns the safe distance on the X axis for the specified SGPlayer. */
	public static double getSafeDistanceX(Player sgPlayer, boolean absolute) {
		return (absolute ? (Math.abs(sgPlayer.getPlayer().getLocation().getX()
				- safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName())
						.getSafeLocation().getX())) : (sgPlayer.getPlayer()
				.getLocation().getX() - safeGuard.sgPlayerManager
				.getPlayer(sgPlayer.getName()).getSafeLocation().getX()));
	}

	/** Returns the safe distance on the Y axis for the specified SGPlayer. */
	public static double getSafeDistanceY(Player sgPlayer, boolean absolute) {
		return (absolute ? (Math.abs(sgPlayer.getPlayer().getLocation().getY()
				- safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName())
						.getSafeLocation().getY())) : (sgPlayer.getPlayer()
				.getLocation().getY() - safeGuard.sgPlayerManager
				.getPlayer(sgPlayer.getName()).getSafeLocation().getY()));
	}

	/** Returns the safe distance on the Z axis for the specified SGPlayer. */
	public static double getSafeDistanceZ(Player sgPlayer, boolean absolute) {
		return (absolute ? (Math.abs(sgPlayer.getPlayer().getLocation().getZ()
				- safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName())
						.getSafeLocation().getZ())) : (sgPlayer.getPlayer()
				.getLocation().getZ() - safeGuard.sgPlayerManager
				.getPlayer(sgPlayer.getName()).getSafeLocation().getZ()));
	}

	/** Returns the safe vertical distance for the specified SGPlayer. */
	public static double getSafeDistanceVertical(Player sgPlayer) {
		return (Math.sqrt(getSafeDistanceY(sgPlayer, true)
				* getSafeDistanceY(sgPlayer, true)));
	}

	/** Returns the safe horizontal distance for the specified SGPlayer. */
	public static double getSafeDistanceHorizontal(Player sgPlayer) {
		return (Math.sqrt((getSafeDistanceX(sgPlayer, true) * getSafeDistanceX(
				sgPlayer, true))
				+ (getSafeDistanceZ(sgPlayer, true) * getSafeDistanceZ(
						sgPlayer, true))));
	}

	/** Sets the safe location for the specified SGPlayer. */
	public static void setSafeLocation(Player sgPlayer) {
		safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName())
				.setSafeLocation(sgPlayer.getPlayer().getLocation());
	}

	/** Returns TRUE if the specified SGPlayer is falling. */
	public static boolean getSafeFalling(Player sgPlayer) {
		return (getSafeDistanceY(sgPlayer, false) < 0);
	}

	/** Returns TRUE if Location difference is negative. */
	public static boolean getFalling(Location to, Location from) {
		return (getDistanceY(to, from, false) < 0);
	}

	/**
	 * Returns the safe location for the specified SGPlayer or if NULL the
	 * closest ground.
	 */
	public static Location getSafeLocation(Player sgPlayer) {
		return (safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName())
				.getSafeLocation() != null ? (safeGuard.sgPlayerManager
				.getPlayer(sgPlayer.getName()).getSafeLocation())
				: (SGBlockUtil
						.findClosestGroundToLocation(sgPlayer.getPlayer())));
	}

	/** Resets the safe location for the specified SGPlayer. */
	public static void resetSafeLocation(Player sgPlayer) {
		safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName())
				.resetSafeLocation();
	}

	public static void setSafeLocationSpawn(Player sgPlayer) {
		safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName())
				.setSafeLocation(
						sgPlayer.getPlayer().getWorld().getSpawnLocation());
	}

	/**
	 * Checks to see if the player is above stairs.
	 * 
	 * @param block
	 * @return
	 */
	public static boolean isAboveStairs(final Player sgPlayer) {
		final Block block = sgPlayer.getLocation().getBlock();
		final Block altBlock = sgPlayer.getLocation().add(0, 0.5, 0).getBlock();
		return (SGBlockUtil.isStair(block.getRelative(BlockFace.NORTH))
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
				|| SGBlockUtil.isStair(altBlock.getRelative(BlockFace.NORTH_EAST)));
	}
}