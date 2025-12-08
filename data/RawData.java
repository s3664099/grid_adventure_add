/*
Title: <Game Name> Raw Data
Author: 
Translator: David Sarkies
Version: 1.0
Date: 8 December 2025
Source:
*/

package data;

public class RawData {
	
	
	private static final Integer[] LOCATION_TYPES = {};
	
    private static final String[] LOCATION_IMAGE = {};
		    
	private static final String[] LOCATIONS = {};
		
	private static final String[] OBJECTS = {};
		
	private static final String[] VERBS = {};
	
	private static final String[] NOUNS = {};
			
	private static final String ITEM_LOCATION = "";
	private static final String ITEM_FLAG = "";
	private static final String[] PREPOSITIONS = {};
	
	public static String getLocation(int number) {
		
		if (number<0 || number >= LOCATIONS.length) {
			throw new IllegalArgumentException("Raw Data - Invalid location number: "+number);
		}
		
		return LOCATIONS[number];
	}
	
	public static String getImage(int number) {
		
		if (number<0 || number >= LOCATION_TYPES.length) {
			throw new IllegalArgumentException("Raw Data - Invalid location type number: "+number);
		}
		
		return LOCATION_IMAGE[LOCATION_TYPES[number]-1];
	}
	
	public static String getObjects(int number) {
				
		if (number<0 || number >= OBJECTS.length+1) {
			throw new IllegalArgumentException("Raw Data - Invalid object number: "+number);
		}
		
		return OBJECTS[number-1];
	}

	public static String[] getPrepositions() {
		return PREPOSITIONS;
	}
		
	public static char getItemLocation(int number) {
		
		if (number<0 || number >= ITEM_LOCATION.length()) {
			throw new IllegalArgumentException("Raw Data - Invalid object location number: "+number);
		}
		
		return ITEM_LOCATION.charAt(number-1);
	}
	
	public static char getItemFlag(int number) {
		
		if (number<0 || number >= ITEM_FLAG.length()) {
			throw new IllegalArgumentException("Raw Data - Invalid object flag number: "+number);
		}		
		
		return ITEM_FLAG.charAt(number-1);
	}
	
	public static String[] getVerbs() {
		return VERBS;
	}
	
	public static String[] getNouns() {
		return NOUNS;
	}
}
/* 3 December 2025 - Created File
 * 6 December 2025 - Removed game specific code
 * 8 December 2025 - Increased version number
 */