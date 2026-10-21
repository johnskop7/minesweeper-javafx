package com.example.minesweeper;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Scanner;


public class LoadScenario {
    public static int rows;
    public static int columns;
    public static int numberOfMines;
    public static int maxTime;
    public static boolean hyperMineEnabled;
    public static int hyperMineRow;
    public static int hyperMineCol;

    public static int[][] minePositions;

    public static String FileId;

    public void showLoadScenarioDialog() {
        TextInputDialog dialog = new TextInputDialog("SCENARIO-ID");
        dialog.setTitle("Load Scenario");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter the scenario ID:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(scenarioId -> {
            try {
                File file = new File("medialab", scenarioId + ".txt");
                Scanner scanner = new Scanner(file);
                Initialization init = new Initialization(file.getPath());
                rows = init.getRows();
                columns = init.getColumns();
                numberOfMines = init.getNumberOfMines();
                maxTime = init.getMaxTime();
                hyperMineEnabled = init.isHyperMine();
                hyperMineRow = init.getHyperMineRow();
                hyperMineCol = init.getHyperMineCol();
                minePositions = init.getMinePositions();
                FileId = scenarioId;
            } catch (FileNotFoundException e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Scenario not found");
                alert.showAndWait();
            }
        });

    }
    public String getId() {
        return FileId;
    }

}
