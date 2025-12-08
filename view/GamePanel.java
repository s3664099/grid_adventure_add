/*
Title: <Game Name> Game Pabel
Author: 
Translator: David Sarkies
Version: 1.0
Date: 8 December 2025
Source: 
*/

package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ui.GameController;

/**
 * Main container panel that manages game views using CardLayout.
 * Handles transitions between the main game view and map view.
 */
public class GamePanel extends JPanel {
	
    private static final long serialVersionUID = -1175236419449166126L;
    
    //View Constants
	private static final String MAIN_VIEW = "MAIN";
	private static final String MAP_VIEW = "MAP";
	private static final String MESSAGE_VIEW = "MESSAGE";

	//UI Components
	private final CardLayout cardLayout = new CardLayout();
    private final JPanel viewContainer = new JPanel(cardLayout);
    private MainGamePanel mainView;
    private MapPanel mapView;
    private MessagePanel messageView;
	
    /**
     * Constructs the GamePanel and initializes all game sub-views.
     *
     * @param controller the {@link GameController} that provides
     *                   state updates and handles game commands;
     *                   must not be {@code null}
     */
	public GamePanel(GameController controller) {
		Objects.requireNonNull(controller, "GameController cannot be null");
		initialiseUI(controller);
	}
	
    /**
     * Creates and configures all sub-panels, adds them to the
     * CardLayout container, and sets the initial view.
     *
     * @param controller the active {@link GameController}
     */
	private void initialiseUI(GameController controller) {
		
		setLayout(new BorderLayout());
		//Create views
		this.mainView = new MainGamePanel(controller,this);
		this.mapView = new MapPanel(controller,this);
		this.messageView = new MessagePanel(this);

		//Configure view container
		viewContainer.add(mainView,MAIN_VIEW);
		viewContainer.add(mapView,MAP_VIEW);
		viewContainer.add(messageView,MESSAGE_VIEW);
		add(viewContainer,BorderLayout.CENTER);
		
		//Initial view
		showMainView();
	}
	
    /**
     * Refreshes the main game view
     * @param controller The game controller providing current state
     */
	public void refreshMainView(GameController controller) {
		if (controller.isMessageState()) {
			controller.setRunningGameState();
		}
		SwingUtilities.invokeLater(() -> mainView.refreshUI(controller));
	}
	
    /**
     * Refreshes the map view
     * @param controller The game controller providing current state
     */
	public void refreshMapView(GameController controller) {
		SwingUtilities.invokeLater(() -> mapView.refreshUI(controller));
	}
	
    /**
     * Refreshes the message view
     * @param controller The game controller providing current state
     */
	public void refreshMessageView(GameController controller) {
		SwingUtilities.invokeLater(() -> 
			messageView.displayMessages(controller.getState().getPanelMessage()));
	}
		
    /**
     * Shows the main game view and triggers activation
     */
	public void showMainView() {
		cardLayout.show(viewContainer,MAIN_VIEW);
		mainView.onViewActivated();
	}
	
    /**
     * Shows the map view and triggers activation
     */
	public void showMapView() {
		cardLayout.show(viewContainer, MAP_VIEW);
		mapView.onViewActivated();
	}
	
    /**
     * Shows the message view and triggers activation
     */
	public void showMessageView() {
		cardLayout.show(viewContainer, MESSAGE_VIEW);
		messageView.onViewActivated();
	}
}

/* 3 December 2025 - Created File
 * 8 December 2025 - Increased version
 */
