package com.example.minesweeper;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;


/**

 The MineField class represents a minefield that consists of a two-dimensional array of buttons.

 It handles some parts the game logic and the interactions between the player and the minefield.
 */
public class MineField {
    private int rows;
    private int columns;
    private int[][] minePositions;
    private static GridPane gridPane;
    private Button[][] buttons;
    private LeftClickHandler leftClickHandler;
    private CountdownTimer timer;

    /**
     Creates a MineField object with the specified number of rows and columns, and mine positions.
     @param rows the number of rows in the minefield
     @param columns the number of columns in the minefield
     @param minePositions the positions of the mines in the minefield
     */
    public MineField(int rows, int columns, int[][] minePositions) {
        this.rows = rows;
        this.columns = columns;
        this.minePositions = minePositions;
        this.gridPane = new GridPane();
        this.gridPane.setHgap(5);
        this.gridPane.setVgap(5);
        this.buttons = new Button[rows][columns];
        this.leftClickHandler = new LeftClickHandler(minePositions, rows, columns, this, 0, timer);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Button button = new Button();
                button.setMinSize(32, 32);
                button.setMaxSize(32, 32);
                button.setFocusTraversable(false); // Set focusTraversable property to false
                int finalI = i;
                int finalJ = j;
                button.setOnMouseClicked(event -> leftClickHandler.handle(event, finalI, finalJ));
                this.buttons[i][j] = button;
                this.gridPane.add(button, j, i);
            }
        }
    }

    /**
     Disables all buttons in the minefield.
     @return true if all buttons were successfully disabled, false otherwise
     */
    public boolean disableAllButtons() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                buttons[i][j].setDisable(true);
            }
        }
        return true;
    }

    /**
     Checks if the game is over by checking if all buttons are disabled.
     @return true if the game is over, false otherwise
     */
    public boolean checkIfGameIsOver() {
        boolean b = false;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (buttons[i][j].isDisabled()) {
                    b = true;
                } else {
                    b = false;
                    break;
                }
            }
            if (!b) {
                break;
            }
        }
        return b;
    }

    /**
     Reveals all mines in the minefield by setting the background of the corresponding buttons to a mine image.
     */
    public void revealAllMines() {
        for (int[] minePosition : minePositions) {
            int row = minePosition[0];
            int col = minePosition[1];
            Button button = buttons[row][col];
            button.setGraphic(null);
            button.setBackground(new Background(new BackgroundImage(
                    new Image("black_mine.png", 40, 40, true, true),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, new BackgroundSize(40, 40, true, true, true, false))));
        }
    }
    /**
     * Returns the button at the specified row and column in the MineField.
     *
     * @param row the row of the button
     * @param col the column of the button
     * @return the button at the specified row and column
     */
    public Button getButton(int row, int col) {
        return buttons[row][col];
    }
    /**
     * Returns the GridPane containing the buttons in the MineField.
     *
     * @return the GridPane containing the buttons in the MineField
     */
    public static GridPane getGridPane() {
        return gridPane;
    }

    /**
     * Returns the number of mines in the MineField.
     *
     * @return the number of mines in the MineField
     */
    public int getMinesCount() {
        return minePositions.length;
    }
    /**
     * Returns the number of unopened buttons in the MineField.
     *
     * @return the number of unopened buttons in the MineField
     */
    public int getUnopenedButtonsCount() {
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (!buttons[i][j].isDisabled()) {
                    count++;
                }
            }
        }
        return count;
    }

}
