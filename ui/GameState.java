/*
Title: <Game Name> Game State
Author: 
Translator: David Sarkies
Version: 1.0
Date: 8 December 2025
Source:
*/

package ui;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import interfaces.GameStateProvider;

/**
 * Immutable snapshot of game state implementing GameStateProvider interface.
 * Thread-safe by design - all fields are final and collections are unmodifiable.
 */
public class GameState implements GameStateProvider {

	//Core game state
	private final String room;
	private final String items;
	private final String exits;
	private final String specialExits;
	private final String time;
	private final String status;
	
	//Game progression
	private final int currentRoom;
	private final int finalScore;
	private final boolean initialGameState;
	private final boolean saveGameState;
	private final boolean endGameState;
	private final boolean restartGameState;
	private final boolean runningState;
	private final boolean messageState;
	private final boolean normalState;
	
	//Saved games navigation
	private final String[] displayedSavedGames;
	private final boolean lowerLimit;
	private final boolean upperLimit;
	
	//Command history
	private final String[] previousCommands;
	private final List<String> message;
	
	//Delegate for dynamic data
	private final GameStateProvider stateProvider;
	
	public GameState(GameStateProvider stateProvider) {
		Objects.requireNonNull(stateProvider,"State provider cannot be null");
		
		this.stateProvider = stateProvider;
		
		//Core state
		this.room = stateProvider.getRoom();
		this.items = stateProvider.getItems();
		this.exits = stateProvider.getExits();
		this.specialExits = stateProvider.getSpecialExits();
		this.time = stateProvider.getTime();
		this.status = stateProvider.getStatus();
		
		//Game progression
		this.currentRoom = stateProvider.getCurrentRoom();
		this.finalScore = stateProvider.getFinalScore();
		this.initialGameState = stateProvider.isInitialGameState();
		this.saveGameState = stateProvider.isSavedGameState();
		this.endGameState = stateProvider.isEndGameState();
		this.restartGameState = stateProvider.isRestartGameState();
		this.runningState = stateProvider.isRunningState();
		this.messageState = stateProvider.isMessageState();
		this.normalState = stateProvider.isNormalState();
		
		//Saved games
		this.displayedSavedGames = stateProvider.getDisplayedSavedGames().clone();
		this.lowerLimit = stateProvider.getLowerLimitSavedGames();
		this.upperLimit = stateProvider.getUpperLimitSavedGames();
		
		//Command history
		this.previousCommands = stateProvider.getCommands().clone();
		this.message = Collections.unmodifiableList(stateProvider.getMessage());
	}
	
	//Implemented interface methods
	@Override
	public String getRoom() {
		return this.room;
	}

	@Override
	public String getItems() {
		return this.items;
	}

	@Override
	public String getExits() {
		return this.exits;
	}

	@Override
	public String getSpecialExits() {
		return this.specialExits;
	}
	
	@Override
	public String getTime() {
		return this.time;
	}

	@Override
	public String getStatus() {
		return this.status;
	}
	
	@Override
	public int getCurrentRoom() {
		return this.currentRoom;
	}
	
	@Override
	public int getFinalScore() {
		return this.finalScore;
	}
	
	@Override
	public boolean isInitialGameState() {
		return this.initialGameState;
	}

	@Override
	public boolean isSavedGameState() {
		return this.saveGameState;
	}

	@Override
	public boolean isEndGameState() {
		return this.endGameState;
	}
	
	@Override
	public boolean isRestartGameState() {
		return this.restartGameState;
	}
	
	@Override
	public String[] getDisplayedSavedGames() {
		return this.displayedSavedGames.clone();
	}

	@Override
	public boolean getLowerLimitSavedGames() {
		return this.lowerLimit;
	}

	@Override
	public boolean getUpperLimitSavedGames() {
		return this.upperLimit;
	}
	
	@Override
	public String[] getCommands() {
		return this.previousCommands.clone();
	}

	@Override
	public List<String> getMessage() {
		return this.message;
	}
	
	//Dynamic data delegation
	public String getRoomName(int roomNumber) {
		return stateProvider.getRoomName(roomNumber);
	}
	
	@Override
	public boolean getRoomVisited(int roomNumber) {
		return stateProvider.getRoomVisited(roomNumber);
	}

	@Override
	public boolean[] getRoomExits(int roomNumber) {
		return stateProvider.getRoomExits(roomNumber);
	}

	@Override
	public String getRoomImageType(int roomNumber) {
		return stateProvider.getRoomImageType(roomNumber);
	}
	
	@Override
	public String toString() {
		return String.format("GameStat[room=%d, score=%d",currentRoom,finalScore);
	}
	
	//For equality checks
	@Override
	public boolean equals(Object o) {
		final boolean isEqual;
		
		if (this==o) {
			isEqual = true;
		} else if (!(o instanceof GameState)) {
			isEqual = false;
		} else {
			GameState that = (GameState) o;
			isEqual = (currentRoom == that.currentRoom) &&
					(finalScore == that.finalScore) &&
					Objects.equals(room, that.room);
		}
		
		return isEqual;
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(currentRoom, finalScore, room);
	}
	
	@Override
	public boolean isRunningState() {
		return runningState;
	}

	@Override
	public boolean isNormalState() {
		return normalState;
	}

	@Override
	public boolean isMessageState() {
		return messageState;
	}

	@Override
	public List<String> getPanelMessage() {
		return stateProvider.getPanelMessage();
	}
}

/* 3 December 2025 - Created file
 * 8 December 2025 - Removed Game related code
 * 				   - Increased version
 */
