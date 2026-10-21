package com.example.minesweeper;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.*;
import java.util.Optional;

public class CreateScenario {

    public void showCreateScenarioDialog() {
        Label titleLabel = new Label("Create Scenario");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label difficultyLabel = new Label("Difficulty Level:");
        ChoiceBox<Integer> difficultyChoiceBox = new ChoiceBox<>();
        difficultyChoiceBox.setValue(1);
        difficultyChoiceBox.getItems().addAll(1, 2);
        Label numMinesLabel = new Label("Number of Mines:");
        ChoiceBox<Integer> numMinesChoiceBox = new ChoiceBox<>();
        numMinesChoiceBox.getItems().addAll(9, 10, 11);
        numMinesChoiceBox.setValue(9);
        Label maxTimeLabel = new Label("Maximum Time:\n (in seconds)");
        TextField maxTimeTextField = new TextField();
        HBox maxTimeBox = new HBox();
        maxTimeTextField.setText("120");
        maxTimeTextField.setPrefWidth(60);
        maxTimeTextField.setEditable(true);
        Button plusButton = new Button("+");
        Button minusButton = new Button("-");

        plusButton.setOnAction(e -> {
            int maxTime = Integer.parseInt(maxTimeTextField.getText());
            maxTime += 1;
            maxTimeTextField.setText(String.valueOf(maxTime));
        });
        minusButton.setOnAction(e -> {
            int maxTime = Integer.parseInt(maxTimeTextField.getText());
            maxTime -= 1;
            maxTimeTextField.setText(String.valueOf(maxTime));
        });
        maxTimeBox.getChildren().addAll(plusButton, maxTimeTextField, minusButton);
        Label hypermineLabel = new Label("Include Hypermine:");
        RadioButton yesRadioButton = new RadioButton("Yes");
        RadioButton noRadioButton = new RadioButton("No");
        ToggleGroup hypermineToggleGroup = new ToggleGroup();
        yesRadioButton.setToggleGroup(hypermineToggleGroup);
        noRadioButton.setToggleGroup(hypermineToggleGroup);
        VBox hypermineBox = new VBox(5, yesRadioButton, noRadioButton);
        hypermineBox.setStyle("-fx-border-color: grey; -fx-border-width: 1px; -fx-padding: 4px;");
        hypermineBox.setAlignment(Pos.CENTER_LEFT);
        hypermineBox.setSpacing(10);
        Button createButton = new Button("Create");
        Button cancelButton = new Button("Cancel");
        HBox buttonBox = new HBox(createButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setSpacing(10);

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Create Scenario");
        dialog.setHeaderText(null);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 10, 10, 10));
        gridPane.add(titleLabel, 0, 0, 2, 1);
        gridPane.add(difficultyLabel, 0, 1);
        gridPane.add(difficultyChoiceBox, 1, 1);
        gridPane.add(numMinesLabel, 0, 2);
        gridPane.add(numMinesChoiceBox, 1, 2);
        gridPane.add(maxTimeLabel, 0, 3);
        gridPane.add(maxTimeBox, 1, 3);
        gridPane.add(hypermineLabel, 0, 4);
        gridPane.add(hypermineBox, 1, 4);
        dialog.getDialogPane().setContent(gridPane);

        difficultyChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.equals(1)) {
                numMinesChoiceBox.getItems().setAll(9, 10, 11);
                numMinesChoiceBox.setValue(9);
                maxTimeTextField.setText("120");
            } else if (newVal.equals(2)) {
                numMinesChoiceBox.getItems().setAll(35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45);
                numMinesChoiceBox.setValue(35);
                maxTimeTextField.setText("240");
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                if (difficultyChoiceBox.getValue() == null ||
                        numMinesChoiceBox.getValue() == null ||
                        maxTimeTextField.getText().isEmpty() ||
                        hypermineToggleGroup.getSelectedToggle() == null
                ) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Please fill all the fields");
                    alert.showAndWait();
                    return null;
                }

                int difficultyLevel = difficultyChoiceBox.getValue();
                int numMines = numMinesChoiceBox.getValue();
                int maxTime = Integer.parseInt(maxTimeTextField.getText());
                int includeHypermine;
                if (hypermineToggleGroup.getSelectedToggle() == yesRadioButton) {
                    includeHypermine = 1;
                } else {
                    includeHypermine = 0;
                }

                TextInputDialog dialog1 = new TextInputDialog("SCENARIO-ID");
                dialog1.setTitle("Save Scenario");
                dialog1.setHeaderText(null);
                dialog1.setContentText("Please enter a filename:");

                Scenario scenario = new Scenario(difficultyLevel, numMines, maxTime, includeHypermine);
                Optional<String> result = dialog1.showAndWait();

                if ((result).isPresent()) {
                    String filename = result.get() + ".txt";
                    saveScenario(scenario, new File("medialab", filename));
                }
            }

            return null;
        });

        dialog.showAndWait();

    }

    public void saveScenario(Scenario scenario, File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println(scenario.getDifficultyLevel());
            writer.println(scenario.getNumMines());
            writer.println(scenario.getMaxTime());
            writer.println(scenario.isIncludeHypermine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static class Scenario implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        private final int difficultyLevel;
        private final int numMines;
        private final int maxTime;
        private final int hasHypermine;

        public Scenario(int difficultyLevel, int numMines, int maxTime, int hasHypermine) {
            this.difficultyLevel = difficultyLevel;
            this.numMines = numMines;
            this.maxTime = maxTime;
            this.hasHypermine = hasHypermine;
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

        public int isIncludeHypermine() {
            return hasHypermine;
        }
    }


}
