/*
Title: <Game Name> Game Button
Author: 
Translator: David Sarkies
Version: 1.0
Date: 8 December 2025
Source: 
*/

package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import view.GamePanel;

/**
 * An ActionListener for switching the UI to the main game view.
 * Encapsulates the action of showing the main view in {@link GamePanel}.
 */
public class ShowMainViewButton implements ActionListener {

	private static final Logger logger = Logger.getLogger(ShowMainViewButton.class.getName());
	private final GamePanel panel;
	
    /**
     * Constructs a GameButton that switches to the main game view when clicked.
     *
     * @param panel the GamePanel to display the main view; must not be null
     * @throws NullPointerException if panel is null
     */
	public ShowMainViewButton(GamePanel panel) {
		this.panel = Objects.requireNonNull(panel, "GamePanel cannot be null");		
	}
	
    /**
     * Invoked when the button is pressed. Attempts to show the main view,
     * logging any unexpected errors to prevent UI crashes.
     *
     * @param event the ActionEvent triggered by the button press
     */
	@Override
	public void actionPerformed(ActionEvent event) {
        try {
            panel.showMainView();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to show main game view", e);
        }
	}
}

/* 3 December 2025 - Created File
 * 8 December 2025 - Increased version number
 */
