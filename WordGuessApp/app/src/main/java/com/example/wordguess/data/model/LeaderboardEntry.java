package com.example.wordguess.data.model;

public class LeaderboardEntry {
    public String name;
    public int score;
    public long timeSeconds;
    public int level;
    public String timestamp;

    public LeaderboardEntry(String name, int score, long timeSeconds, int level, String timestamp) {
        this.name = name;
        this.score = score;
        this.timeSeconds = timeSeconds;
        this.level = level;
        this.timestamp = timestamp;
    }
}