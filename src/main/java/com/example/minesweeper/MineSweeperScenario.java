package com.example.minesweeper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MineSweeperScenario {
    private int difficultyLevel;
    private int numMines;
    private int maxTime;
    private boolean HyperMineEnabled;

    int count = 0;

    public MineSweeperScenario(String fileName) throws IOException, InvalidDescriptionException, InvalidValueException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            while (reader.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (count != 4) {
            throw new InvalidDescriptionException("The number of lines is not correct!");
        }
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        // Read the first line
        String line = reader.readLine();
        if (line == null) {
            throw new InvalidDescriptionException("Missing difficulty level");
        }
        try {
            difficultyLevel = Integer.parseInt(line);
            if (difficultyLevel != 1 && difficultyLevel != 2) {
                throw new InvalidValueException("Invalid difficulty level: " + difficultyLevel);
            }
        } catch (NumberFormatException e) {
            throw new InvalidValueException("Invalid difficulty level: " + line);
        }

        // Read the second line
        line = reader.readLine();
        if (line == null) {
            throw new InvalidDescriptionException("Missing number of mines");
        }
        try {
            numMines = Integer.parseInt(line);
            if (difficultyLevel == 1 && (numMines < 9 || numMines > 11)) {
                throw new InvalidValueException("Invalid number of mines: " + numMines);
            } else if (difficultyLevel == 2 && (numMines < 35 || numMines > 45)) {
                throw new InvalidValueException("Invalid number of mines: " + numMines);
            }
        } catch (NumberFormatException e) {
            throw new InvalidValueException("Invalid number of mines: " + line);
        }

        // Read the third line
        line = reader.readLine();
        if (line == null) {
            throw new InvalidDescriptionException("Missing max time");
        }
        try {
            maxTime = Integer.parseInt(line);
            if (difficultyLevel == 1 && (maxTime < 120 || maxTime > 180)) {
                throw new InvalidValueException("Invalid max time: " + maxTime);
            } else if (difficultyLevel == 2 && (maxTime < 240 || maxTime > 360)) {
                throw new InvalidValueException("Invalid max time: " + maxTime);
            }
        } catch (NumberFormatException e) {
            throw new InvalidValueException("Invalid max time: " + line);
        }

        // Read the fourth line
        line = reader.readLine();
        if (line == null) {
            throw new InvalidDescriptionException("Missing Hyper Mine flag");
        }
        try {
            HyperMineEnabled = Integer.parseInt(line) == 1;
            if (difficultyLevel == 1 && HyperMineEnabled) {
                throw new InvalidValueException("Hyper Mine flag should be 0 for difficulty level 1");
            } else if (difficultyLevel == 2 && !HyperMineEnabled) {
                throw new InvalidValueException("Hyper Mine flag should be 1 for difficulty level 2");
            }
        } catch (NumberFormatException e) {
            throw new InvalidValueException("Invalid Hyper Mine flag: " + line);
        }

        // Check for extra lines
        line = reader.readLine();
        if (line != null) {
            throw new InvalidDescriptionException("Too many lines in scenario description");
        }
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public int getNumMines() {
        return numMines;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public boolean isHyperMineEnabled() {
        return HyperMineEnabled;
    }

}

class InvalidDescriptionException extends Exception {
    public InvalidDescriptionException(String message) {
        super(message);
    }
}

class InvalidValueException extends Exception {
    public InvalidValueException(String message) {
        super(message);
    }
}
