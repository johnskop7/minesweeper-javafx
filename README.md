# Minesweeper - JavaFX Desktop Game

A desktop implementation of Minesweeper built with **Java 19**, **JavaFX**, and **Maven** for the Multimedia Technology course at the School of Electrical and Computer Engineering, National Technical University of Athens (NTUA).

The application extends the classic Minesweeper rules with configurable scenarios, two difficulty levels, a countdown timer, a special **Hypermine** mechanic, game-history tracking, and scenario validation.

## Features

- Interactive JavaFX graphical interface
- Configurable game scenarios
- Two difficulty levels with different board sizes and mine counts
- Countdown timer and automatic game termination
- Left-click cell revealing and right-click flag placement
- Recursive revealing of neighboring empty cells
- Optional Hypermine mechanic
- Scenario creation, loading, and validation
- Custom exceptions for invalid scenario descriptions and values
- Display of the five most recent completed rounds
- Solution/reveal functionality
- File-based storage of scenario descriptions and generated mine positions

## Technologies

- Java 19
- JavaFX 19
- Maven
- Object-oriented programming
- Event-driven GUI programming
- File I/O
- Exception handling

## Project Structure

```text
minesweeper-javafx/
├── README.md
├── pom.xml
├── src/
│   └── main/
│       ├── java/com/example/minesweeper/
│       │   ├── CountdownTimer.java
│       │   ├── CreateScenario.java
│       │   ├── Initialization.java
│       │   ├── LeftClickHandler.java
│       │   ├── LoadScenario.java
│       │   ├── MineField.java
│       │   ├── MineSweeperGUI.java
│       │   ├── MineSweeperScenario.java
│       │   ├── RightClickHandler.java
│       │   └── Round.java
│       └── resources/
├── medialab/
│   └── example scenario files
└── docs/
    ├── course-handout.pdf
    └── report.pdf
```

## Running the Application

### Requirements

- JDK 19 or newer
- Maven

From the repository root:

```bash
mvn clean javafx:run
```

The JavaFX Maven plugin launches `com.example.minesweeper.MineSweeperGUI`.

## Scenario Format

Scenario files are stored in `medialab/` and contain four lines describing:

1. difficulty level
2. number of mines
3. maximum game time
4. whether the Hypermine mechanic is enabled

The application validates the scenario before starting a game and reports invalid descriptions or values to the user.

## Architecture

The implementation separates responsibilities across classes: `MineSweeperGUI` manages the main interface and menus, `MineField` represents the game board, click-handler classes implement user interaction, `Initialization` creates mine layouts, `MineSweeperScenario` validates scenario files, and `CountdownTimer` manages time-limited rounds.

## Documentation

- `docs/course-handout.pdf` — original NTUA Multimedia Technology project handout
- `docs/report.pdf` — original short implementation report, with personal identifiers removed for public sharing

The assignment specifies the JavaFX GUI, two scenario difficulty levels, scenario validation through custom exceptions, countdown-based gameplay, recursive cell revealing, the Hypermine mechanic, and five-round history.

## Coursework Note

This repository is a cleaned portfolio version of a project developed for the Multimedia Technology course at NTUA. IDE metadata and compiled build artifacts have been removed, repository-relative paths have been made portable across operating systems, and the Maven launcher configuration has been corrected. The underlying game implementation remains faithful to the original coursework.

## Author

**Ioannis Litsos**  
Electrical and Computer Engineering, NTUA
