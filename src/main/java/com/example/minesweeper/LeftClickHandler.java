package com.example.minesweeper;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;




public class LeftClickHandler {

    private int[][] minePositions;
    private int rows;
    private int columns;
    private MineField mineField;
    private int maxTime;

    private CountdownTimer timer;
    private String winner;
    private int totalMines;
    public static int totalTries = 0;
    private int totalTime;
    public static List<Round> rounds = new ArrayList<>();

    public LeftClickHandler(int[][] minePositions, int rows, int columns, MineField mineField, int maxTime, CountdownTimer timer) {
        this.minePositions = minePositions;
        this.rows = rows;
        this.columns = columns;
        this.mineField = mineField;
        this.maxTime = maxTime;
        this.timer = timer;
    }

    public void handle(MouseEvent event, int row, int col) {
        boolean isMine = false;
        totalTries = totalTries + 1;
        for (int i = 0; i < minePositions.length; i++) {
            if (minePositions[i][0] == row && minePositions[i][1] == col) {
                isMine = true;
                break;
            }
        }
        Button button = mineField.getButton(row, col);
        if (button.getGraphic() != null) {
            return;
        } else {
            if (isMine) {
                timer.stop();
                mineField.revealAllMines();
                mineField.disableAllButtons();
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Game Over");
                alert.setHeaderText(null);
                alert.setContentText("You have clicked on a mine. Game over!");
                alert.showAndWait();
                winner = "Computer" ;
                totalMines = mineField.getMinesCount();
                totalTime = maxTime - (timer.getMinutes()*60 + timer.getSeconds());
                addRound(totalMines , totalTries - 1 , totalTime , winner);
                totalTries = 0;

            } else {
                int count = countNeighboringMines(row, col);
                button.setText(count > 0 ? String.valueOf(count) : "");
                button.setDisable(true);
                if (count == 0) {
                    revealNeighbors(row, col);
                }

                if (mineField.getUnopenedButtonsCount() == mineField.getMinesCount() - RightClickHandler.getRevealedMines()) {
                    timer.stop();
                    mineField.disableAllButtons();
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Congratulations!");
                    alert.setHeaderText(null);
                    alert.setContentText("You have won the game!");
                    alert.showAndWait();
                    winner = "User";
                    totalMines = mineField.getMinesCount();
                    totalTime = maxTime - (timer.getMinutes()*60 + timer.getSeconds());
                    addRound(totalMines , totalTries , totalTime , winner);
                    totalTries = 0;
                }
            }
        }
    }

    int countNeighboringMines(int row, int col) {
        int count = 0;
        for (int i = Math.max(0, row - 1); i <= Math.min(rows - 1, row + 1); i++) {
            for (int j = Math.max(0, col - 1); j <= Math.min(columns - 1, col + 1); j++) {
                if (i == row && j == col) {
                    continue;
                }
                for (int k = 0; k < minePositions.length; k++) {
                    if (minePositions[k][0] == i && minePositions[k][1] == j) {
                        count++;
                        break;
                    }
                }
            }
        }
        return count;
    }

    void revealNeighbors(int row, int col) {
        for (int i = Math.max(0, row - 1); i <= Math.min(rows - 1, row + 1); i++) {
            for (int j = Math.max(0, col - 1); j <= Math.min(columns - 1, col + 1); j++) {
                Button button = mineField.getButton(i, j);
                if (!button.isDisabled()) {
                    int count = countNeighboringMines(i, j);
                    button.setText(count > 0 ? String.valueOf(count) : "");
                    button.setGraphic(null);
                    button.setDisable(true);
                    if (count == 0) {
                        revealNeighbors(i, j);
                    }
                }
            }
        }
    }
    public static void addRound(int totalMines, int totalTries, int totalTime, String winner) {
        rounds.add(new Round(totalMines, totalTries, totalTime, winner));
        if (rounds.size() > 5) {
            rounds.remove(0);
        }
    }

    public static int getTotalTries() {
        return totalTries;
    }
}
