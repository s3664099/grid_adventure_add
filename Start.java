/*
Title: <Game Name> Start
Author: 
Translator: 
Version: 1.0
Date: 8 December 2025
Source: 
*/

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Entry point for the adventure game.
 * 
 * This class is responsible for:
 * Configuring logging to an external file
 * Initializing and launching the game
 * Handling startup errors and logging critical failures
 */
public class Start {
	
	private static final Logger logger = Logger.getLogger(Start.class.getName());
	
    /**
     * Main method. Initializes logging and launches the game.
     *
     * @param args Command-line arguments (unused)
     */
	public static void main(String[] args) {
		
		 // Set up logging to file
		try {
			FileHandler fileHander = new FileHandler("mylog.log",true);
			fileHander.setFormatter(new SimpleFormatter());
			fileHander.setLevel(Level.ALL);
			logger.addHandler(fileHander);
			
		
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "An error occured while creating the log: "+e.getMessage());
		} 

		// Start the game
		try {
			logger.log(Level.INFO, "Starting the game ...");
			
			Main game = new Main();
			game.startGame();
			
			logger.log(Level.INFO, "Game started successfully");
			
		} catch (Exception e) {
			
			//Log the exception with details
			logger.log(Level.SEVERE, "An error occured while starting the game: "+e.getMessage());
			
			System.err.println("Failed to start the game. Please check the logs for more details");
			System.exit(1);
		}
	}
}

/*
3 December 2025 - Created File
4 December 2025 - Added title back
8 December 2025 - Increased version number
*/
