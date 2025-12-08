/*
Title: <Game Name> Parsed Command
Author:
Translator: David Sarkies
Version: 1.0
Date: 8 December 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package command_process;

import data.GameEntities;

/**
 * Represents a parsed player command in the game.
 * <p>
 * This class encapsulates the raw command string entered by the player,
 * its parsed verb/noun components, and the derived command state and type.
 * Commands are categorized as movement, single-command, or multi-command,
 * and mapped to specific {@link CommandType} values for handling.
 * </p>
 */
public class ParsedCommand {
	
	private final String command;
	private final String[] splitTwoCommand;
	private final String[] splitFullCommand;
	private final int verbNumber;
	private final int nounNumber;
	
    /**
     * Represents the general structure/state of a parsed command.
     */
	private enum CommandState {
		
        /** Command does not map to a known state. */
		NONE,
		
		/** Movement command (e.g. north, south, etc.). */
		MOVE,
		
		/** Standalone command with no noun (e.g. WAIT, SAVE, QUIT). */
		SINGLE_COMMAND, 
		
		/** Command requiring an additional noun (e.g. TAKE SWORD). */
		MULTIPLE_COMMAND 
	};
	
    /**
     * Represents the specific type of command identified by the parser.
     */
	private enum CommandType { NONE,TAKE,GIVE,DROP,LOAD,SAVE,QUIT,RESTART};

	private CommandState commandState = CommandState.NONE;
	private CommandType commandType = CommandType.NONE;
	
    /**
     * Creates a new parsed command instance.
     *
     * @param verbNumber   numeric identifier for the verb portion of the command
     * @param nounNumber   numeric identifier for the noun portion of the command
     * @param codedCommand encoded representation of the command
     * @param splitCommand split command array (up to two words: verb and noun)
     * @param command      the full raw command string as entered by the player
     */
	public ParsedCommand(int verbNumber, int nounNumber, String[] splitCommand, String command) {
				
		this.splitTwoCommand = splitCommand;
		this.splitFullCommand = command.split(" ");
		this.verbNumber = verbNumber;
		this.nounNumber = nounNumber;
		this.command = command;
		
		setState(verbNumber);
	}
	
    /**
     * Updates the command state based on a new verb number.
     *
     * @param verbNumber numeric identifier for the new verb
     */
	public void updateState(int verbNumber) {
		setState(verbNumber);
	}
	
    /**
     * Determines the {@link CommandState} of the current command based on the given verb number.  
     * <p>
     * - If the verb is within the MOVE range, the command is classified as {@link CommandState#MOVE}.  
     * - If the verb matches one of the recognized single-action commands (e.g., EAT, DRINK, WAIT),  
     *   the command is classified as {@link CommandState#SINGLE_COMMAND} and forwarded to 
     *   {@link #setSingleCommand(int)} for further classification.  
     * - Otherwise, the command is treated as a {@link CommandState#MULTIPLE_COMMAND} and delegated 
     *   to {@link #setMultipleCommand(int)}.  
     *
     * @param verbNumber the numeric identifier of the parsed verb, as defined in {@link GameEntities}
     */
	private void setState(int verbNumber) {
		
		if (verbNumber>GameEntities.MOVE_BOTTOM && verbNumber<GameEntities.MOVE_TOP) {
			commandState = CommandState.MOVE;
		} else if (verbNumber == GameEntities.CMD_LOAD || verbNumber == GameEntities.CMD_SAVE || 
				   verbNumber == GameEntities.CMD_QUIT || verbNumber == GameEntities.CMD_NORTH ||
					verbNumber == GameEntities.CMD_SOUTH || verbNumber == GameEntities.CMD_EAST ||
					verbNumber == GameEntities.CMD_WEST || verbNumber == GameEntities.CMD_RESTART) {
			commandState = CommandState.SINGLE_COMMAND;
			setSingleCommand(verbNumber);
		} else {
			commandState = CommandState.MULTIPLE_COMMAND;
			setMultipleCommand(verbNumber);
		}
	}
	
    /**
     * Assigns the {@link CommandType} for commands that operate independently (no target needed).  
     * Examples include simple actions like EAT, DRINK, WAIT, or SAVE.  
     * <p>
     * This method is called when {@link #setState(int)} identifies the command as a 
     * {@link CommandState#SINGLE_COMMAND}.
     *
     * @param verbNumber the numeric identifier of the verb to classify as a single command
     */
	private void setSingleCommand(int verbNumber) {
		
		if (verbNumber == GameEntities.CMD_SAVE) {
			commandType = CommandType.SAVE;
		} else if (verbNumber == GameEntities.CMD_QUIT) {
			commandType = CommandType.QUIT;
		} else if (verbNumber == GameEntities.CMD_RESTART) {
			commandType = CommandType.RESTART;
		}
	}
	
    /**
     * Assigns the {@link CommandType} for commands that usually involve a target object or entity.  
     * Examples include TAKE, GIVE, DROP, ATTACK, or EXAMINE.  
     * <p>
     * This method is called when {@link #setState(int)} identifies the command as a 
     * {@link CommandState#MULTIPLE_COMMAND}.
     *
     * @param verbNumber the numeric identifier of the verb to classify as a multiple command
     */
	private void setMultipleCommand(int verbNumber) {
		
		if (verbNumber == GameEntities.CMD_TAKE) {
			commandType = CommandType.TAKE;
		} else if (verbNumber == GameEntities.CMD_DROP) {
			commandType = CommandType.DROP;
		} 
	}
	
    // --------------------
    // Getters
    // --------------------

    /**
     * @return the numeric identifier of the verb portion
     */
	public int getVerbNumber() {
		return verbNumber;
	}
	
    /**
     * @return the numeric identifier of the noun portion
     */
	public int getNounNumber() {
		return nounNumber;
	}
	
    /**
     * @return the raw command string entered by the player
     */
	public String getCommand() {
		return command;
	}
		
    /**
     * @return an array of up to two elements: verb and noun
     */
	public String[] getSplitTwoCommand() {
		return splitTwoCommand;
	}
	
    /**
     * @return the full command split into tokens
     */
	public String[] getSplitFullCommand() {
		return splitFullCommand;
	}
	
    // --------------------
    // State checks
    // --------------------

    /**
     * Checks whether the noun part of the command is present and non-empty.
     *
     * @return true if a valid noun is supplied, false otherwise
     */
	public boolean checkNounLength() {
		return splitTwoCommand.length > 1 && !splitTwoCommand[1].isEmpty();
	}
	
    /**
     * @return true if this command is a movement command
     */
	public boolean checkMoveState() {
		return commandState == CommandState.MOVE;
	}
	
    /**
     * @return true if this command is a single-command (no noun required)
     */
	public boolean checkSingleCommandState() {
		return commandState == CommandState.SINGLE_COMMAND;
	}
	
    /**
     * @return true if this command is a multi-command (noun required)
     */
	public boolean checkMultipleCommandState() {
		return commandState == CommandState.MULTIPLE_COMMAND;
	}
	
    /**
     * @return true if the command type is {@link CommandType#NONE}
     */
	public boolean checkNoneCommandType() {
		return commandState == CommandState.NONE;
	}
	
    // --------------------
    // Command type checks
    // --------------------

    /** @return true if the command is a TAKE command */
	public boolean checkTake() {
		return commandType == CommandType.TAKE;
	}
	
    /** @return true if the command is a DROP command */
	public boolean checkDrop() {
		return commandType == CommandType.DROP;
	}
	
    /** @return true if the command is a GIVE command */
	public boolean checkGive() {
		return commandType == CommandType.GIVE;
	}
	
    /** @return true if the command is a LOAD command */
	public boolean checkLoad() {
		return commandType == CommandType.LOAD;
	}
	
    /** @return true if the command is a SAVE command */
	public boolean checkSave() {
		return commandType == CommandType.SAVE;
	}
	
    /** @return true if the command is a QUIT command */
	public boolean checkQuit() {
		return commandType == CommandType.QUIT;
	}
	
    /** @return true if the command is a RESTART command */
	public boolean checkRestart() {
		return commandType == CommandType.RESTART;
	}
}

/* 3 December 2025 - Created file
 * 5 December 2025 - Cleared game specific data
 * 6 December 2025 - Removed coded command
 * 8 December 2025 - Fixed errors and removed noun table & room functions
 * 				   - Increased version number
 */