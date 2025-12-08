/*
Title: <Game Name> Command Parser
Author: 
Translator: David Sarkies
Version: 1.0
Date: 8 December 2025
Source: 
*/

package command_process;

import commands.Move;
import data.Constants;
import data.GameEntities;
import data.RawData;
import game.Game;

/**
 * CommandParser is responsible for parsing raw user input into structured
 * {@link ParsedCommand} objects that the game engine can interpret.
 * <p>
 * It handles normalization of shorthand commands, identification of verbs and nouns,
 * and special cases such as movement, eating, and looking at objects.
 */
public class CommandParser {
		
	private final CommandNormaliser normaliser;
	
	/**
	 * Constructs a new {@code CommandParser} with its own internal
	 * {@link CommandNormaliser}.
	 */
	public CommandParser() {
		normaliser = new CommandNormaliser();
	}
	
	/**
	 * Parses the raw input string into a {@link ParsedCommand}, performing
	 * normalization, verb/noun lookup, and context-sensitive adjustments.
	 *
	 * @param rawInput the raw user input string
	 * @param game the current game instance, used for item lookups and state checks
	 * @param room the current room identifier where the command is issued
	 * @return a fully parsed {@link ParsedCommand} instance
	 */
	public ParsedCommand parse(String rawInput, Game game, int room) {
		
		rawInput = normaliser.normalise(rawInput);
		rawInput = parseMovement(rawInput);
		String[] splitCommand = splitCommand(rawInput);
		int verbNumber = getVerbNumber(splitCommand[0]);
							
		splitCommand[1] = splitCommand[1].trim();
		int nounNumber = getNounNumber(splitCommand[1],verbNumber);
		ParsedCommand command = new ParsedCommand(verbNumber,nounNumber,splitCommand,rawInput);

		if (command.checkMoveState()) {
			command = parseMove(command,room);
		} 
		return command;
	}
	
	/**
	 * Splits a raw command string into verb and noun components.
	 * <p>
	 * Always returns an array of length 2. The first element is the verb,
	 * the second is the remainder (noun phrase or empty string).
	 *
	 * @param rawInput the normalized command string
	 * @return a two-element array: [verb, noun phrase]
	 */
	private String[] splitCommand(String rawInput) {
		
		String[] splitCommand = {"",""};
		String[] commands = rawInput.split(GameEntities.SPACE);
		splitCommand[0] = commands[0];
		
		if(commands.length>1) {
			splitCommand[1] = rawInput.substring(commands[0].length(),rawInput.length());
		}
		
		return splitCommand;
	}
	
	/**
	 * Retrieves the verb index corresponding to a given verb string.
	 * <p>
	 * Verbs are matched against the list provided by {@link RawData#getVerbs()}.
	 *
	 * @param verb the verb string to look up
	 * @return the verb number if found, otherwise {@code Constants.NUMBER_OF_VERBS + 1}
	 */
	private int getVerbNumber(String verb) {
		
		int verbNumber = Constants.NUMBER_OF_VERBS+1;
		int verbCount = 0;
				
		for (String command:RawData.getVerbs()) {
			verbCount ++;
			
			if (verb.equals(command)) {
				verbNumber = verbCount;
			}
		}
		
		return verbNumber;
	}
	
	/**
	 * Retrieves the noun index corresponding to a given noun string.
	 * <p>
	 * Handles single-word and multi-word nouns, as well as special cases such
	 * as directional movement commands.
	 *
	 * @param noun the noun string to look up
	 * @param verbNumber the associated verb number, used for context-sensitive resolution
	 * @return the noun number if found, otherwise -1
	 */
	private int getNounNumber(String noun,int verbNumber) {
		
		int nounNumber = Constants.NUMBER_OF_NOUNS;
				
		//Only called if more than two words
		if (noun.length()>1) {
			//Does not contain more than one word?
			if (noun.split(GameEntities.SPACE).length>1) {
				noun = noun.split(" ")[0];
			}
			
			int nounCount = 0;
			for (String command:RawData.getNouns()) {
				nounCount ++;
				if (noun.equals(command)) {
					nounNumber = nounCount;
				}
			}
		} else {
			nounNumber = -1;
			if(verbNumber>0 && verbNumber<5) {
				nounNumber = new Move().parseSingleDirection(nounNumber, verbNumber);
			} else if (verbNumber==GameEntities.CMD_GO && nounNumber>0 && nounNumber<7) {
				nounNumber = 8;
			}
		}
				
		return nounNumber;
	}
			
	/**
	 * Delegates movement command parsing to the {@link Move} class.
	 *
	 * @param command the parsed command to refine
	 * @param room the current room identifier
	 * @return the updated {@link ParsedCommand} after movement parsing
	 */
	private ParsedCommand parseMove(ParsedCommand command,int room) {
		return new Move().normaliseMoveCommand(command, room);
	}
		
	/**
	 * Helper class that normalizes shorthand or alternative user inputs
	 * into canonical command forms.
	 */
	private class CommandNormaliser {
		
		/**
		 * Converts shorthand, synonyms, or alternate command forms into
		 * normalized commands understood by the parser.
		 * <p>
		 * For example, "u" or "up" become "go up", "north" becomes "n", etc.
		 *
		 * @param input the raw user input
		 * @return the normalized command string
		 */
		public String normalise(String input) {

			input = input.toLowerCase();
			
			if (input.equals("u") || input.equals("up")) {
				input = "go up";
			} else if (input.equals("d") || input.equals("down")) {
				input = "go down";
			} else if (input.equals("i") || input.equals("enter") ||
					input.equals("inside") || input.equals("go inside")) {
				input = "go in";
			} else if (input.equals("o") || input.equals("exit") ||				
					input.equals("outside") || input.equals("go outside")) {
				input = "go out";
			} else if (input.equals("north")) {
				input = "n";
			} else if (input.equals("south")) {
				input = "s";
			} else if (input.equals("east")) {
				input = "e";
			} else if (input.equals("west")) {
				input = "w";
			}			
			return input;
		}
	}
	
	/**
	 * Ensures movement-related commands ("in", "out", "up", "down") are prefixed
	 * with "go" for consistency.
	 *
	 * @param command the input command string
	 * @return the normalized movement command string
	 */
	public String parseMovement(String command) {
		
		if (command.equals("in") || command.equals("out") ||
			command.equals("up") || command.equals("down")) {
			command = "go "+command;
		}
				
		return command;
	}
}

/* 3 December 2025 - Created File
 * 5 December 2025 - Removed game specific code
 * 6 December 2025 - Removed coded command
 * 8 December 2025 - Increased version number
 */