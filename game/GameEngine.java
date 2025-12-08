/*
Title: <Game Name> Game Engine
Author: 
Translator: David Sarkies
Version: 1.0
Date: 8 December 2025
Source: 
*/

package game;

import interfaces.GameCommandHandler;
import interfaces.GameStateProvider;

import java.io.IOException;
import java.util.List;

import Tests.Test;
import command_process.ActionResult;
import command_process.CommandProcessor;

/**
 * Core engine for the adventure game.
 *
 * {@code GameEngine} handles command processing, state management,
 * and communication between the game logic and the UI.
 * It implements {@link GameCommandHandler} and {@link GameStateProvider}
 * to provide game functionality and status to the interface.
 */
public class GameEngine implements GameCommandHandler,GameStateProvider {
	
	private Game game;
	private Player player;
	private CommandProcessor processor;
	private final String[] commandHistory = {"","",""};
	private final Test test = new Test();

    /**
     * Constructs a {@code GameEngine} with the specified game and player.
     *
     * @param game the game state object
     * @param player the player object
     */	
	public GameEngine(Game game,Player player) {
		this.game = game;
		this.player = player;
		this.test.setTest(this.game, this.player);
	}
	
	//=== Core Game Loop ===//

    /**
     * Processes a command issued by the player.
     * Delegates to {@link SwimmingHandler} if the player is swimming,
     * otherwise to {@link CommandProcessor}.
     *
     * @param command the command string to process
     * @throws IOException if command execution fails
     */
	public void processCommand(String command) throws IOException {
		
		ActionResult result = null;
		
		processor = new CommandProcessor();
		result = processor.execute(command,game,player);
		applyResult(result);
		updateCommandHistory(command);
		
		test.displayValue(this.game, this.player);
	}

    /**
     * Applies the effects of an {@link ActionResult} to the game and player.
     *
     * @param result the action result containing updated game and player
     */
	private void applyResult(ActionResult result) {
		this.player = result.getPlayer();
		this.game = result.getGame();
		this.player.turnUpdateStats();
	}

    /**
     * Updates the history of the last three commands.
     *
     * @param command the command to add to history
     */
	private void updateCommandHistory(String command) {
		
		//Saves the commands into the previous command list
		if (this.commandHistory[0].equals("")) {
			this.commandHistory[0] = command;
		} else if (this.commandHistory[1].equals("")) {
			this.commandHistory[1] = command;
		} else if (this.commandHistory[2].equals("")) {
			this.commandHistory[2] = command;
		} else {
			this.commandHistory[0] = this.commandHistory[1];
			this.commandHistory[1] = this.commandHistory[2];
			this.commandHistory[2] = command;
		}
	}
		
	//=== State Management ===//

    /**
     * Adds a message to the game display.
     *
     * @param message the message text
     * @param clear true to clear previous messages, false to append
     * @param isLong true if the message is long
     */	
	public void addMessage(String message, boolean clear, boolean isLong) {
		game.addMessage(message,clear,isLong);
	}

    /** @return the player's remaining time as a string */
	public String getTime() {
		return player.toStringTimeRemaining();
	}

    /** @return the player's status as a string */
	public String getStatus() {
		return player.toStringStatus();
	}

    /**
     * Returns a description of the player's current room.
     *
     * @return the room description
     */
	public String getRoom() {	
		return String.format("You are %s",game.getRoomName(player.getDisplayRoom()));	
	}

    /**
     * Returns a list of items in the player's current room.
     *
     * @return a string representation of items
     */
	public String getItems() {
		
		String itemDisplay = "";
		
		if (player.isPlayerStateNormal()) {
			itemDisplay = game.getItems(player.getDisplayRoom());
		}
		
		return itemDisplay;
	}
	
    /** @return the game object */
	public Game getGame() {
		return this.game;
	}
	
	/** @return the player object */
	public Player getPlayer() {
		return this.player;
	}
	
    /**
     * Returns the exits of the player's current room.
     *
     * @return a string describing the available exits
     */
	public String getExits() {
		
		String exitDisplay = "";
		
		if (player.isPlayerStateNormal()) {
			exitDisplay = game.getExits(player.getRoom());
		}		
		
		return exitDisplay;
	}

    /**
     * Returns special exits of the player's current room.
     *
     * @return a string describing special exits
     */
	public String getSpecialExits() {

		String exitDisplay = "";
		
		if (player.isPlayerStateNormal()) {
			exitDisplay = game.getSpecialExits(player.getRoom());
		}		
		
		return exitDisplay;		
		
	}

    /**
     * Returns the current game messages.
     *
     * @return a list of messages
     */
	public List<String> getMessage() {
		return game.getNormalMessage();
	}
		
    /** @return the last three commands entered */
	public String[] getCommands() {
		return this.commandHistory;
	}
	
    //=== Load Game Position ===//
	
	@Override
	public void increaseLoadPosition() throws IOException {
		this.game.increaseCount();
		processCommand("load");
	}
		
	@Override
	public void decreaseLoadPosition() throws IOException {
		this.game.descreaseCount();
		processCommand("load");
	}
	
    /** @return true if lower limit of saved games is reached */
	public boolean getLowerLimitSavedGames() {
		return game.getLowerLimitSavedGames();
	}
	
    /** @return true if upper limit of saved games is reached */
	public boolean getUpperLimitSavedGames() {
		return game.getUpperLimitSavedGames();
	}
	
	/** @return displayed saved games as a string array */
	public String[] getDisplayedSavedGames() {
		return game.getDisplayedSavedGames();
	}
	
	//=== Game State Setters ===//
	
	public void setRunningGameState() {
		game.setRunningGameState();
	}
	
	public void setSavedGameState() {
		game.setSavedGameState();
	}
		
	public void setMessageState() {
		game.setMessageGameState();
	}
	
	//=== Game State Setters ===//
	
	public boolean isInitialGameState() {
		return game.isInitialGameState();
	}
	
	public boolean isSavedGameState() {
		return game.isSavedGameState();
	}
	
	public boolean isEndGameState() {
		return game.isEndGameState();
	}
	
	public boolean isRestartGameState() {
		return game.isRestartGameState();
	}
	
	public boolean isRunningState() {
		return game.isRunningState();
	}
	
	public boolean isMessageState() {
		return game.isMessageState();
	}
	
	public boolean isNormalState() {
		return player.isPlayerStateNormal();
	}
	
    /**
     * Calculates the player's final score based on stats.
     *
     * @return the final score
     */
	public int getFinalScore() {
		boolean timeBonus = (int) player.getStat("timeRemaining")<640;
		double timeScore = (int) player.getStat("timeRemaining")/7.0;
		double applyTimeBonus = timeBonus ? -timeScore:0;
		int wisdom = (int) player.getStat("wisdom");
		return (int) ((int) ((float) player.getStat("strength"))+wisdom+applyTimeBonus);
	}
	
	//=== Room Queries ===//
	
	@Override
	public boolean getRoomVisited(int roomNumber) {
		return game.getRoomVisited(roomNumber);
	}

	@Override
	public boolean[] getRoomExits(int roomNumber) {
		return game.getRoomExits(roomNumber);
	}

	@Override
	public String getRoomImageType(int roomNumber) {
		return game.getRoomImageType(roomNumber);
	}
	
	@Override
	public String getRoomName(int roomNumber) {
		return game.getRoomName(roomNumber);
	}

	@Override
	public int getCurrentRoom() {
		return player.getRoom();
	}

	@Override
	public void setRoom(int locationID) {
		player.setRoom(locationID);
	}

	@Override
	public List<String> getPanelMessage() {
		return game.getPanelMessage();
	}
} 

/*
3 December 2025 - Created File
7 December 2025 - Removed game related code
8 December 2025 - Increased version number
*/