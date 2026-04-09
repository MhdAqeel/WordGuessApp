package com.example.wordguess.data.model;

public class GameState {
    public String playerName = "";
    public String secretWord = "";
    public int score = 100;
    public int attemptsUsed = 0;
    public int maxAttempts = 10;
    public int level = 1;
    public boolean tipUsed = false;
    public String tipText = "";
    public String message = "Guess the secret word!";
    public long elapsedSeconds = 0;
    public boolean roundOver = false;
    public boolean roundWon = false;
}