/*
Title: <Game Name> Write Operation Interface
Author: 
Translator: David Sarkies
Version: 1.0
Date: 8 December 2025
Source: 
*/

package interfaces;

import java.io.IOException;

/**
 * Defines the contract for handling game commands and state modifications.
 * Implementations should process these commands and manage game state transitions.
 */
public interface GameCommandHandler {

    /**
     * Sets game state to running
     * @param isSavedGame true to enter saved game selection, false to return to normal play
     */
	public void setRunningGameState();
	
    /**
     * Sets the game state to display saved games
     * @param isSavedGame true to enter saved game selection, false to return to normal play
     */
	public void setSavedGameState();
		
    /**
     * Processes a text command from the player
     * @param input The raw command input (e.g., "go north")
     * @throws IOException if command processing fails due to I/O operations
     * @throws GameCommandException if command is invalid or cannot be executed
     */	
	public void processCommand(String input) throws IOException;
			
    /**
     * Directly moves player to specified location
     * @param locationID The destination location identifier
     * @throws IllegalArgumentException if locationID is invalid
     */	
	public void setRoom(int locationID);
	
    /**
     * Navigates forward through saved game list
     * @throws IOException if navigation fails due to I/O operations
     * @throws IllegalStateException if at end of saved game list
     */
	public void increaseLoadPosition() throws IOException;
	
    /**
     * Navigates backward through saved game list
     * @throws IOException if navigation fails due to I/O operations
     * @throws IllegalStateException if at beginning of saved game list
     */
	public void decreaseLoadPosition() throws IOException;
	
}

/* 3 December 2025 - Created File
 * 8 December 2025 - Removed game specific code
 * 				   - Increased version
 */
