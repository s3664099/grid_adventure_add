/*
Title: <Game Name> Constant Class
Author: 
Translator: David Sarkies
Version: 1.0
Date: 8 December 2025
Source: 

This class is designed to hold the constants. They have been made public since they
do not change, and it makes them easily accessible
*/

package data;

public class Constants {

    // Prevent instantiation (private constructor)
    private Constants() {
        throw new UnsupportedOperationException("Constants - Utility class");
    }
	
	//Game related constants
	public static final int NUMBER_OF_ROOMS = 0;
	public static final int NUMBER_OF_ITEMS = 0;
	public static final int NUMBER_OF_VERBS = 0;
	public static final int NUMBER_OF_NOUNS = 0;
	public static final int NUMBER_EXITS = 4;
	public static final int FLAG_HIDDEN = 9;
	public static final int INITIAL_SAVE_COUNT = 2;
	
	//Panel Related constants
	public static final int MESSAGE_LENGTH = 100;
	
	//Threshold for item categories in the item list
	public static final int MAX_CARRIABLE_ITEMS = 0; // Items with IDs <= 24 are carriable
	public static final int FOOD_THRESHOLD = 0; // Items with IDs >16 are food
	public static final int DRINK_THRESHOLD = 0; // Items with IDs > 21 are drinks
	public static final int LINE_LENGTH = 90;
	
	//Constants for the Game Class
	public static final int START_LOCATION = 0;
	
	//Constants for the player starting values
	public static final float STARTING_STRENGTH = 0;
	public static final int STARTING_WISDOM = 0;
	public static final int STARTING_TIME = 0;
	
	public static final String NORTH = "North";
	public static final String SOUTH = "South";
	public static final String EAST = "East";
	public static final String WEST = "West";
	public static final String[] DIRECTIONS = {NORTH, SOUTH, EAST,WEST};

	public static final String STAT_STRENGTH = "strength";
	public static final String STAT_WISDOM = "wisdom";
	public static final String STAT_TIME = "timeRemaining";
}

/* 3 December 2025 - Created File
 * 6 December 2025 - Cleared values
 * 7 December 2025 - Made direction names public
 * 8 December 2025 - Increased version number
 */