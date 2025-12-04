/*
Title: <Game Name> Command Validator
Author: 
Translator: David Sarkies
Version: 0.1
Date: 5 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package command_process;

import java.util.logging.Logger;

import commands.Consume;
import commands.ItemCommands;
import commands.Move;
import data.Constants;
import data.GameEntities;
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
		logger.info("Command Valid: "+validCommand+" Code: "+command.getCodedCommand());
		
		if (validCommand) {
			result = specialCommandValidator(command,result);
		} else {
			result = specialNounValidator(command,result);
		}
		
		if (checkResultNull(result)) {
			result = new ActionResult(result.getGame(),player,result.isValid());
		}
		
		return result;
	}
	
    /**
     * Applies specialized command checks for movement, item interactions, 
     * and unique entities like trapdoors.
     *
     * @param command the parsed player command
     * @param result the current validation result
     * @return the updated {@link ActionResult} after applying special rules
     */
	private ActionResult specialCommandValidator(ParsedCommand command,ActionResult result) {
		
		Game game = result.getGame();
		Player player = result.getPlayer();
		
		return result;
	}
	
	/**
     * Applies specialized command checks for nouns that are not listed as the main nouns, 
     *
     * @param command the parsed player command
     * @param result the current validation result
     * @return the updated {@link ActionResult} after applying special rules
     */
	private ActionResult specialNounValidator(ParsedCommand command,ActionResult result) {
		Game game = result.getGame();
		Player player = result.getPlayer();		
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
		
    // ===== State checks =====
	
	private boolean checkMoveState(ParsedCommand command) {
		return command.checkMoveState();
	}
	
	private boolean checkTakeState(ParsedCommand command) {
		return command.checkTake();
	}
	
	private boolean checkDropOrGive(ParsedCommand command) {
		return command.checkDrop() || command.checkGive();
	}
	
	private boolean checkGive(ParsedCommand command,ActionResult result) {
		return command.checkGive() && result.isValid();
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
		
	   // ===== Validators for specific actions =====
	
	private ActionResult validateMoveCommand(ParsedCommand command, Game game, Player player) {
		Move moveValidator = new Move();
		return moveValidator.validateMove(command,game,player);
	}
	
	private ActionResult validateTakeCommand(ParsedCommand command, Game game, Player player) {
		ItemCommands takeValidator = new ItemCommands();
		return takeValidator.validateTake(game, player, command);
	}
	
	private ActionResult validateDropOrGive(ParsedCommand command, Game game, Player player) {
		ItemCommands carryingValidator = new ItemCommands();
		ActionResult result = carryingValidator.validateCarrying(game,player,command);
		if (checkGive(command,result)) {
			result = validateGive(command,game,player,carryingValidator);
		}
		return result;
	}
	
	private ActionResult validateGive(ParsedCommand command, Game game, Player player,ItemCommands carryingValidator) {
		return carryingValidator.validateGive(game, player, command);
	}
	
	private ActionResult validateExamineNoun(Game game, Player player) {
		return new ActionResult(game,player,true);
	}
}

/* 3 December 2025 - Created File
 * 5 December 2025 - Removed game specific code
 */
