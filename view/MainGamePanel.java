/*
Title: <Game Name> Main Game Panel
Author: 
Translator: David Sarkies
Version: 1.0
Date: 8 December 2025
Source: 
*/

package view;

import java.awt.BorderLayout;
import java.util.Objects;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import interfaces.GameStateProvider;
import interfaces.GameUI;
import interfaces.GameView;
import ui.GameController;

/**
 * Main game view panel combining status, room, and command components.
 * <p>
 * This panel serves as the primary in-game view, displaying the player’s
 * current status, the room description, and a command input area. It
 * implements both {@link GameUI} and {@link GameView} so that it can be
 * refreshed by the controller and participate in the card-layout view
 * switching handled by {@link GamePanel}.
 * </p>
 */ 
class MainGamePanel extends JPanel implements GameUI, GameView {
	
	private static final long serialVersionUID = -9087851496246015145L;
	
    /** Game controller that drives state updates and handles commands. */
	private final GameController controller;
	
    /** Parent container that manages multiple game views. */
	private final GamePanel parentPanel;
	
    /** Displays current player status such as score, health, etc. */
	private StatusPanel statusPanel;
	
    /** Displays the current room description and related visuals. */
	private RoomPanel roomPanel;
	
    /** Provides the command input area for player actions. */
	private CommandPanel commandPanel;
	
    /** Current immutable snapshot of the game state. */
	private GameStateProvider state;
	
    /** Indicates whether child components have been created and added. */
	private boolean isInitialised = false;

    /**
     * Constructs the main game panel.
     *
     * @param controller the {@link GameController} managing game logic,
     *                   must not be {@code null}
     * @param panel      the parent {@link GamePanel} managing this view,
     *                   must not be {@code null}
     * @throws NullPointerException if {@code controller} or {@code panel} is null
     */
	public MainGamePanel(GameController controller, GamePanel panel) {
		
		this.state = controller.getState();
        this.controller = Objects.requireNonNull(controller, "GameController cannot be null");
        this.parentPanel = Objects.requireNonNull(panel, "Parent panel cannot be null");
        
        setLayout(new BorderLayout());
	}
	
    /**
     * Called when this view is activated (brought to the foreground).
     * Lazily initialises sub-components on first activation and refreshes the UI.
     */
	@Override
	public void onViewActivated() {
		if (!isInitialised) {
			initialiseComponents();
			isInitialised=true;
		}
		
		if (controller.isMessageState()) {
			controller.setMessageState();
		} else if (state.isSavedGameState()) {
			controller.setRunningGameState();
		}
		
		refreshUI(controller);
	}
	
    /**
     * Creates and adds all sub-components (status, room, command panels).
     * Called only once, during the first activation of the view.
     */
	public void initialiseComponents() {
				
		// Top section for status and label panels
		statusPanel = new StatusPanel(state);
		roomPanel = new RoomPanel(state);
		commandPanel = new CommandPanel(controller,state,parentPanel);
		
		this.add(statusPanel, BorderLayout.NORTH); 
		this.add(roomPanel,BorderLayout.CENTER);
		this.add(commandPanel,BorderLayout.SOUTH);
	}

    /**
     * Refreshes all child panels to reflect the latest game state.
     * Ensures updates occur on the Swing Event Dispatch Thread.
     *
     * @param game the current {@link GameController} providing fresh state
     */
	@Override
	public void refreshUI(GameController game) {
		
		SwingUtilities.invokeLater(()-> {
			this.state = game.getState();
			statusPanel.refreshUI(state);
			roomPanel.refreshUI(state);
			commandPanel.refreshUI(state);
			
			revalidate();
			repaint();
			commandPanel.requestCommandFocus();
		});
	}

    /**
     * No-op for this view. Map switching is handled by the parent container.
     *
     * @param game ignored
     */
	@Override
	public void showMapView(GameController game) {}

    /**
     * Called when this view is deactivated.
     * Currently does nothing but may be extended for cleanup.
     */
	@Override
	public void onViewDeactivated() {}

    /**
     * Returns this panel as the Swing component representing the view.
     *
     * @return this {@code MainGamePanel} instance
     */
	@Override
	public JComponent getViewComponent() {
		return this;
	}

    /**
     * Closes the view and marks it uninitialised,
     * allowing components to be rebuilt if needed later.
     */
	@Override
	public void closeUI() {
        isInitialised = false;
	}
}

/* 3 December 2025 - Created File
 * 8 December 2025 - Increased version
 */