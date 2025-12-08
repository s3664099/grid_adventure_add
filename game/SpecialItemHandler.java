/*
Title: <Game Name> Special Item Handler Class
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
import data.Location;

/**
 * Computes special item descriptions for specific rooms based on game state.
 * <br>
 * This handler returns contextual descriptions (e.g., items mounted, hidden, or revealed)
 * depending on item flags/locations and whether a room has been viewed.
 * If no special description should be shown, an empty string is returned.
 */
public class SpecialItemHandler implements Serializable {

	private static final long serialVersionUID = -3392796825592359959L;

    /** Static mapping of room number to its base special description. */
	private static final Map<Integer, String> itemDescriptions = new HashMap<>();
	
    /**
     * Initializes the static special descriptions for known rooms.
     * Uses {@link GameEntities} room constants as keys.
     */
	public SpecialItemHandler() {
		//itemDescriptions.put(GameEntities.ROOM_CLEARING,"A tree bristling with apples");
	}
	
    /**
     * Returns the context-sensitive special item description for a room.
     * <ul>
     *   <li>If the room has no special description, returns an empty string.</li>
     *   <li>If a special item is not present/visible (based on item flags/locations or room viewed state), returns an empty string.</li>
     * </ul>
     *
     * @param roomNumber   the current room number
     * @param itemList     the array of {@link Item} instances (indexed by {@link GameEntities} item constants)
     * @param locationList the array of {@link Location} instances (indexed by room number)
     * @return the special description for the room, or an empty string if none should be shown
     */
	public String getSpecialItems(int roomNumber,Item[] itemList, Location[] locationList) {
		String description = itemDescriptions.getOrDefault(roomNumber,"");
		return description;
	}
}

/* 3 December 2025 - Created File
 * 7 December 2025 - Removed game related code
 * 8 December 2025 - Increased version
 */
