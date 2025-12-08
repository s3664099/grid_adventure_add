/*
Title: <Game Name> Move Command
Author: 
Translator: David Sarkies
Version: 1.0
Date: 8 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package commands;

import data.Constants;
import data.GameEntities;
import game.Game;
import game.Player;

import command_process.ActionResult;
import command_process.ParsedCommand;

/**
 * Handles player movement logic, including parsing movement commands,
 * validating moves, applying restrictions, and executing room transitions.
 */
public class Move {
	
    /** Direction modifiers for room transitions (N, S, E, W). */
	private static final int[] DIRECTION_MODIFIERS = {-10, +10, +01, -01};
		
    /**
     * Normalizes a parsed move command by resolving direction, special rooms,
     * and coded commands into a usable noun number.
     *
     * @param command the parsed command to normalize
     * @param room the player's current room
     * @return a normalized {@link ParsedCommand}
     */
	public ParsedCommand normaliseMoveCommand(ParsedCommand command,int room) {
		
		int verbNumber = command.getVerbNumber();
		int nounNumber = command.getNounNumber();

		if(nounNumber>GameEntities.MOVE_NOT_DIRECTION) {
			nounNumber -= Constants.NUMBER_OF_ITEMS;
		}
		
		return new ParsedCommand(verbNumber,nounNumber,command.getSplitTwoCommand(),command.getCommand());
	}
	
    /**
     * Resolves single-direction commands where the noun may be omitted.
     *
     * @param nounNumber the noun number from the command
     * @param verbNumber the verb number from the command
     * @return a corrected noun number representing a direction
     */
	public int parseSingleDirection(int nounNumber, int verbNumber) {

		if (nounNumber == -1) {
			nounNumber = verbNumber;
		} else if (nounNumber>Constants.NUMBER_OF_ITEMS && nounNumber<Constants.NUMBER_OF_NOUNS) {
			nounNumber = nounNumber-Constants.NUMBER_OF_ITEMS;
		}
		return nounNumber;
	}
	
    /**
     * Validates whether a move is possible based on the parsed command,
     * player state, and room conditions.
     *
     * @param command the move command
     * @param game the current game state
     * @param player the player attempting the move
     * @return an {@link ActionResult} describing validity and effects
     */
	public ActionResult validateMove(ParsedCommand command, Game game, Player player) {

		boolean validMove = true;
		int room = player.getRoom();
		ActionResult result = new ActionResult(game,player,validMove);
		
		if (isNotDirection(command)) {
			result = notDirection(game,player);
		} else if (isExitBlocked(game,room,command.getNounNumber())) {
			result = exitBlocked(game,player);
		} 
		
		return result;
	}
	
    /**
     * Executes a validated move by updating the player's room,
     * applying room entry effects, and generating messages.
     *
     * @param game the current game state
     * @param player the player making the move
     * @param command the parsed move command
     * @return an {@link ActionResult} describing the outcome
     */
	public ActionResult executeMove(Game game, Player player, ParsedCommand command) {
		
		ActionResult blockedCheck = evaluateMovementRestrictions(game,player,command);
		
		//Move is not blocked
		if (!blockedCheck.isValid()) {
			
			int direction = command.getNounNumber();
			int newRoom = calculateNewRoom(player.getRoom(),direction);
			player.setRoom(newRoom);
			game.addMessage("Ok",true,true);
			game.getRoom(newRoom).setVisited();			
			blockedCheck = handleRoomEntryEffects(game,player,command);
		}
		
		return blockedCheck;
	}
	
    /**
     * Calculates the new room index based on the current room and direction.
     *
     * @param currentRoom the player's current room
     * @param direction the movement direction
     * @return the new room index
     */
	private int calculateNewRoom(int currentRoom, int direction) {
		return currentRoom + DIRECTION_MODIFIERS[direction-1];
	}
	
    /**
     * Evaluates whether movement is blocked by entities, events,
     * or environmental restrictions.
     *
     * @param game the current game state
     * @param player the player attempting to move
     * @param command the parsed command
     * @return an {@link ActionResult} indicating if movement is blocked
     */
	private ActionResult evaluateMovementRestrictions(Game game, Player player, ParsedCommand command) {
		ActionResult result = new ActionResult(game,player,false);		
		return result;
	}
	
    /**
     * Handles effects that trigger upon entering a room (e.g., traps, ferry).
     *
     * @param game the current game state
     * @param player the player entering the room
     * @param command the parsed move command
     * @return an {@link ActionResult} describing room entry outcomes
     */
	private ActionResult handleRoomEntryEffects(Game game,Player player,ParsedCommand command) {
		ActionResult result = new ActionResult(game,player,true);
		return result;
	}
	
    /**
     * Checks if the command is not a valid direction.
     */
	private boolean isNotDirection(ParsedCommand command) {
		return command.getNounNumber()>GameEntities.MOVE_NOT_DIRECTION;
	}
	
    /**
     * Handles invalid directional commands.
     *
     * @return an {@link ActionResult} indicating invalid input
     */
	private ActionResult notDirection(Game game,Player player) {
		game.addMessage("I don't understand",true,true);
		return new ActionResult(game,player,false);
	}
	
    /**
     * Checks if there is no exit in the given direction.
     */
	private boolean isExitBlocked(Game game, int room, int nounNumber) {
		return !game.checkExit(room,nounNumber-1);
	}
	
    /**
     * Handles blocked exits (no exit in that direction).
     *
     * @return an {@link ActionResult} indicating failure
     */
	private ActionResult exitBlocked(Game game, Player player) {
		game.addMessage("You can't go that way",true,true);
		return new ActionResult(game, player, false);
	}
}

/* 3 December 2025 - Created File
 * 6 December 2025 - Removed game specific code
 * 8 December 2025 - Fixed errors
 * 				   - Increased version number
 */