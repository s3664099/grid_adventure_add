/*
Title: <Game Name> Command Validator
Author: 
Translator: David Sarkies
Version: 1.0
Date: 8 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package command_process;

import java.util.logging.Logger;

import data.Constants;
import game.Game;
import game.Player;

/**
 * Validates parsed player commands and ensures they are executable within the game rules. 
 * 
 * <p>This class checks for invalid or incomplete commands, provides feedback to the player,
 * and delegates valid commands to the appropriate handlers (e.g., movement, item actions, consumption). 
 * Special handling is included for game-specific entities such as trapdoors.</p>
 */
public class CommandValidator {

	private static final Logger logger = Logger.getLogger(Game.class.getName());
	
    /**
     * Validates a parsed command against the current game and player state.
     *
     * @param command the parsed player command
     * @param game the current game instance
     * @param player the player issuing the command
     * @return an {@link ActionResult} representing the outcome of the validation
     */
	public ActionResult validateCommand(ParsedCommand command, Game game, Player player) {

		boolean validCommand = true;
		
		if (checkVerbAndNounInvalid(command)) {
			logger.info("Validation Failed - Verb & Noun Invalid");
			validCommand = false;
			game = handleCheckVerbAndNounInvalidFails(game);
		} else if (checkVerbOrNounInvalid(command)) {
			logger.info("Validation Failed - Verb or Noun Invalid");
			validCommand = false;
			game = handleVerbOrNounInvalidFails(game,command);
		} else if (checkMissingNoun(command)) {
			logger.info("Validation Failed - Noun Missing");
			validCommand = false;
			game = handleMissingNounFails(game);
		} else if (checkNone(command)) {
			logger.info("Validation Failed - Missing Noun Failed");
			validCommand = false;
			game = handleCheckNoneFails(game);
		} else if (isNounMissing(command)) {
			logger.info("Validation Failed - Is Noun Missing True");
			validCommand = false;
			game = handleCheckNounFails(game);
		}
		
		ActionResult result = new ActionResult(game,player,validCommand);
		logger.info("Command Valid: "+validCommand);
		
		if (checkResultNull(result)) {
			result = new ActionResult(result.getGame(),player,result.isValid());
		}
		
		return result;
	}
			
    // ===== Command checks =====

    /**
     * @return true if the command contains an invalid verb or noun.
     */
	private boolean checkVerbOrNounInvalid(ParsedCommand command) {
		return (command.getVerbNumber()>Constants.NUMBER_OF_VERBS ||
				command.getNounNumber() == Constants.NUMBER_OF_NOUNS);
	}
	
    /**
     * @return true if both the verb and noun are invalid.
     */
	private boolean checkVerbAndNounInvalid(ParsedCommand command) {

		return (command.getVerbNumber()>Constants.NUMBER_OF_VERBS && 
				command.getNounNumber() == Constants.NUMBER_OF_NOUNS);
	}
	
    /**
     * @return true if a noun is required but missing.
     */
	private boolean checkMissingNoun(ParsedCommand command) {
		return (command.checkMultipleCommandState() && !command.checkNounLength());
	}
	
    /**
     * @return true if the command is unrecognized.
     */
	private boolean checkNone(ParsedCommand command) {
		return (command.checkNoneCommandType());
	}
	
    /**
     * @return true if the noun is missing in a multi-word or movement command.
     */
	private boolean isNounMissing(ParsedCommand command) {
		boolean validCommand = false;
		if (command.checkMultipleCommandState()||
			(command.checkMoveState() && command.getSplitTwoCommand()[0].equals("go"))) {
			if(command.getSplitFullCommand().length==1) {
				validCommand = true;
			}
		}
		return validCommand;
	}
	
    /**
     * @return true if the result is invalid and lacks a player reference.
     */
	private boolean checkResultNull(ActionResult result) {
		return result.getPlayer()==null && !result.isValid();
	}
					
    // ===== Error handling =====

	/** Adds a "You can't do that" message when verb/noun invalid. */
	private Game handleVerbOrNounInvalidFails(Game game, ParsedCommand command) {
		game.addMessage("You can't "+command.getCommand(), true, true);
		return game;
	}

    /** Adds a generic "What!!" message for both verb and noun invalid. */
	private Game handleCheckVerbAndNounInvalidFails(Game game) {
		game.addMessage("What!!",true,true);
		return game;
	}
	
    /** Adds a missing noun message. */
	private Game handleMissingNounFails(Game game) {
		game.addMessage("Most commands need two words", true, true);
		return game;
	}

	/** Adds an "I don't understand" message. */
	private Game handleCheckNoneFails(Game game) {
		game.addMessage("I don't understand", true, true);
		return game;
	}

    /** Adds a two-word requirement message. */
	private Game handleCheckNounFails(Game game) {
		game.addMessage("Most commands need two words",true,true);
		return game;
	}
}

/* 3 December 2025 - Created File
 * 5 December 2025 - Removed game specific code
 * 6 December 2025 - Removed game specific methods
 * 8 December 2025 - Increased version number
 */
