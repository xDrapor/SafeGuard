package com.xdrapor.safeguard.utilities;
import net.minecraft.server.v1_5_R2.IBlockAccess;
import net.minecraft.server.v1_5_R2.Item;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_5_R2.CraftWorld;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.xdrapor.safeguard.core.ICore;
import com.xdrapor.safeguard.player.SGPlayer;

/**
 * This is a static class that will not be instanced ever. The 'data' is already instanced in the Event we are passed, no reason to create it again. <-- Rename once understood.
 * 
 * @author xDrapor
 * @author IchBinMude
 * @author Richard
 * 
 */
public class SGBlockUtil implements ICore {
	
	/** Array to hold all materials harvestable by hand */
	private static Material[] possibleWithHand = { Material.TRAP_DOOR,
			Material.WOOD_DOOR, Material.CHEST, Material.WORKBENCH,
			Material.FENCE, Material.JUKEBOX, Material.FENCE_GATE,
			Material.WOOD, Material.WOOD_STEP, Material.BOOKSHELF,
			Material.MELON, Material.PUMPKIN, Material.SIGN, Material.WOOL,
			Material.RAILS, Material.CLAY, Material.POWERED_RAIL,
			Material.SOIL, Material.GRASS, Material.GRAVEL, Material.MYCEL,
			Material.SPONGE, Material.CAKE_BLOCK, Material.DIRT,
			Material.LEVER, Material.PISTON_BASE, Material.PISTON_EXTENSION,
			Material.PISTON_MOVING_PIECE, Material.PISTON_STICKY_BASE,
			Material.SAND, Material.SOUL_SAND, Material.WOOD_PLATE,
			Material.CACTUS, Material.LADDER, Material.GLASS,
			Material.THIN_GLASS, Material.REDSTONE_LAMP_OFF,
			Material.REDSTONE_LAMP_ON, Material.BED_BLOCK, Material.COCOA,
			Material.HUGE_MUSHROOM_1, Material.HUGE_MUSHROOM_2,
			Material.LEAVES, Material.VINE };

	/** Array to hold all materials harvestable by axes */
	private static Material[] axeBlocks = { Material.JACK_O_LANTERN,
			Material.PUMPKIN, Material.BOOKSHELF, Material.CHEST,
			Material.WORKBENCH, Material.FENCE, Material.FENCE_GATE,
			Material.HUGE_MUSHROOM_1, Material.HUGE_MUSHROOM_2,
			Material.JUKEBOX, Material.NOTE_BLOCK, Material.SIGN,
			Material.TRAP_DOOR, Material.WOOD, Material.WOODEN_DOOR,
			Material.WOOD_PLATE, Material.WOOD_STEP, Material.WOOD_STAIRS,
			Material.BIRCH_WOOD_STAIRS, Material.SPRUCE_WOOD_STAIRS,
			Material.JUNGLE_WOOD_STAIRS };
	
	/** Array to hold pickaxe blocks */
	private static Material[] pickaxeBlocks = { Material.LAPIS_ORE,
			Material.LAPIS_BLOCK, Material.DIAMOND_ORE, Material.GOLD_ORE,
			Material.IRON_ORE, Material.COAL_ORE, Material.ICE,
			Material.EMERALD_BLOCK, Material.BREWING_STAND, Material.CAULDRON,
			Material.IRON_FENCE, Material.IRON_DOOR_BLOCK, Material.IRON_BLOCK,
			Material.DIAMOND_BLOCK, Material.GOLD_BLOCK,
			Material.DETECTOR_RAIL, Material.POWERED_RAIL, Material.RAILS,
			Material.BRICK, Material.COAL_ORE, Material.COBBLESTONE,
			Material.DISPENSER, Material.ENCHANTMENT_TABLE,
			Material.ENDER_STONE, Material.ENDER_CHEST, Material.FURNACE,
			Material.MOB_SPAWNER, Material.MOSSY_COBBLESTONE,
			Material.NETHER_BRICK, Material.NETHER_FENCE, Material.NETHERRACK,
			Material.SANDSTONE, Material.STEP, Material.COBBLESTONE_STAIRS,
			Material.BRICK_STAIRS, Material.SANDSTONE_STAIRS, Material.STONE,
			Material.SMOOTH_BRICK, Material.SMOOTH_STAIRS, Material.STONE_PLATE };
	
	/** Array to hold shear blocks */
	private static Material[] shearBlocks = { Material.LEAVES, Material.WEB,
			Material.WOOL };
	
	/** Array to hold shovel blocks */
	private static Material[] shovelBlocks = { Material.CLAY, Material.DIRT,
			Material.SOIL, Material.GRASS, Material.GRAVEL, Material.MYCEL,
			Material.SAND, Material.SOUL_SAND, Material.SNOW,
			Material.SNOW_BLOCK };
	
	/** Returns the time between the last block break and last block hit times of the specified SGPlayer. */
	public static double getPlayerBreakDuration(Player sgPlayer) {
		return Math.round(safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).getLastBlockBreakTime() - safeGuard.sgPlayerManager.getPlayer(sgPlayer.getName()).getLastBlockHitTime());
	}

	/** Returns a block hardness value. */
	public static double getHardness(Block block) {
		if(block.getTypeId() == 0) return 0;
		return net.minecraft.server.v1_5_R2.Block.byId[block.getTypeId()].l(((CraftWorld)block.getWorld()).getHandle(), block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
	}

	/** Returns how long it should take to break the block with the specified tool. */
	public static double getDurationVSTool(SGPlayer player, ItemStack item, Block block) {
		float currentToolMultiplier = item.getTypeId() == 0 ? 1.0F : net.minecraft.server.v1_5_R2.Item.byId[item.getTypeId()].getDestroySpeed(new net.minecraft.server.v1_5_R2.ItemStack(Item.byId[item.getTypeId()]), net.minecraft.server.v1_5_R2.Block.byId[block.getTypeId()]);
		double currentBlockHardness = getHardness(block);
		double percentToGet = 100D;
		if(isQualityTool(player.getPlayer().getItemInHand(), block.getType()) || possibleBreakWithHand(block)) {
			if(player.getPlayer().getItemInHand().containsEnchantment(Enchantment.DIG_SPEED)) {
				percentToGet = 100D - (getPercentAtEffLevel(player.getPlayer().getItemInHand().getEnchantmentLevel(Enchantment.DIG_SPEED)));
			}
		}
		return Math.round((1000f * 5f * currentBlockHardness) / (currentToolMultiplier * 3.33f) * (percentToGet / 100));
	}

	/** Returns the closest ground Location relative to the specified Location. */
	public static Location findClosestGroundToLocation(Player sgPlayer) {
		Location highestBlock = sgPlayer.getWorld().getHighestBlockAt(sgPlayer.getLocation()).getLocation();

		// Before you had an infinite loop until condition produced a result, what about 'The End' where your condition could possibly never return a result.
		if (highestBlock == null) {	return sgPlayer.getWorld().getSpawnLocation(); }

		// Return highest block if player is above highest block.
		if (sgPlayer.getLocation().getY() >= highestBlock.getY()) { return highestBlock; }

		// Return location under player if below highest block.
		for (int i = 0; i < sgPlayer.getLocation().getY(); i++) {
			if (!sgPlayer.getWorld().getBlockAt(sgPlayer.getLocation().subtract(0, i, 0)).isEmpty()) {
				return sgPlayer.getLocation().subtract(0, i - 1, 0);
			}
		}

		return sgPlayer.getWorld().getSpawnLocation();
	}

	/** Returns true if the specified Block is a fence. */
	public static boolean isFence(final Block block) {
		return block.getType() == Material.NETHER_FENCE || block.getType() == Material.FENCE;
	}

	/**
	 * Checks to see if the specified item is a shovel.
	 * @param item
	 * @return
	 */
	private static boolean isShovel(ItemStack item){
		Material mat = item.getType();
		return mat == Material.WOOD_SPADE || mat == Material.STONE_SPADE || mat == Material.IRON_SPADE || mat == Material.GOLD_SPADE || mat == Material.DIAMOND_SPADE;
	}
	/**
	 * Checks to see if the specified item is a pickaxe.
	 * @param item
	 * @return
	 */
	private static boolean isShear(ItemStack item){
		Material mat = item.getType();
		return mat == Material.SHEARS;
	}

	/**
	 * Checks to see if the specified item is a sword.
	 * @param item
	 * @return
	 */
	private static boolean isSword(ItemStack item){
		Material mat = item.getType();
		return mat == Material.WOOD_SWORD || mat == Material.STONE_SWORD || mat == Material.IRON_SWORD || mat == Material.GOLD_SWORD || mat == Material.DIAMOND_SWORD;
	}

	/** Checks to see if the specified item is a pickaxe. */
	private static boolean isPickaxe(ItemStack item){
		Material mat = item.getType();
		return mat == Material.WOOD_PICKAXE || mat == Material.STONE_PICKAXE || mat == Material.IRON_PICKAXE || mat == Material.GOLD_PICKAXE || mat == Material.DIAMOND_PICKAXE;
	}

	/** Checks to see if the specified item is an axe */
	private static boolean isAxe(ItemStack item){
		Material mat = item.getType();
		return mat == Material.WOOD_AXE || mat == Material.STONE_AXE || mat == Material.IRON_AXE || mat == Material.GOLD_AXE || mat == Material.DIAMOND_AXE;
	}
	
	/** Checks if the block is a quality tool */
	public static boolean isQualityTool(ItemStack item, Material mat) {
		if (isAxe(item)) {
			for (Material i : axeBlocks) {
				if (i == mat) {
					return true;
				}
			}
		}
		if (isPickaxe(item)) {
			for (Material i : pickaxeBlocks) {
				if (i == mat) {
					return true;
				}
			}
		}
		if (isShear(item)) {
			for (Material i : shearBlocks) {
				if (i == mat) {
					return true;
				}
			}
		}
		if (isShovel(item)) {
			for (Material i : shovelBlocks) {
				if (i == mat) {
					return true;
				}
			}
		}
		if (isSword(item)) {
			return true;
		}
		return false;
	}
	
	/** Gets the efficiency level modifier */
	private static int getPercentAtEffLevel(int enchantLevel)
	{
		switch(enchantLevel)
		{
		case 1:
			return 30;
		case 2:
			return 39;
		case 3:
			return 51;
		case 4:
			return 66;
		case 5:
			return 85;
		default:
			return 100;
		}
	}
	
	/** Returns true if the specified Block is a stair. */
	public static boolean isStair(final Block block) {
		return block.getType() == Material.WOOD_STEP
				|| block.getType() == Material.STEP
				|| block.getType() == Material.BRICK_STAIRS
				|| block.getType() == Material.COBBLESTONE_STAIRS
				|| block.getType() == Material.WOOD_STAIRS
				|| block.getType() == Material.BIRCH_WOOD_STAIRS
				|| block.getType() == Material.JUNGLE_WOOD_STAIRS
				|| block.getType() == Material.SPRUCE_WOOD_STAIRS
				|| block.getType() == Material.NETHER_BRICK_STAIRS
				|| block.getType() == Material.SANDSTONE_STAIRS
				|| block.getType() == Material.SMOOTH_STAIRS
				|| block.getType() == Material.WOOD_DOUBLE_STEP;
	}
	
	/** Is the block able to be harvestable with the hand alone? */
	private static boolean possibleBreakWithHand(Block block) {
		for(Material i : possibleWithHand) {
			if(block.getType() == i)return true;
		}
		return false;
	}

	/** Returns true if the specified Block is snow. */
	public static boolean isSnow(final Block block) {
		return block.getType() == Material.SNOW;
	}

	/** Returns true if the specified Block is ice. */
	public static boolean isIce(final Block block) {
		return block.getType() == Material.ICE;
	}
	
	/** Returns true if the block is a ladder **/
	public static boolean isLadder(Block block) {
		return block.getType() == Material.LADDER;
	}
	
	/** Checks to see if the specified block is a vine. */
	public static boolean isClimbable(final Block block){
		return block.getType() == Material.VINE || block.getType() == Material.LADDER;
	}
	
	/** Checks to see if the specified block is a lily. */
	public static boolean isLily(final Block block){
		return block.getType() == Material.WATER_LILY;
	}
	
	/** Returns whether a block is passable or not **/
	public static boolean isPassable(final SGPlayer player, final IBlockAccess blockAccess, final double x, final double y, final double z, final int id) {
		final int bx = Location.locToBlock(x);
		final int by = Location.locToBlock(y);
		final int bz = Location.locToBlock(z);
		final net.minecraft.server.v1_5_R2.Block block = net.minecraft.server.v1_5_R2.Block.byId[id];
		final Block craftBlock = player.getPlayer().getWorld().getBlockAt((int)x, (int)y, (int)z);
		if(block == null || craftBlock == null)return true;
		final double fx = x - bx;
		final double fy = y - by;
		final double fz = z - bz;
		if (craftBlock.isLiquid() || craftBlock.isEmpty() || player.isOnLily() || player.isClimbing() || player.isOnSnow() || fx < block.u() || fx >= block.v() || fy < block.w() || fy >= block.x() || fz < block.y() || fz >= block.z()) return true;
		else {
			if (SGMovementUtil.isAboveStairs(player.getPlayer()))if ((blockAccess.getData(bx, by, bz) & 0x4) != 0)if (fy < 0.5) return true;else if (fy >= 0.5) return true;
			else if (id == Material.WOODEN_DOOR.getId()) return true;
			else if (id == Material.IRON_DOOR_BLOCK.getId()) return true;
			else if (player.getPlayer().getLocation().getBlock().getRelative(BlockFace.NORTH).getType() == Material.WOODEN_DOOR) return false;
			else if (player.getPlayer().getLocation().getBlock().getRelative(BlockFace.NORTH).getType() == Material.IRON_DOOR_BLOCK) return false;
			else if (id == Material.SOUL_SAND.getId() && fy >= 0.875) return true; // 0.125
			else if (id == Material.SAND.getId() && fy >= 0.975) return true; // 0.025
			else if (id == Material.IRON_FENCE.getId() || id == Material.THIN_GLASS.getId())if (Math.abs(0.5 - fx) > 0.05 && Math.abs(0.5 - fz) > 0.05) return true;
			else if (id == Material.FENCE.getId() || id == Material.NETHER_FENCE.getId())if (Math.abs(0.2 - fx) > 0.02 && Math.abs(0.2 - fz) > 0.02) return true;
			else if (id == Material.FENCE_GATE.getId() && (blockAccess.getData(bx, by, bz) & 0x4)!= 0) return true;
			else if (id == Material.CAKE_BLOCK.getId() && fy >= 0.4375) return true; 
			else if (id == Material.CAULDRON.getId()){if (Math.abs(0.5 - fx) < 0.1 && Math.abs(0.5 - fz) < 0.1 && fy > 0.1) return true;}
			else if (id == Material.WATER.getId())return true;
			else if (id == Material.LADDER.getId())return true;
			else if (id == Material.VINE.getId())return true;
			else if (id == Material.WATER_LILY.getId())return true;
			else if (id == Material.SNOW.getId())return true;
			else if (id == Material.AIR.getId())return true;
			else if (id == Material.CACTUS.getId() && fy >= 0.9375) return true;
			return false;
		}
	}
}