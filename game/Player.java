/*
Title: <Game Name> Initialise Game Class
Author: 
Translator: David Sarkies
Version: 1.0
Date: 8 December 2025
Source: 
*/

package game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import data.Constants;

/**
 * Represents the player in the game.
 * <p>
 * The player has stats such as strength, wisdom, time, weight, food, and drink. 
 * They can move between rooms, swim, and their stats change over time.
 */
public class Player implements Serializable {
	
	private static final long serialVersionUID = 495300605316911022L;
	private static final Logger logger = Logger.getLogger(Player.class.getName());
	
	private int room = Constants.START_LOCATION;
	private int roomToDisplay = this.room;
	private final Map<String,Object> stats = new HashMap<>();
	private enum PlayerState { NORMAL };
	private PlayerState playerState = PlayerState.NORMAL;
		
    /**
     * Creates a new player with default starting stats.
     */
	public Player() {
		
		//Initialize Stats			
		stats.put("timeRemaining",Constants.STARTING_TIME);		
		stats.put("weight", 0);
	}
	
    /**
     * Gets the current display room for the player.
     *
     * @return the room number to display
     */
	public int getDisplayRoom() {
		return this.roomToDisplay;
	}
		
    /**
     * Applies turn-based updates to the player's stats.
     * Decreases time remaining and reduces strength based on weight carried.
     */
	public void turnUpdateStats() {
		logger.info(stats.get("timeRemaining").toString());
		int timeRemaining = (int) stats.get("timeRemaining");
		stats.put("timeRemaining", timeRemaining-1);
		logger.info(stats.get("timeRemaining").toString());
	}
	
    /** @return the current room number */
	public int getRoom() {
		return this.room;
	}
	
    /**
     * Sets the player's current room.
     *
     * @param room the new room number
     */
	public void setRoom(int room) {
		logger.log(Level.INFO, "Player moved to room: " + room);

		this.room = room;
		this.roomToDisplay = room;
	}
	
    /**
     * Retrieves a stat by name.
     *
     * @param statName the stat key (e.g., "strength", "wisdom")
     * @return the stat value, or {@code null} if not found
     */
	public Object getStat(String statName) {
		return stats.get(statName);
	}
	
    /**
     * Updates a stat.
     *
     * @param statName the stat key
     * @param value    the new value
     */
	public void setStat(String statName,Object value) {
		logger.info("Adjust "+statName+" by "+value);
		stats.put(statName,value);
	}
	
    /**
     * Reduces a named stat by 1.
     *
     * @param statName the stat key
     */
	public void reduceStat(String statName) {
		int stat = (int) stats.get(statName);
		if(stat>0) {
			stats.put(statName, stat-1);
		}
	}
	
    /** Sets the player back to the {@link PlayerState#NORMAL} state. */
	public void setPlayerStateNormal() {
		playerState = PlayerState.NORMAL;
	}
	
    /**
     * Checks if the player is in the {@link PlayerState#NORMAL} state.
     *
     * @return true if normal
     */
	public boolean isPlayerStateNormal() {
		return playerState == PlayerState.NORMAL;
	}
			
    /** @return formatted string of strength and wisdom */
	public String toStringStatus() {
		return String.format("Strength: %.2f         wisdom: %d", stats.get("strength"),stats.get("wisdom"));
	}

    /** @return formatted string of time remaining */
	public String toStringTimeRemaining() {
		return String.format("Time Remaining: %d",stats.get("timeRemaining"));
	}
	
    /** {@inheritDoc} */
	@Override
	public String toString() {
	    return "Player{" +
	            "room=" + room +
	            ", timeRemaining=" + stats.get("timeRemaining")  +
	            ", weight=" + stats.get("weight") +
	            '}';
	}
}

/* 3 December 2025 - Created File
 * 7 December 2025 - Cleared Game Related code
 * 8 December 2025 - Increased version number
 */