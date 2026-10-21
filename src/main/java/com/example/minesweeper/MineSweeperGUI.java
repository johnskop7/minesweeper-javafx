package com.example.minesweeper;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import static com.example.minesweeper.LeftClickHandler.*;
import static com.example.minesweeper.LoadScenario.maxTime;

public class MineSweeperGUI extends Application {
    private String FileId;

    private int rows;
    private int columns;
    private int numberOfMines;
    private int numberofFlags;
    private static MineField mineField;
    private int[][] minePositions;
    private CountdownTimer timer;
    private int totalTime;




    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);

        // Set the background color
        BackgroundFill backgroundFill = new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);

        // create menu bar
        MenuBar menuBar = new MenuBar();
        Menu applicationMenu = new Menu("Application");
        MenuItem createMenuItem = new MenuItem("Create");
        MenuItem loadMenuItem = new MenuItem("Load");
        MenuItem startMenuItem = new MenuItem("Start");
        MenuItem exitMenuItem = new MenuItem("Exit");
        applicationMenu.getItems().addAll(createMenuItem, loadMenuItem, startMenuItem, exitMenuItem);
        Menu detailsMenu = new Menu("Details");
        MenuItem roundsMenuItem = new MenuItem("Rounds");
        MenuItem solutionsMenuItem = new MenuItem("Solutions");
        detailsMenu.getItems().addAll(roundsMenuItem, solutionsMenuItem);
        menuBar.getMenus().addAll(applicationMenu, detailsMenu);

        // create label for mine count, flag count, and timer
        Label infoLabel = new Label("Mines: " + numberOfMines + "      Used Flags: " + numberofFlags);
        Color treeBrown = Color.rgb(110, 79, 50);
        infoLabel.setStyle("-fx-font-size: 19px; -fx-font-weight: bold; -fx-text-fill:" + toRGBCode(treeBrown));
        Label timerLabel = new Label("Remaining Time: 0:00");
        timerLabel.setStyle("-fx-font-size: 19px; -fx-font-weight: bold; -fx-text-fill: brown;");


        // create welcome message
        Label welcomeLabel = new Label("Welcome to the Medialab\n MineSweeper");
        welcomeLabel.setStyle("-fx-border-color: black; -fx-font-size: 27px; -fx-font-weight:BOLD; -fx-padding: 20px; -fx-text-fill:black;-fx-shape: \"M50,0 C80,0 110,20 110,50 L110,150 C110,180 80,200 50,200 L-50,200 C-80,200 -110,180 -110,150 L-110,50 C-110,20 -80,0 -50,0 Z\";");
        welcomeLabel.setFont(Font.font("Calibre", FontWeight.BOLD, 30));
        welcomeLabel.setTextAlignment(TextAlignment.CENTER);
        welcomeLabel.setBackground(background);

        // create image
        ImageView imageView = new ImageView(new Image("grey_tiles.png"));
        imageView.fitWidthProperty().bind(primaryStage.widthProperty());
        imageView.fitHeightProperty().bind(primaryStage.heightProperty());

        // create stack pane
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(imageView, welcomeLabel, menuBar);
        StackPane.setAlignment(menuBar, Pos.TOP_LEFT);
        StackPane.setAlignment(welcomeLabel, Pos.CENTER);

        // create scene and set it to the stage
        Scene scene = new Scene(stackPane, 400, 400);
        primaryStage.setTitle("MediaLab MineSweeper");
        primaryStage.setScene(scene);
        primaryStage.show();

        // set up menu item actions
        createMenuItem.setOnAction(event -> {
            CreateScenario createScenario = new CreateScenario();
            createScenario.showCreateScenarioDialog();
        });

        loadMenuItem.setOnAction(event -> {
            LoadScenario loadscenario = new LoadScenario();
            loadscenario.showLoadScenarioDialog();
            FileId = loadscenario.getId();
        });

        startMenuItem.setOnAction(event -> {
            String Id = LoadScenario.FileId;

            if (Id == null || Initialization.isInvalidFile()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setContentText("Please load a scenario first!");
                alert.showAndWait();
            } else {
                if (timer != null) {
                    timer.stop();
                }
                Initialization.setInvalidFile(false);
                timer = new CountdownTimer(maxTime, timerLabel);
                timer.start();
                Initialization initialization1 = new Initialization(new java.io.File("medialab", FileId + ".txt").getPath());
                rows = LoadScenario.rows;
                columns = LoadScenario.columns;
                int numberOfMines = LoadScenario.numberOfMines;
                boolean hyperMineEnabled = LoadScenario.hyperMineEnabled;
                int maxTime = LoadScenario.maxTime;
                int hyperMineRow = initialization1.getHyperMineRow();
                int hyperMineCol = initialization1.getHyperMineCol();
                minePositions = initialization1.getMinePositions();
                infoLabel.setText("Mines: " + numberOfMines + "      Used Flags: 0");

                // Create Minefield instance
                mineField = new MineField(rows, columns, minePositions);
                GridPane gridPane = MineField.getGridPane();

                // Set the style of the GridPane to add a grey border
                gridPane.setStyle("-fx-border-color: grey; -fx-border-width: 4px;");

                // Create LeftClickHandler instance
                LeftClickHandler leftClickHandler = new LeftClickHandler(minePositions, rows, columns, mineField, maxTime, timer);

                // Create RightClickHandler instance
                RightClickHandler rightClickHandler = new RightClickHandler(numberOfMines, minePositions, rows, columns, mineField, hyperMineEnabled, hyperMineRow, hyperMineCol);

                // Add mouse event handlers to cells
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        Button button = mineField.getButton(i, j);
                        int finalI = i;
                        int finalJ = j;
                        int finalI1 = i;
                        int finalJ1 = j;
                        button.setOnMouseClicked(event1 -> {
                            if (event1.getButton() == MouseButton.PRIMARY) {
                                leftClickHandler.handle(event1, finalI, finalJ);
                            } else if (event1.getButton() == MouseButton.SECONDARY) {
                                rightClickHandler.handle(event1, finalI1, finalJ1);
                                numberofFlags = rightClickHandler.getNumberOfFlags();
                                //update the info Label
                                infoLabel.setText("Mines: " + numberOfMines + "      Used Flags: " + numberofFlags);
                            }
                        });
                    }
                }

                //create root pane
                BorderPane root1 = new BorderPane();
                root1.setTop(menuBar);

                VBox vbox = new VBox();
                vbox.getChildren().addAll(infoLabel, timerLabel);
                vbox.setAlignment(Pos.CENTER);
                vbox.setStyle("-fx-border-color:" + toRGBCode(treeBrown) + "; -fx-border-width: 4px; ");

                VBox vbox_main = new VBox();
                vbox_main.getChildren().addAll(vbox, gridPane);
                vbox_main.setAlignment(Pos.TOP_CENTER);
                vbox_main.setFillWidth(true);
                vbox_main.setSpacing(1);
                vbox_main.setBackground(background);

                BorderPane centerPane = new BorderPane();
                centerPane.setCenter(vbox_main);
                centerPane.setStyle("-fx-border-color: grey; -fx-border-width: 4px;");
                root1.setCenter(centerPane);

                // Create scene and set it to the stage
                Scene scene1;
                if (hyperMineEnabled) {
                    scene1 = new Scene(root1, 600, 689);
                } else {
                    scene1 = new Scene(root1, 340, 430);
                }
                primaryStage.setTitle("MediaLab MineSweeper");
                primaryStage.setScene(scene1);
                primaryStage.show();
            }
        });

        exitMenuItem.setOnAction(event -> {
            Platform.exit();
        });

        roundsMenuItem.setOnAction(event -> {
            showRoundsDialog();
        });

        solutionsMenuItem.setOnAction(event -> {
            if (mineField == null || mineField.checkIfGameIsOver()) {
                return; // do nothing if the mineField is null or the game is already over
            } else {
                timer.stop();

                // Show message that the game is lost
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game over");
                alert.setHeaderText("You lost the game.");
                alert.setContentText("All mines will be revealed.");
                alert.showAndWait();
                mineField.revealAllMines();
                mineField.disableAllButtons();
                totalTime = maxTime - (timer.getMinutes()*60 + timer.getSeconds());
                addRound(LoadScenario.numberOfMines,totalTries,totalTime, "Computer");
            }
        });

    }


    public static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public static MineField getMineField() {
        return mineField;
    }

    private void showRoundsDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setTitle("Last Five Rounds");
        StringBuilder contentBuilder = new StringBuilder();
        //Collections.reverse(rounds); // show most recent rounds first
        for (int i = 0; i < Math.min(5, rounds.size()); i++) {
            Round round = rounds.get(i);
            contentBuilder.append(String.format("Round %d:\n", i + 1));
            contentBuilder.append(String.format("Total Mines: %d\n", round.getTotalMines()));
            contentBuilder.append(String.format("Total Tries: %d\n", round.getTotalTries()));
            contentBuilder.append(String.format("Total Time: %d seconds\n", round.getTotalTime()));
            contentBuilder.append(String.format("Winner: %s\n", round.getWinner()));
            contentBuilder.append("\n");
        }
        alert.setContentText(contentBuilder.toString());
        alert.showAndWait();
    }
}
