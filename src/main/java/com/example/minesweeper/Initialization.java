package com.example.minesweeper;

import javafx.scene.control.Alert;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Initialization {
    public static int rows;
    private int columns;
    private static int numberOfMines;
    private int MaxTime;
    private boolean hyperMine;
    private int hyperMineRow;
    private int hyperMineCol;
    private int[][] minePositions;
    private Random random;
    private static boolean InvalidFile = false;


    public Initialization(String scenarioFile) {
        try {
            MineSweeperScenario scenario = new MineSweeperScenario(scenarioFile);
            numberOfMines = scenario.getNumMines();
            MaxTime = scenario.getMaxTime();
            hyperMine = scenario.isHyperMineEnabled();
            InvalidFile = false;
            if (scenario.getDifficultyLevel() == 1) {
                rows = 9;
                columns = 9;
            } else {
                rows = 16;
                columns = 16;
            }
        } catch (IOException e) {
            System.err.println("Error reading scenario file: " + e.getMessage());
        } catch (InvalidDescriptionException e) {
            InvalidFile = true;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Description : Please load a valid Scenario!");
            alert.showAndWait();
            System.err.println("InvalidDescriptionException: " + e.getMessage());
        } catch (InvalidValueException e) {
            InvalidFile = true;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid values: Please load a valid Scenario!");
            alert.showAndWait();
            System.err.println("InvalidValueException: " + e.getMessage());
        }
        initializeMinePositions();
        if (hyperMine) {
            initializeHyperMine();
        }
        WriteMinePositions();
    }


    void initializeMinePositions() {
        try {
            minePositions = new int[numberOfMines][2];
            ArrayList<Integer> positions = new ArrayList<>();
            for (int i = 0; i < rows * columns; i++) {
                positions.add(i);
            }
            random = new Random();

            for (int i = 0; i < numberOfMines; i++) {
                int position = positions.remove(random.nextInt(positions.size()));
                minePositions[i][0] = position / columns;
                minePositions[i][1] = position % columns;
            }
        }
        catch (IllegalArgumentException e){
            return;

        }
    }


    private void initializeHyperMine() {
        int index = random.nextInt(numberOfMines);
        hyperMineRow = minePositions[index][0];
        hyperMineCol = minePositions[index][1];
    }

    private void WriteMinePositions() {
        try {
            FileWriter writer = new FileWriter(new File("medialab", "mines.txt"), false);
            writer.write("");
            writer.close();
        } catch (IOException e) {
            System.err.println("Error clearing file: " + e.getMessage());
        }

        for (int i = 0; i < numberOfMines; i++) {
            try {
                FileWriter writer = new FileWriter(new File("medialab", "mines.txt"), true);
                if (minePositions[i][0] == hyperMineRow & minePositions[i][1] == hyperMineCol) {
                    writer.write(minePositions[i][0] + "," + minePositions[i][1] + "," + "1\n");
                } else {
                    writer.write(minePositions[i][0] + "," + minePositions[i][1] + "," + "0\n");
                }
                writer.close();
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        }
    }

    public int getRows() {
        return rows;
    }


    public int getColumns() {
        return columns;
    }


    public int getHyperMineRow() {
        return hyperMineRow;
    }


    public int getHyperMineCol() {
        return hyperMineCol;
    }


    public int[][] getMinePositions() {
        return minePositions;
    }


    public static int getNumberOfMines() {
        return numberOfMines;
    }


    public int getMaxTime() {
        return MaxTime;
    }


    public boolean isHyperMine() {
        return hyperMine;
    }

    public static boolean isInvalidFile() {
        return InvalidFile;
    }

    public static void setInvalidFile(boolean invalidFile) {
        InvalidFile = invalidFile;
    }
}
