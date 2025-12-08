# Island of Secrets

A retro-type-in-text adventure that has been converted from BASIC to Java

---

## Table of Contents
1. [Overview](#overview)
2. Features
3. Installation & Running
4. [Controls](#controls)
5. [Program Structure](#program-structure)
6. [Development Guide](#development-guide)
7. Known Issues & Roadman
8. [Credits](#credits)
9. License

---

##Overview

This project is a modern translation of the game <Game Name>, originally published as a BASIC listing in a book of the same name. Players would type the game into their computers to play it. However, the authors intentionally obfuscated the code to make it difficult for users to deduce the game’s solution, while still leaving the game playable. 

This approach came with challenges: errors often crept in during transcription, and the lack of comments or documentation made it hard for users to learn the tricks of the BASIC language. The code’s readability was also notoriously poor, adding to the difficulty.

- [Read the book on the Internet Archive] 
- [View the original BASIC version on GitHub]

**Tech Stack:**
- Language: Java 18.0.2-ea
- GUI Framework: Swing
- Build Tool: JDK (javac/java)

### Notes on the Basic Code

**Reading the Data**



## Installation & Running

### Prerequisites
- Java 18.0.2-ea or higher

### Build & Run from the Command Line

```bash
# Compile
javac -d out $(find . -name "*.java")

You will need to copy the Images directory across to the compiled directory
cp -r /Images /out

# Run
java -cp out <package>.Main
```

### Run from IDE
- Open project in Eclipse
- Run the Start Class

---

## Controls

The game is controlled through an input line where a verb/noun command is entered. Most actions use either one or two commands. With the give command, you can include who you are giving the item to, and the say command can also have a phrase.

There are buttons for use, including a map button which will display a map of the game, with locations visited.

Below the map buttons are the three previous commands entered, and clicking on the button repeats the command.

On startup there is a button that opens a webpage in the Internet Archive which contains a copy of the book (since the original book contains hints and clues for the game).

The load command will display a list of options that can be clicked. With the load command you have buttons that will allow you to cycle through the options.

## Program Structure

### Packages

* **default package** — Entry point only, contains `Start` class.
* **Model** — Core game logic and state (`Main`, `Game`, `Player`, etc.).
* **Game** — Contains the classes that hold the game variables
* **View** — GUI components and rendering logic.
* **Controller** — Handles input and game loop logic.
* **Commands** — Processes the commands
* **Data** — Holds all of the data
* **Images** — Holds the images for the map
* **Interfaces** — Contains the interfaces for the game

### Main Entry Points

* `Start` — Development/test launcher.
* `Main` — Official game entry point for production (needs to be updated).

### Game Flow

1. Game starts via `Main.main()` or `Start.main()`
2. Initializes `Game` and `Player`
3. Loads assets and GUI via `GameFrame`
4. Enters main game loop until exit

*Full JavaDocs available in `/docs`.*

## Development Guide

### Coding Conventions

* Follow standard Java naming conventions.
* All public methods/classes must have Javadoc comments.
* Use `TODO` tags for unfinished sections.

### Adding New Content

This is a blank template to be used to build a grid adventure (where the locations are laid out in a grid).

### Testing

## Known Issues & Roadmap

## Development Notes ##

### Version 1 (v01)
- **Status**: Blank code to create a new adventure using the template


---

## Credits
* **Lead Developer:** David Sarkies

### Image Acknowledgements ###