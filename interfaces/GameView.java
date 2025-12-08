/*
Title: <Game Name> GameView Interface
Author: 
Translator: David Sarkies
Version: 1.0
Date: 8 December 2025
Source: 
*/

package interfaces;

import javax.swing.JComponent;

/**
 * Defines the contract for UI components that can be managed by a view controller.
 * Provides lifecycle hooks for view activation/deactivation and component access.
 */
public interface GameView {
	
    /**
     * Called when the view becomes active/visible.
     * Implementations should:
     * - Initialize resources
     * - Register listeners
     * - Update displayed content
     */
	default void onViewActivated() {};
	
    /**
     * Called when the view becomes inactive/hidden.
     * Implementations should:
     * - Clean up resources
     * - Unregister listeners
     * - Persist any state
     */
	default void onViewDeactivated() {};
	
    /**
     * Gets the root Swing component for this view.
     * @return The root JComponent that contains this view's UI elements
     */
	JComponent getViewComponent();
}

/* 3 December 2025 - Created File
 * 8 December 2025 - Increased version
 */
