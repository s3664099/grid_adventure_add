/*
Title: <Game Name> Game Specific Constant Class
Author: 
Translator: David Sarkies
Version: 0.1
Date: 5 December 2025
Source: 

This class is designed to hold the constants. They have been made public since they
do not change, and it makes them easily accessible
*/

package data;

public class GameEntities {

    // Prevent instantiation (private constructor)
    private GameEntities() {
        throw new UnsupportedOperationException("Constants - Utility class");
    }

	// === Rooms ===
	
	// === Items ===

	// === Commands ===
	
	// === Codes ===
	
	// === Directions ===
	public static final int NORTH = 1;
	public static final int SOUTH = 2;
	public static final int EAST = 3;
	public static final int WEST = 4;

}

/* 3 December 2025 - Created File
 * 6 December 2025 - Cleared contents
 */