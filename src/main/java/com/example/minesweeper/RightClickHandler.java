package com.example.minesweeper;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class RightClickHandler {
    private int[][] minePositions;
    private int numberOfMines;
    private int rows;
    private int columns;
    private MineField mineField;
    private boolean hyperMineEnabled;
    private int hyperMineRow;
    private int hyperMineCol;
    private int numberOfFlags;


    private static int revealedMines = 0;

    public RightClickHandler(int numberOfMines, int[][] minePositions, int rows, int columns, MineField mineField, boolean hyperMineEnabled, int hyperMineRow, int hyperMineCol) {
        this.minePositions = minePositions;
        this.rows = rows;
        this.columns = columns;
        this.mineField = mineField;
        this.hyperMineEnabled = hyperMineEnabled;
        this.hyperMineRow = hyperMineRow;
        this.hyperMineCol = hyperMineCol;
        this.numberOfFlags = 0;
        this.numberOfMines = numberOfMines;
    }

    public void handle(MouseEvent event, int row, int col) {
        Image flagImage = new Image("flag.png");
        ImageView flagImageView = new ImageView(flagImage);
        flagImageView.setPreserveRatio(true);
        flagImageView.setFitHeight(32);
        flagImageView.setFitWidth(32);
        Button button = mineField.getButton(row, col);
        if (button.isDisabled()) {
            return;
        }
        if (event.getButton() == MouseButton.SECONDARY) {
            if (button.getGraphic() == null) {
                if (numberOfFlags < mineField.getMinesCount()) {
                    flagImageView.setTranslateX(button.getTranslateX());
                    flagImageView.setTranslateY(button.getTranslateY());
                    button.setGraphic(flagImageView);
                    numberOfFlags++;
                    if (hyperMineEnabled && row == hyperMineRow && col == hyperMineCol && LeftClickHandler.getTotalTries() <= 4) {
                        revealHyperMineNeighbors();
                    }
                }
            } else {
                button.setGraphic(null);
                numberOfFlags--;
            }
        }
    }

    private void revealHyperMineNeighbors() {
        for (int i = 0; i < rows; i++) {
            Button b = mineField.getButton(i, hyperMineCol);
            if (!b.isDisabled()) {
                boolean isMine = false;
                for (int k = 0; k < numberOfMines; k++) {
                    if (minePositions[k][0] == i && minePositions[k][1] == hyperMineCol) {
                        isMine = true;
                        break;
                    }
                }
                if (isMine) {
                    b.setDisable(true);
                    b.setGraphic(null);
                    b.setBackground(new Background(new BackgroundImage(
                            new Image("black_mine.png", 32, 32, true, true),
                            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.CENTER, new BackgroundSize(32, 32, true, true, true, false))));
                    revealedMines++;
                } else {
                    b.setGraphic(null);
                    b.setDisable(true);
                    int neighborMines = getNeighborMines(i, hyperMineCol);
                    if (neighborMines > 0) {
                        b.setText(Integer.toString(neighborMines));
                    }
                }
            }
        }
        for (int j = 0; j < columns; j++) {
            Button b = mineField.getButton(hyperMineRow, j);
            if (!b.isDisabled()) {
                boolean isMine = false;
                for (int k = 0; k < numberOfMines; k++) {
                    if (minePositions[k][0] == hyperMineRow && minePositions[k][1] == j) {
                        isMine = true;
                        break;
                    }
                }
                b.setGraphic(null);
                b.setDisable(true);
                if (isMine) {
                    b.setBackground(new Background(new BackgroundImage(
                            new Image("black_mine.png", 32, 32, true, true),
                            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.CENTER, new BackgroundSize(32, 32, true, true, true, false))));
                    revealedMines++;
                } else {
                    int neighborMines = getNeighborMines(hyperMineRow, j);
                    if (neighborMines > 0) {
                        b.setText(Integer.toString(neighborMines));
                    }
                }
            }
        }
    }

    int getNeighborMines(int row, int col) {
        int count = 0;
        for (int i = Math.max(0, row - 1); i <= Math.min(rows - 1, row + 1); i++) {
            for (int j = Math.max(0, col - 1); j <= Math.min(columns - 1, col + 1); j++) {
                if (i == row && j == col) {
                    continue;
                }
                for (int[] minePosition : minePositions) {
                    if (minePosition[0] == i && minePosition[1] == j) {
                        count++;
                        break;
                    }
                }
            }
        }
        return count;
    }

    public static int getRevealedMines() {
        return revealedMines;
    }

    public int getNumberOfFlags() {
        return numberOfFlags;
    }
}
