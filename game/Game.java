/*
Title: <Game Name> Game Class
Author: 
Translator: David Sarkies
Version: 1.0
Date: 8 December 2025
Source: 
*/

package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import data.Constants;
import data.Item;
import data.Location;

/**
 * Core game state and logic container.
 * <p>
 * The {@code Game} class manages rooms, items, exits, game state transitions,
 * messages, save-game handling, and special mechanics. It acts as the central 
 * hub for the adventure game.
 * </p>
 */
public class Game implements Serializable {
	
	private static final long serialVersionUID = 3473676803014192040L;
	private static final Logger logger = Logger.getLogger(Game.class.getName());
	
	/** List of all Game Locations. */
	private Location[] locationList;
	
	/** List of all game items. */
	private Item[] itemList;
	
	/** Active Exits for the current room. */
	private boolean[] exitNumbers;
	
	/** Handler for special exits. */	
	private SpecialExitHandler specialExitHandler = new SpecialExitHandler();
	
	/** Handler for special items. */
	private SpecialItemHandler specialItemHandler = new SpecialItemHandler();
	
	/** Standard in-game message builder. */
	private MessageBuilder normalMessage = new MessageBuilder("Let your quest begin!");
	
	/** Special message builder. */
	private MessageBuilder panelMessage = new MessageBuilder();
	
	/** Stores last commands entered by the player. */
	private String[] commands = {"","",""};
	
	/** Enum of possible game states. */
	private enum GameState { STARTED,RUNNING,SAVED_GAMES,ENDED,RESTART }
	
	private boolean hasMessage = false;
	private GameState gameState = GameState.STARTED;
	
	/** Save game tracking. */
	private int saveGameCount = 0;
	private int startGameCount = Constants.INITIAL_SAVE_COUNT;
	private boolean upperLimitSavedGames = false;
	private boolean lowerLimitSavedGames = false;
	private String[] savedGamesDisplayed = {"","","","",""};
		
	/**
	 * Creates a new game instance with the given locations, items, and exit handler.
	 *
	 * @param locations the list of available locations
	 * @param items the list of available items
	 * @param specialExitHandler the handler for special exits
	 */
	public Game(Location[] locations, Item[] items,SpecialExitHandler specialExitHandler) {
		
		this.locationList = locations;
		this.itemList = items;
		this.specialExitHandler = specialExitHandler;
		
		// Sets start location as visited
		locationList[Constants.START_LOCATION].setVisited();
		
	}
	
	/**
	 * Returns the name of a room by number.
	 *
	 * @param roomNumber the room index
	 * @return the room name
	 * @throws IllegalArgumentException if the room number is invalid
	 */
	public String getRoomName(int roomNumber) {
		
	    if (roomNumber < 0 || roomNumber >= locationList.length) {
	        throw new IllegalArgumentException("Invalid room number: " + roomNumber);
	    }
		
		return this.locationList[roomNumber].getName();
	}
	
	/**
	 * Retrieves all items present in the given room, including special items.
	 *
	 * @param roomNumber the room index
	 * @return a string listing items, or empty if none
	 * @throws IllegalArgumentException if the room number is invalid
	 */
	public String getItems(int roomNumber) {
		
	    if (roomNumber < 0 || roomNumber >= locationList.length) {
	        throw new IllegalArgumentException("Invalid room number: " + roomNumber);
	    }
	    
	    List<String> itemsFound = new ArrayList<>();
		
		String specialItems = specialItemHandler.getSpecialItems(roomNumber, itemList, locationList);
		if(!specialItems.isEmpty()) {
			itemsFound.add(specialItems);
		}
				
		//Goes through each of the items
		for (Item item:itemList) {
			if(item != null && item.isAtLocation(roomNumber) && item.getItemFlag()<1) {
				itemsFound.add(item.getItemName());
			}
		}
						
		return itemsFound.isEmpty()?"":"You see: "+String.join(", ",itemsFound);
	}
	
	/**
	 * Returns available exits for a given room.
	 * 
	 * @param roomNumber the room index
	 * @return formatted string of exits, or empty if none
	 */
	public String getExits(int roomNumber) {
		
		exitNumbers = locationList[roomNumber].getExits();

		String exits = "";
		for (int i=0;i<Constants.DIRECTIONS.length;i++) {
			if (exitNumbers[i] && specialExitHandler.displayExit(roomNumber, Constants.DIRECTIONS[i])) {
				exits = addExit(Constants.DIRECTIONS[i],exits);
			}
		}
		
		return exits.isEmpty()?"":"You can go:"+exits;
	}
	
	/**
	 * Returns description of special exits in a room.
	 *
	 * @param roomNumber the room index
	 * @return special exits description, or empty if none
	 */
	public String getSpecialExits(int roomNumber) {
		return specialExitHandler.getSpecialExit(roomNumber, itemList);	
	}
	
	/**
	 * Checks whether an exit exists in a given direction.
	 *
	 * @param room the room index
	 * @param direction the direction index
	 * @return true if an exit exists, false otherwise
	 */
	public boolean checkExit(int room, int direction) {
		return exitNumbers[direction];
	}
	
	/**
	 * Gets a {@link Location} object by room number.
	 *
	 * @param roomNumber the room index
	 * @return the corresponding location
	 */
	public Location getRoom(int roomNumber) {
		return locationList[roomNumber];
	}
	
	// --- Map and location functions ---
	
	/** Returns whether a room has been visited. */
	public boolean getRoomVisited(int roomNumber) {
		return locationList[roomNumber].getVisited();
	}
	
	/** Returns the type of image associated with a room. */
	public String getRoomImageType(int roomNumber) {
		return locationList[roomNumber].getRoomType();
	}
	
	/** Returns the exits available from a given room. */
	public boolean[] getRoomExits(int roomNumber) {
		return locationList[roomNumber].getExits();
	}
	
	//Checks to see if an exit has already been added
	private String addExit(String exit, String exits) {
		
		if (exits.length()>0) {
			exits = String.format("%s, %s",exits,exit);
		} else {
			exits = String.format("%s %s",exits,exit);
		}
		
		return exits;
	}
	

	// --- Messaging functions ---

	/** Gets the accumulated in-game messages. */
	public List<String> getNormalMessage() {
		return normalMessage.getMessages();
	}
	
	/** Gets messages destined for the panel view. */
	public List<String> getPanelMessage() {
		return panelMessage.getMessages();
	}
			
	/**
	 * Adds a message to the message object
	 *
	 * @param message the message text
	 * @param clear whether to clear existing messages first
	 * @param isLongMessage whether to format as a long message
	 */
	public void addMessage(String message,boolean clear, boolean isLongMessage ) {
		logger.info("Adding message: " + message);
		
		if (isLongMessage) {
			normalMessage.addLongMessage(message, clear);
		} else {
			normalMessage.addMessage(message, clear);
		}
	}
	
	/**
	 * Adds a message to the panel message object.
	 *
	 * @param message the message text
	 * @param clear whether to clear existing panel messages first
	 */
	public void addPanelMessage(String message,boolean clear) {
		logger.info("Adding Panel message: " + message);
		panelMessage.addMessage(message, clear);
	}
	
	// --- Item and command accessors ---

	/** Gets a stored command by index. */
	public String getCommand(int number) {
		if (number<0||number>=commands.length) {
			throw new IllegalArgumentException("Invalid command number: " + number);
		}
		return commands[number];
	}
	
	/** Gets an item by index. */
	public Item getItem(int itemNumber) {
		if (itemNumber<0||itemNumber >=itemList.length) {
			throw new IllegalArgumentException("Invalid item number: " + itemNumber);
		}
		return itemList[itemNumber];
	}
	
	/** Returns a sum of an item’s flag and location values. */
	public int getItemFlagSum(int itemNumber) {
		
		if (itemNumber<0||itemNumber >=itemList.length) {
			throw new IllegalArgumentException("Invalid item number: " + itemNumber);
		}
		
		return itemList[itemNumber].getItemFlag() + itemList[itemNumber].getItemLocation();
	}
				
	// --- Save game handling ---

	/** Gets the current save game count. */
	public int getCount() {
		return this.saveGameCount;
	}
	
	/** Increases the save game count. */
	public void increaseCount() {
		this.saveGameCount++;
	}
	
	/** Decreases the save game count. */
	public void descreaseCount() {
		this.saveGameCount--;
	}
	
	/** Resets the save game count. */
	public void resetCount() {
		this.saveGameCount=0;
	}
			
	/** Sets whether more saved games are available above. */
	public void setUpperLimitSavedGames(boolean moreGames) {
		this.upperLimitSavedGames = moreGames;
		logger.info("More game position set to: " + this.upperLimitSavedGames);
	}
	
	/** Returns whether more saved games exist above. */
	public boolean getUpperLimitSavedGames() {
		return this.upperLimitSavedGames;
	}
	
	/** Sets whether more saved games are available below. */
	public void setLowerLimitSavedGames(boolean lessGames) {
		this.lowerLimitSavedGames = lessGames;
		logger.info("Less game position set to: " + this.lowerLimitSavedGames);
	}
	
	/** Returns whether more saved games exist below. */
	public boolean getLowerLimitSavedGames() {
		return this.lowerLimitSavedGames;
	}
	
	/** Returns the list of displayed saved game slots. */
	public String[] getDisplayedSavedGames() {
		return this.savedGamesDisplayed;
	}

	/** Updates displayed saved games. */
	public void setDisplayedGames(String[] gameDisplayed) {
		this.savedGamesDisplayed = gameDisplayed;
	}	
	
	/** Sets game state to running. */
	public void setRunningGameState() {
		gameState = GameState.RUNNING;
	}

	/** Sets game state to saved game selection. */
	public void setSavedGameState() {
		gameState = GameState.SAVED_GAMES;
	}
	
	/** Toggles message state on/off. */
	public void setMessageGameState() {
		hasMessage = !hasMessage;
	}
		
	/** Sets game state to ended. */
	public void setEndGameState() {
		
		logger.info("Game ended.");
		gameState = GameState.ENDED;
	}
	
	/** Sets game state to restart. */
	public void setRestartGameState() {
		
		logger.info("Game ended.");
		gameState = GameState.RESTART;
	}
	
	/** Checks and progresses from STARTED to RUNNING state. */
	public boolean isInitialGameState() {
		
		boolean started = false;

		if (startGameCount>0) {
			startGameCount --;
		} else if (gameState == GameState.STARTED) {
			gameState = GameState.RUNNING;
			started = true;
		}
		return started;
	}
	
	/** Returns true if game is in saved game state. */
	public boolean isSavedGameState() {
		return gameState == GameState.SAVED_GAMES;
	}
	
	/** Returns true if game is ended. */
	public boolean isEndGameState() {
		return gameState == GameState.ENDED;
	}
	
	/** Returns true if game has restarted. */
	public boolean isRestartGameState() {
		return gameState == GameState.RESTART;
	}
	
	/** Returns true if game is showing messages. */
	public boolean isMessageState() {
		return hasMessage;
	}
	
	/** Returns true if game is in running state. */
	public boolean isRunningState() {
		return gameState == GameState.RUNNING;
	}
}

/* 3 December 2025 - Created File
 * 7 December 2025 - Removed game related code
 * 8 December 2025 - Increased version number
 */