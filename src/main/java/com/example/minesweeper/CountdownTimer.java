package com.example.minesweeper;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.minesweeper.LeftClickHandler.addRound;


public class CountdownTimer {
    private final int maxTime;
    private int remainingTime;
    public Timer timer;
    public TimerTask task;
    private Label timerLabel;
    public static boolean running;
    private TimerListener listener;
    private int minutes;
    private int seconds;
    private int totalMines;
    private String winner;
    private int totalTries;
    private MineField minefield;

    public CountdownTimer(int maxTime, Label timerLabel) {
        this.maxTime = maxTime;
        this.remainingTime = maxTime;
        this.timerLabel = timerLabel;
        this.running = false;
        this.timer = new Timer();
    }

    public void start() {
        if (running) {
            stop();
        }
        running = true;
        task = new TimerTask() {
            @Override
            public void run() {
                remainingTime--;
                int minutes = remainingTime / 60;
                int seconds = remainingTime % 60;
                Platform.runLater(() -> {
                    timerLabel.setText("Remaining Time: " + String.format("%d:%02d", minutes, seconds));
                });
                if (remainingTime == 0) {
                    timer.cancel();
                    Platform.runLater(() -> {
                        minefield = MineSweeperGUI.getMineField();
                        minefield.disableAllButtons();
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText(null);
                        alert.setContentText("You are out of time! You lost!");
                        alert.showAndWait();
                        winner = "Computer";
                        totalMines = Initialization.getNumberOfMines();
                        totalTries = LeftClickHandler.getTotalTries();
                        addRound(totalMines, totalTries, maxTime, winner);
                        totalTries = 0;
                    });

                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }


    public void stop() {
        running = false;
        task.cancel();
        timer.cancel();
        minutes = remainingTime / 60;
        seconds = remainingTime % 60;
        remainingTime = maxTime;
        if (listener != null) {
            listener.onStop();
        }
    }

    public interface TimerListener {
        void onStop();
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

}
