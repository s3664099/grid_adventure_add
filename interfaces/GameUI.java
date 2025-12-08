/*
Title: <Game Name> GameUI interface
Author: 
Translator: David Sarkies
Version: 1.0
Date: 8 December 2025
Source: 
*/


package interfaces;

import ui.GameController;

/**
 * Defines the core user interface operations for the game.
 * Implementations handle UI updates, view management, and lifecycle operations.
 */
public interface GameUI {
	
    /**
     * Refreshes the entire UI with current game state.
     * @param controller The game controller providing current state and operations
     * @throws NullPointerException if controller is null
     */
	void refreshUI(GameController controller);
	
    /**
     * Activates and displays the map view.
     * @param controller The game controller providing map data and operations
     * @throws IllegalStateException if map cannot be displayed
     */
	void showMapView(GameController controller);
	
    /**
     * Closes and cleans up the UI resources.
     * Implementations should:
     * - Release any system resources
     * - Save persistent state
     * - Terminate any background operations
     */
	void closeUI();
}

/* 3 December 2025 - Created File
 * 8 December 2025 - Increased version
 */
