package com.example.minesweeper;

public class Round {
        private  int totalMines;
        private  int totalTries;
        private int totalTime;
        private  String winner;

        public Round(int totalMines, int totalTries, int totalTime, String winner) {
            this.totalMines = totalMines;
            this.totalTries = totalTries;
            this.totalTime = totalTime;
            this.winner = winner;
        }

        public int getTotalMines() {
            return totalMines;
        }

        public int getTotalTries() {
            return totalTries;
        }

        public int getTotalTime() {
            return totalTime;
        }

        public String getWinner() {
            return winner;
        }

    }
