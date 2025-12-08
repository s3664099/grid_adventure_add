/*
Title: <Game Name> Main 
Author: 
Translator: David Sarkies
Version: 1.0
Date: 8 December 2025
Source: 
*/

import javax.swing.SwingUtilities;

import game.Game;
import game.GameEngine;
import game.GameInitialiser;
import game.Player;
import view.GameFrame;

/**
 * Entry point for the adventure game.
 * Initializes game data and launches the user interface.
 */
public class Main {
	
    /**
     * Starts the game by initializing core data structures and launching the UI.
     */
	public void startGame()  {

		try {
			
			//Initialises the game data
			Game gameData =  GameInitialiser.initialiseGame();
			Player player = new Player();
			GameEngine game = new GameEngine(gameData,player);
			
			//Launch UI
			SwingUtilities.invokeLater(() -> {
				try {
					new GameFrame(game);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("Main - Failed to launch game UI: "+e.getMessage());
				}
			});

		} catch (Exception e) {
			System.err.println("Failed to start the game: " + e.getMessage());
			e.printStackTrace();
		}
	}
}

/*
3 December 2025 - Created File
4 December 2025 - Added Title Name
8 December 2025 - Increased version number
*/