/*
Title: <Game Name> Command Processor
Author: 
Translator: David Sarkies
Version: 1.0
Date: 8 December 2025
Source:
*/

package command_process;

import game.Game;
import game.Player;
import java.util.logging.Logger;

/**
 * The {@code CommandProcessor} class orchestrates the handling of player input by 
 * coordinating parsing, validation, and execution of commands in the game. 
 * <p>
 * It follows a three-step process:
 * <ol>
 *   <li>Parse the raw input into a structured {@link ParsedCommand}.</li>
 *   <li>Validate the parsed command against the game state and player state.</li>
 *   <li>Execute the validated command to produce an {@link ActionResult}.</li>
 * </ol>
 * <p>
 * This class also logs key steps of command processing for debugging and monitoring.
 */
public class CommandProcessor {

	/** Responsible for converting raw player input into structured commands. */
	private final CommandParser parser;

    /** Validates parsed commands against the current game and player state. */
	private final CommandValidator validator;
	
	/** Executes validated commands and applies their effects to the game state. */
	private final CommandExecutor executor;
	
    /** Logger for recording command processing events and errors. */
	private static final Logger logger = Logger.getLogger(CommandExecutor.class.getName());
	
    /**
     * Constructs a new {@code CommandProcessor} with its own parser, validator,
     * and executor instances.
     */
	public CommandProcessor() {
		this.parser = new CommandParser();
		this.validator = new CommandValidator();
		this.executor = new CommandExecutor();
	}
	
    /**
     * Processes a raw player command through parsing, validation, and execution.
     * <p>
     * Logs each stage of the process, including input, parsed command, and validation results.
     * If an exception occurs during processing, it will be logged and a failed 
     * {@link ActionResult} will be returned.
     *
     * @param rawInput the raw string input entered by the player
     * @param game     the current {@link Game} instance
     * @param player   the {@link Player} issuing the command
     * @return an {@link ActionResult} representing the outcome of the command
     */
	public ActionResult execute(String rawInput,Game game, Player player) {
		
		ActionResult result = new ActionResult();
		
		try {
			logger.info("Executing command: " + rawInput);
			ParsedCommand command = parser.parse(rawInput, game,player.getRoom());
			logger.info("Parsed command: " + command.getCommand());
			result = validator.validateCommand(command,game,player);
			logger.info("Validation result: " + result.isValid());
		
			if(result.isValid()) {
				result = executor.executeCommand(game,player,command);
			}
		} catch (Exception e) {
			logger.severe("An error occurred while processing the command.");
	        result = new ActionResult(game, player, false);
		}
		return result;
	}
}

/* 3 December 2025 - Created File
 * 6 December 2025 - Removed Coded Command
 * 8 December 2025 - Increased version number
 */
