/*
Title: <Game Name> Special Exit Handler
Author: 
Translator: David Sarkies
Version: 1.0
Date: 8 December 2025
Source: 
*/

package game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import data.Constants;
import data.GameEntities;
import data.Item;

/**
 * Handles special exits for certain rooms in the adventure game.
 * <p>
 * Unlike normal exits, which are handled in a generic way, some rooms
 * have unique exits or additional descriptions that require custom logic.
 * This class manages those cases, ensuring the player receives the correct
 * directional and descriptive information when navigating.
 * </p>
 * 
 * <p>
 * Each special exit is stored in a {@link Map}, keyed by the room number
 * (from {@link GameEntities}). The value is a two-element {@code String[]},
 * where:
 * <ul>
 *   <li>Index 0 → The direction to suppress (the normal exit should not display).</li>
 *   <li>Index 1 → The description to display for the special exit.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * For example:  
 * {@code {WEST, "You can also go west into the cave"}}  
 * suppresses the default west exit, and instead provides a custom description.
 * </p>
 * 
 * This class is {@link Serializable} for compatibility with saved game states.
 */
public class SpecialExitHandler implements Serializable {

	private static final long serialVersionUID = 7662068425968354288L;

    /**
     * Stores special exits mapped to room numbers.
     * Each entry is a 2-element array:
     * <ul>
     *   <li>Index 0 → Exit direction (suppressed).</li>
     *   <li>Index 1 → Exit description (displayed).</li>
     * </ul>
     */
	private Map<Integer, String[]> specialExits = new HashMap<Integer, String[]>();
	
    /**
     * Constructs and populates the special exit mapping.
     * <p>
     * Loads all predefined room-to-exit associations from {@link GameEntities}.
     * These define the special cases where normal exits should be hidden
     * and replaced with custom descriptions.
     * </p>
     */
	public SpecialExitHandler() {
	   //specialExits.put(GameEntities.ROOM_STOREROOM, new String[]{"","There is a door to the east"});
	   //specialExits.put(GameEntities.ROOM_CAVE, new String[]{Constants.WEST,"You can also go west into the cave"});
	}
	
    /**
     * Determines whether a normal exit should be displayed.
     * <p>
     * If the given room has a special exit that overrides the specified direction,
     * this method will return {@code false} to prevent duplication.
     * Otherwise, the normal exit is shown.
     * </p>
     * 
     * @param roomNumber the ID of the current room
     * @param exit       the exit direction under consideration
     * @return {@code true} if the normal exit should be displayed,  
     *         {@code false} if it is suppressed by a special exit
     */
	public boolean displayExit(int roomNumber,String exit) {
		
		String[] exitDescriptions =  specialExits.getOrDefault(roomNumber, new String[] {"",""});
		return !exitDescriptions[0].equals(exit);
	}
	
    /**
     * Retrieves the special exit description for a given room.
     * <p>
     * If the room has a custom exit description, that string is returned.
     * </p>
     * 
     * @param roomNumber the ID of the current room
     * @param itemList   the list of all items, used to check trapdoor state
     * @return the custom exit description, or an empty string if none exists
     */
	public String getSpecialExit(int roomNumber, Item[] itemList) {
		
		String[] exitDescriptions = specialExits.getOrDefault(roomNumber, new String[]{"", ""});
		String baseDescription = exitDescriptions[1];
		
		return baseDescription;
	}
}

/* 3 December 2025 - Created File
 * 7 December 2025 - Removed game related code
 * 8 December 2025 - Increased version number
 */
